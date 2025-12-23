package kz.shyngys.util;

import kz.shyngys.model.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabelMapper {
    private LabelMapper() {
    }

    public static Label toLabel(ResultSet resultSet) throws SQLException {
        Label label = new Label();
        label.setId(resultSet.getLong("l.id"));
        label.setName(resultSet.getString("l.name"));
        return label;
    }
}
