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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    private final String SQL_GET_WRITER_BY_ID = "select id, first_name, last_name from writers where id = ?";
    private final String SQL_GET_POSTS_BY_WRITER_ID = "select * from posts where writer_id = ?";


    @Override
    public Writer getById(Long id) {
        try (Connection connection = DriverManager.getConnection(DatabaseProperties.URL,
                DatabaseProperties.USERNAME,
                DatabaseProperties.PASSWORD)) {
            Writer writer = getWriterById(connection, id);

            List<Post> posts = getPostsByWriterId(connection, id);

            writer.setPosts(posts);

            return writer;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
        }
    }

    private List<Post> getPostsByWriterId(Connection connection, Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_GET_POSTS_BY_WRITER_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Post> posts = new ArrayList<>();
                while (rs.next()) {
                    posts.add(mapToPost(rs));
                }
                return posts;
            }
        }
    }

    private Writer getWriterById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_GET_WRITER_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToWriter(rs);
                }
                throw new WriterNotFoundException("Writer не найден по id: " + id);
            }
        }
    }

    @Override
    public List<Writer> getAll() {
        return List.of();
    }

    @Override
    public Writer save(Writer writer) {
        return null;
    }

    @Override
    public Writer update(Writer writer) {
        return null;
    }

    @Override
    public void deleteById(Writer writer) {

    }

    private Post mapToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("id"));
        post.setContent(resultSet.getString("content"));
        if (Objects.nonNull(resultSet.getTimestamp("created"))) {
            post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        }
        if (Objects.nonNull(resultSet.getTimestamp("updated"))) {
            post.setCreated(resultSet.getTimestamp("updated").toLocalDateTime());
        }
        post.setStatus(Status.valueOf(resultSet.getString("status")));
        return post;
    }

    private Writer mapToWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("id"));
        writer.setFirstName(resultSet.getString("first_name"));
        writer.setLastName(resultSet.getString("last_name"));
        return writer;
    }
}
