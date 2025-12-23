package kz.shyngys.util;

import kz.shyngys.model.Writer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WriterMapper {
    private WriterMapper() {
    }

    public static Writer toWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("w.id"));
        writer.setFirstName(resultSet.getString("w.first_name"));
        writer.setLastName(resultSet.getString("w.last_name"));
        return writer;
    }
}
