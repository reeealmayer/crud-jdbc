package kz.shyngys.repository;

import kz.shyngys.db.DatabaseProperties;
import kz.shyngys.exception.WriterNotFoundException;
import kz.shyngys.model.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    private final String SQL_GET_BY_ID = "select id, first_name, last_name from writers where id = ?";


    @Override
    public Writer getById(Long id) {
        try (Connection connection = DriverManager.getConnection(DatabaseProperties.URL, DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Writer writer = mapToWriter(resultSet);
                    return writer;
                }
                throw new WriterNotFoundException("Writer не найден по id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL " + e.getMessage());
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

    private Writer mapToWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("id"));
        writer.setFirstName(resultSet.getString("first_name"));
        writer.setLastName(resultSet.getString("last_name"));
        return writer;
    }
}
