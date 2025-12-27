package kz.shyngys.repository.impl;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Writer;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.util.PostMapper;
import kz.shyngys.util.WriterMapper;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    private static final String SQL_GET_WRITER_WITH_POSTS_BY_ID = "SELECT w.id, w.first_name, w.last_name, " +
            "p.id, p.content, p.created, p.updated, p.status " +
            "FROM writers w " +
            "LEFT JOIN posts p ON w.id = p.writer_id " +
            "WHERE w.id = ?";

    private static final String SQL_GET_ALL_WRITERS = "SELECT w.id, w.first_name, w.last_name FROM writers w";
    private static final String SQL_INSERT_WRITER = "INSERT INTO writers (first_name, last_name) VALUES (?, ?)";
    private static final String SQL_UPDATE_WRITER_BY_ID = "UPDATE writers SET first_name = ?, last_name = ? WHERE id = ?";
    private static final String SQL_DELETE_WRITER_BY_ID = "DELETE FROM writers WHERE id = ?";
    private static final String SQL_INSERT_POST = "INSERT INTO posts (content, status, writer_id) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_POST = "UPDATE posts SET content = ?, status = ? WHERE id = ?";
    private static final String SQL_INSERT_LABEL = "INSERT INTO labels (name) VALUES (?) " +
            "ON DUPLICATE KEY UPDATE name = name";
    private static final String SQL_GET_LABEL_BY_NAME = "SELECT id FROM labels WHERE name = ?";
    private static final String SQL_INSERT_POST_LABEL = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";

    @Override
    public Writer getById(Long id) {
        try (PreparedStatement ps = DatabaseUtils.getPreparedStatement(SQL_GET_WRITER_WITH_POSTS_BY_ID)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                Writer writer = null;
                List<Post> posts = new ArrayList<>();

                while (rs.next()) {
                    if (writer == null) {
                        writer = WriterMapper.toWriter(rs);
                    }

                    Post post = PostMapper.toPost(rs);
                    if (post.getId() != null && post.getId() != 0L) {
                        posts.add(post);
                    }
                }

                if (writer == null) {
                    throw new NotFoundException("Writer не найден с id: " + id);
                }

                writer.setPosts(posts);
                return writer;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении Writer по id: " + id, e);
        }
    }

    @Override
    public List<Writer> getAll() {
        try (PreparedStatement ps = DatabaseUtils.getPreparedStatement(SQL_GET_ALL_WRITERS);
             ResultSet rs = ps.executeQuery()) {

            List<Writer> writers = new ArrayList<>();

            while (rs.next()) {
                writers.add(WriterMapper.toWriter(rs));
            }

            if (writers.isEmpty()) {
                throw new NotFoundException("В таблице writers нет записей");
            }

            return writers;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех Writers", e);
        }
    }

    /**
     * Сохраняются посты, лейблы и создаются записи в post_labels
     * @param writer
     * @return
     */
    @Override
    public Writer save(Writer writer) {
        try {
            Long writerId = saveWriter(writer);
            writer.setId(writerId);

            if (!CollectionUtils.isEmpty(writer.getPosts())) {
                for (Post post : writer.getPosts()) {
                    Long postId = savePost(writerId, post);
                    post.setId(postId);

                    if (!CollectionUtils.isEmpty(post.getLabels())) {
                        savePostLabels(postId, post.getLabels());
                    }
                }
            }

            DatabaseUtils.commit();
            return writer;
        } catch (Exception e) {
            DatabaseUtils.rollback();
            throw new RuntimeException("Ошибка при сохранении Writer", e);
        }
    }

    /**
     * Если есть список постов, то следующая логика:
     * - если у поста есть id, то он изменяется в бд
     * - если у поста нет id, то он добавляется к писателю
     * @param writer
     * @return Writer
     */
    @Override
    public Writer update(Writer writer) {
        try {
            updateWriter(writer);

            List<Post> posts = writer.getPosts();
            if (!CollectionUtils.isEmpty(posts)) {
                writer.setPosts(savePosts(writer.getId(), posts));
            }

            DatabaseUtils.commit();
            return writer;
        } catch (Exception e) {
            DatabaseUtils.rollback();
            throw new RuntimeException("Ошибка при обновлении Writer с id: " + writer.getId(), e);
        }
    }

    @Override
    public void deleteById(Writer writer) {
        try (PreparedStatement ps = DatabaseUtils.getPreparedStatement(SQL_DELETE_WRITER_BY_ID)) {
            ps.setLong(1, writer.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Writer с id " + writer.getId() + " не найден для удаления");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении Writer с id: " + writer.getId(), e);
        }
    }

    private Long saveWriter(Writer writer) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_WRITER)) {
            ps.setString(1, writer.getFirstName());
            ps.setString(2, writer.getLastName());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Создание Writer не удалось, ни одна строка не добавлена");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                throw new SQLException("Создание Writer не удалось, id не получен");
            }
        }
    }

    private void updateWriter(Writer writer) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_UPDATE_WRITER_BY_ID)) {
            ps.setString(1, writer.getFirstName());
            ps.setString(2, writer.getLastName());
            ps.setLong(3, writer.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Writer с id " + writer.getId() + " не найден для обновления");
            }
        }
    }

    private Long savePost(Long writerId, Post post) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_POST)) {
            ps.setString(1, post.getContent());
            ps.setString(2, post.getStatus().name());
            ps.setLong(3, writerId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Создание Post не удалось, ни одна строка не добавлена");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                throw new SQLException("Создание Post не удалось, id не получен");
            }
        }
    }

    private List<Post> savePosts(Long writerId, List<Post> posts) throws SQLException {
        try (PreparedStatement psInsert = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_POST);
             PreparedStatement psUpdate = DatabaseUtils.getTransactionPreparedStatement(SQL_UPDATE_POST)) {

            List<Post> postsToInsert = new ArrayList<>();

            for (Post post : posts) {
                if (post.getId() != null) {
                    psUpdate.setString(1, post.getContent());
                    psUpdate.setString(2, post.getStatus().name());
                    psUpdate.setLong(3, post.getId());
                    psUpdate.addBatch();
                } else {
                    psInsert.setString(1, post.getContent());
                    psInsert.setString(2, post.getStatus().name());
                    psInsert.setLong(3, writerId);
                    psInsert.addBatch();
                    postsToInsert.add(post);
                }
            }

            psInsert.executeBatch();
            psUpdate.executeBatch();

            try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                int index = 0;
                while (generatedKeys.next() && index < postsToInsert.size()) {
                    postsToInsert.get(index).setId(generatedKeys.getLong(1));
                    index++;
                }
            }

            return posts;
        }
    }

    private void savePostLabels(Long postId, List<Label> labels) throws SQLException {
        for (Label label : labels) {
            Long labelId = getOrCreateLabel(label.getName());
            label.setId(labelId);
            linkPostLabel(postId, labelId);
        }
    }

    private Long getOrCreateLabel(String name) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_GET_LABEL_BY_NAME)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }

        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_LABEL)) {
            ps.setString(1, name);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Создание Label не удалось");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("Не удалось получить ID label");
            }
        }
    }

    private void linkPostLabel(Long postId, Long labelId) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_INSERT_POST_LABEL)) {
            ps.setLong(1, postId);
            ps.setLong(2, labelId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Не удалось связать Post и Label");
            }
        }
    }
}