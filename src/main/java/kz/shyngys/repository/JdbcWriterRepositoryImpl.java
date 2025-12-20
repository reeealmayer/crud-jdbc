package kz.shyngys.repository;

import kz.shyngys.db.DatabaseProperties;
import kz.shyngys.exception.WriterNotFoundException;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
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

    @Override
    public Writer getById(Long id) {
        try (Connection connection = DriverManager.getConnection(DatabaseProperties.URL,
                DatabaseProperties.USERNAME,
                DatabaseProperties.PASSWORD);
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
                    throw new WriterNotFoundException("Writer не найден с id: " + id);
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
        try (
                Connection connection = DriverManager.getConnection(DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD);
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
                    throw new WriterNotFoundException("В таблице writers нет записей");
                }
                return writers;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public Writer save(Writer writer) {
        try (
                Connection connection = DriverManager.getConnection(DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_WRITER, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Создание writer не удалось, ни одна строка не добавлена");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    writer.setId(generatedKeys.getLong(1));
                    return writer;
                } else {
                    throw new SQLException("Создание writer не удалось, id не получен");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public Writer update(Writer writer) {
        try (
                Connection connection = DriverManager.getConnection(DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_WRITER_BY_ID)
        ) {
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.setLong(3, writer.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Обновление writer не удалось, ни одна строка не затронута");
            }
            return writer;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Writer writer) {
        try (
                Connection connection = DriverManager.getConnection(DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD);
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
