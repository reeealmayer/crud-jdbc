package kz.shyngys.repository.impl;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.util.LabelMapper;
import kz.shyngys.util.PostMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
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
}
