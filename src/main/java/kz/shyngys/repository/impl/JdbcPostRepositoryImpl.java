package kz.shyngys.repository.impl;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.util.PostMapper;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcPostRepositoryImpl implements PostRepository {

    private static final String SQL_GET_POST_BY_ID = "SELECT p.id, p.content, p.created, p.updated, p.status, " +
            "l.id, l.name, " +
            "w.id, w.first_name, w.last_name " +
            "FROM posts p " +
            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
            "LEFT JOIN labels l ON l.id = pl.label_id " +
            "LEFT JOIN writers w ON p.writer_id = w.id " +
            "WHERE p.id = ?";
    private static final String SQL_GET_ALL_POSTS = "SELECT p.id, p.content, p.created, p.updated, p.status " +
            "FROM posts p WHERE p.status = 'ACTIVE'";
    private static final String SQL_DELETE_POST_BY_ID = "UPDATE posts SET status = 'DELETED' WHERE id = ?";
    private static final String SQL_INSERT_LABEL = "INSERT INTO labels (name) VALUES (?) " +
            "ON DUPLICATE KEY UPDATE name = name";
    private static final String SQL_GET_LABEL_BY_NAME = "SELECT id FROM labels WHERE name = ?";
    private static final String SQL_INSERT_POST_LABEL = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";
    private static final String SQL_INSERT_POST = "INSERT INTO posts (content, status, writer_id) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_POST = "UPDATE posts SET content = ?, status = ?, writer_id = ? WHERE id = ?";
    private static final String SQL_DELETE_POST_LABELS = "DELETE FROM post_labels WHERE post_id = ?";

    @Override
    public Post getById(Long id) {
        try (PreparedStatement ps = DatabaseUtils.getPreparedStatement(SQL_GET_POST_BY_ID)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return PostMapper.mapPostFromResultSet(rs, id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении Post по id: " + id, e);
        }
    }

    @Override
    public List<Post> getAll() {
        try (PreparedStatement ps = DatabaseUtils.getPreparedStatement(SQL_GET_ALL_POSTS);
             ResultSet rs = ps.executeQuery()) {

            List<Post> posts = new ArrayList<>();

            while (rs.next()) {
                posts.add(PostMapper.toPost(rs));
            }

            if (posts.isEmpty()) {
                throw new NotFoundException("В таблице posts нет записей");
            }

            return posts;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех Posts", e);
        }
    }

    @Override
    public Post save(Post post) {
        try {
            Long postId = savePost(post);

            if (!CollectionUtils.isEmpty(post.getLabels())) {
                savePostLabels(postId, post.getLabels());
            }

            DatabaseUtils.commit();
            post.setId(postId);
            return post;
        } catch (Exception e) {
            DatabaseUtils.rollback();
            throw new RuntimeException("Ошибка при сохранении Post", e);
        }
    }

    @Override
    public Post update(Post post) {
        try {
            updatePost(post);

            if (!CollectionUtils.isEmpty(post.getLabels())) {
                deletePostLabels(post.getId());
                savePostLabels(post.getId(), post.getLabels());
            }

            DatabaseUtils.commit();
            return post;
        } catch (Exception e) {
            DatabaseUtils.rollback();
            throw new RuntimeException("Ошибка при обновлении Post с id: " + post.getId(), e);
        }
    }

    @Override
    public void deleteById(Post post) {
        try (PreparedStatement ps = DatabaseUtils.getPreparedStatement(SQL_DELETE_POST_BY_ID)) {
            ps.setLong(1, post.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Post с id " + post.getId() + " не найден для удаления");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении Post с id: " + post.getId(), e);
        }
    }


    private Long savePost(Post post) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_POST)) {
            ps.setString(1, post.getContent());
            ps.setString(2, post.getStatus().name());
            ps.setLong(3, post.getWriter().getId());

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

    private void updatePost(Post post) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_UPDATE_POST)) {
            ps.setString(1, post.getContent());
            ps.setString(2, post.getStatus().name());
            ps.setLong(3, post.getWriter().getId());
            ps.setLong(4, post.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Post с id " + post.getId() + " не найден для обновления");
            }
        }
    }

    private void savePostLabels(Long postId, List<Label> labels) throws SQLException {
        for (Label label : labels) {
            Long labelId = getOrCreateLabel(label.getName());
            label.setId(labelId);
            linkPostLabel(postId, labelId);
        }
    }

    private void deletePostLabels(Long postId) throws SQLException {
        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_DELETE_POST_LABELS)) {
            ps.setLong(1, postId);
            ps.executeUpdate();
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