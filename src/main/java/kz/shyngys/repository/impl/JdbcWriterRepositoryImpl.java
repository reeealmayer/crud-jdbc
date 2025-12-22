package kz.shyngys.repository.impl;

import kz.shyngys.db.DatabaseConnection;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;
import kz.shyngys.repository.WriterRepository;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcWriterRepositoryImpl implements WriterRepository {


    private final String SQL_GET_WRITER_WITH_POSTS_BY_ID = "select w.id, w.first_name, w.last_name, p.id, p.content, p.created, p.updated, p.status " +
            " from writers w " +
            " left join posts p " +
            " on w.id = p.writer_id " +
            " where w.id = ? ";
    private final String SQL_GET_ALL_WRITERS = "select w.id, w.first_name, w.last_name from writers w";
    private final String SQL_INSERT_WRITER = "insert into writers (first_name, last_name) values (?, ?)";
    private final String SQL_UPDATE_WRITER_BY_ID = "update writers set first_name = ?, last_name = ? where id = ?";
    private final String SQL_DELETE_WRITER_BY_ID = "delete from writers where id = ?";
    private final String SQL_INSERT_POST = "insert into posts (content, status, writer_id) values (?, ?, ?)";
    private final String SQL_INSERT_LABEL = "insert into labels (name) values (?) on duplicate key update name = name";
    private final String SQL_GET_LABEL_BY_NAME = "select id from labels where name = ?";
    private final String SQL_INSERT_POST_LABEL = "insert into post_labels (post_id, label_id) values (?, ?)";

    @Override
    public Writer getById(Long id) {
        Connection connection = DatabaseConnection.getInstance();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_WRITER_WITH_POSTS_BY_ID)
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Writer writer = null;
                List<Post> posts = new ArrayList<>();

                while (resultSet.next()) {
                    if (writer == null) {
                        writer = mapToWriter(resultSet);
                    }

                    Post post = mapToPost(resultSet);
                    if (post.getId() != 0L) {
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
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public List<Writer> getAll() {
        Connection connection = DatabaseConnection.getInstance();
        try (
                Statement statement = connection.createStatement()
        ) {

            try (ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_WRITERS)) {
                List<Writer> writers = new ArrayList<>();
                while (resultSet.next()) {
                    Writer writer = mapToWriter(resultSet);
                    if (writer != null) {
                        writers.add(writer);
                    }
                }
                if (writers.isEmpty()) {
                    throw new NotFoundException("В таблице writers нет записей");
                }
                return writers;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public Writer save(Writer writer) {
        Connection connection = DatabaseConnection.getInstance();
        try {
            connection.setAutoCommit(false);
            try {
                Long writerId = saveWriter(connection, writer);
                writer.setId(writerId);

                if (!CollectionUtils.isEmpty(writer.getPosts())) {
                    for (Post post : writer.getPosts()) {
                        Long postId = savePost(connection, writerId, post);
                        post.setId(postId);

                        if (!CollectionUtils.isEmpty(post.getLabels())) {
                            savePostLabels(connection, postId, post.getLabels());
                        }

                    }
                }
                connection.commit();
                connection.setAutoCommit(true);
                return writer;
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public Writer update(Writer writer) {
        Connection connection = DatabaseConnection.getInstance();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_WRITER_BY_ID)
        ) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.setLong(3, writer.getId());

            try {
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Обновление writer не удалось, ни одна строка не затронута");
                }

                List<Post> posts = writer.getPosts();
                if (!CollectionUtils.isEmpty(posts)) {
                    writer.setPosts(savePosts(connection, writer.getId(), posts));
                }
                connection.commit();
                return writer;
            } catch (SQLException e) {
                connection.rollback();

                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Writer writer) {
        Connection connection = DatabaseConnection.getInstance();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_WRITER_BY_ID)
        ) {
            preparedStatement.setLong(1, writer.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Удаление writer не удалось, ни одна строка не затронута");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    private Long saveWriter(Connection connection, Writer writer) throws SQLException {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_INSERT_WRITER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Создание writer не удалось, ни одна строка не добавлена");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Создание writer не удалось, id не получен");
                }
            }
        }
    }

    private Long savePost(Connection connection, Long writerId, Post post) throws SQLException {
        try (
                PreparedStatement preparedStatement
                        = connection.prepareStatement(SQL_INSERT_POST, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setString(2, post.getStatus().name());
            preparedStatement.setLong(3, writerId);

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Создание post не удалось, id не получен");
                }
            }
        }
    }

    private List<Post> savePosts(Connection connection, Long writerId, List<Post> posts) throws SQLException {
        try (
                PreparedStatement preparedStatement
                        = connection.prepareStatement(SQL_INSERT_POST, Statement.RETURN_GENERATED_KEYS)
        ) {
            for (Post post : posts) {
                preparedStatement.setString(1, post.getContent());
                preparedStatement.setString(2, post.getStatus().name());
                preparedStatement.setLong(3, writerId);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                int index = 0;
                while (generatedKeys.next() && index < posts.size()) {
                    posts.get(index).setId(generatedKeys.getLong(1));
                    index++;
                }
            }
            return posts;
        }
    }

    private void savePostLabels(Connection connection, Long postId, List<Label> labels) throws SQLException {
        for (Label label : labels) {
            Long labelId = getOrCreateLabel(connection, label.getName());
            label.setId(labelId);
            linkPostLabel(connection, postId, labelId);
        }
    }

    private Long getOrCreateLabel(Connection connection, String name) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LABEL_BY_NAME)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(
                SQL_INSERT_LABEL,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("Не удалось получить ID label");
            }
        }
    }

    private void linkPostLabel(Connection connection, Long postId, Long labelId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_POST_LABEL)) {
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            preparedStatement.executeUpdate();
        }
    }

    private Post mapToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("p.id"));
        post.setContent(resultSet.getString("p.content"));
        Timestamp createdTimestamp = resultSet.getTimestamp("p.created");
        if (Objects.nonNull(createdTimestamp)) {
            post.setCreated(createdTimestamp.toLocalDateTime());
        }
        Timestamp updatedTimestamp = resultSet.getTimestamp("p.updated");
        if (Objects.nonNull(updatedTimestamp)) {
            post.setCreated(updatedTimestamp.toLocalDateTime());
        }
        String statusName = resultSet.getString("p.status");
        if (statusName != null) {
            post.setStatus(Status.valueOf(statusName));
        }
        return post;
    }

    private Writer mapToWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("w.id"));
        writer.setFirstName(resultSet.getString("w.first_name"));
        writer.setLastName(resultSet.getString("w.last_name"));
        return writer;
    }
}
