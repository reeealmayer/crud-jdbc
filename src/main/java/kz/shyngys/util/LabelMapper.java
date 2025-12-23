package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.dto.LabelCreateRequestDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabelMapper {
    private LabelMapper() {
    }

    public static Label toLabel(LabelCreateRequestDto requestDto) {
        Label label = new Label();
        label.setName(requestDto.getName());
        return label;
    }

    public static Label toLabel(ResultSet resultSet) throws SQLException {
        Label label = new Label();
        label.setId(resultSet.getLong("l.id"));
        label.setName(resultSet.getString("l.name"));
        return label;
    }
}
