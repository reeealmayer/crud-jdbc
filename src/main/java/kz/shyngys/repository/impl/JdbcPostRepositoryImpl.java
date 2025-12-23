package kz.shyngys.repository.impl;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.util.LabelMapper;
import kz.shyngys.util.PostMapper;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcPostRepositoryImpl implements PostRepository {
    private final String SQL_GET_POST_BY_ID = " select p.id, p.content, p.created, p.updated, p.status, " +
            " l.id, l.name " +
            " from posts p " +
            " left join post_labels pl " +
            " on p.id = pl.post_id " +
            " left join labels l " +
            " on l.id = pl.label_id " +
            " where p.id = ?";
    private final String SQL_GET_ALL_POSTS = "select p.id, p.content, p.created, p.updated, p.status from posts p where p.status = 'ACTIVE'";
    private final String SQL_DELETE_POST_BY_ID = "update posts set status = 'DELETED' where id = ?";
    private final String SQL_INSERT_LABEL = "insert into labels (name) values (?) on duplicate key update name = name";
    private final String SQL_GET_LABEL_BY_NAME = "select id from labels where name = ?";
    private final String SQL_INSERT_POST_LABEL = "insert into post_labels (post_id, label_id) values (?, ?)";
    private final String SQL_INSERT_POST = "insert into posts (content, status, writer_id) values (?, ?, ?)";

    @Override
    public Post getById(Long id) {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_POST_BY_ID)
        ) {
            preparedStatement.setLong(1, id);
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                Post post = null;
                List<Label> labels = new ArrayList<>();

                while (resultSet.next()) {
                    if (post == null) {
                        post = PostMapper.toPost(resultSet);
                    }

                    Label label = LabelMapper.toLabel(resultSet);
                    if (label.getId() != 0L) {
                        labels.add(label);
                    }
                }
                if (post == null) {
                    throw new NotFoundException("Post не найден с id: " + id);
                }
                post.setLabels(labels);
                return post;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    @Override
    public List<Post> getAll() {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_POSTS)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Post> posts = new ArrayList<>();
                while (resultSet.next()) {
                    Post post = PostMapper.toPost(resultSet);
                    posts.add(post);
                }
                if (posts.isEmpty()) {
                    throw new SQLException("В таблице posts нет записей");
                }
                return posts;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    @Override
    public Post save(Post post) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try {
                Long postId = savePost(connection, post.getWriter().getId(), post);

                if (!CollectionUtils.isEmpty(post.getLabels())) {
                    savePostLabels(connection, postId, post.getLabels());
                }

                connection.commit();
                connection.setAutoCommit(true);
                post.setId(postId);
                return post;
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e.getMessage());
        }
    }

    @Override
    public Post update(Post post) {
        return null;
    }

    @Override
    public void deleteById(Post post) {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_POST_BY_ID)
        ) {
            preparedStatement.setLong(1, post.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Удаление post не удалось, ни одна строка не затронута");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    private void savePostLabels(Connection connection, Long postId, List<Label> labels) throws SQLException {
        for (Label label : labels) {
            Long labelId = getOrCreateLabel(connection, label.getName());
            label.setId(labelId);
            linkPostLabel(connection, postId, labelId);
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
}
