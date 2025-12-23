package kz.shyngys.repository.impl;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.util.LabelMapper;
import kz.shyngys.util.PostMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcLabelRepositoryImpl implements LabelRepository {

    private final String SQL_GET_LABEL_BY_ID = "select l.id, l.name, " +
            " p.id, p.content, p.created, p.updated, p.status " +
            " from labels l " +
            " left join post_labels  pl " +
            " on l.id = pl.label_id " +
            " left join posts p " +
            " on p.id = pl.post_id " +
            " where l.id = ? and p.status = 'ACTIVE'";
    private final String SQL_GET_ALL_LABELS = "select l.id, l.name from labels l";
    private final String SQL_INSERT_LABEL = "insert into labels (name) values (?)";
    private final String SQL_UPDATE_LABEL_BY_ID = "update labels set name = ? where id = ?";
    private final String SQl_DELETE_LABEL_BY_ID = "delete from labels where id = ?";

    @Override
    public Label getById(Long id) {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LABEL_BY_ID)
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Label label = null;
                List<Post> posts = new ArrayList<>();
                while (resultSet.next()) {
                    if (label == null) {
                        label = LabelMapper.toLabel(resultSet);
                    }

                    Post post = PostMapper.toPost(resultSet);
                    posts.add(post);
                }
                if (label == null) {
                    throw new NotFoundException("Label не найден с id " + id);
                }
                label.setPost(posts);
                return label;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    @Override
    public List<Label> getAll() {
        Connection connection = DatabaseUtils.getConnection();
        try (
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_LABELS)) {
                List<Label> labels = new ArrayList<>();
                while (resultSet.next()) {
                    labels.add(LabelMapper.toLabel(resultSet));
                }
                if (labels.isEmpty()) {
                    throw new NotFoundException("В таблице labels нет записей");
                }
                return labels;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    @Override
    public Label save(Label label) {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement
                        = connection.prepareStatement(SQL_INSERT_LABEL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, label.getName());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Создание Label не удалось, ни одна строка не добавлена");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    label.setId(generatedKeys.getLong(1));
                    return label;
                }
                throw new SQLException("Создание Label не удалось, id не получен");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    @Override
    public Label update(Label label) {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_LABEL_BY_ID)
        ) {
            preparedStatement.setString(1, label.getName());
            preparedStatement.setLong(2, label.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Обновление Label не удалось, ни одна строка не изменилась");
            }
            return label;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }

    @Override
    public void deleteById(Label label) {
        Connection connection = DatabaseUtils.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(SQl_DELETE_LABEL_BY_ID)
        ) {
            preparedStatement.setLong(1, label.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Удаление Label не удалось, ни одна строка не изменилась");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL: " + e);
        }
    }
}
