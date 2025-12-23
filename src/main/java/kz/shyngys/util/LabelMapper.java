package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.dto.LabelDto;

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

    public static Label toLabel(LabelDto dto) {
        Label label = new Label();
        label.setId(dto.getId());
        label.setName(dto.getName());
        return label;
    }

    public static LabelDto toLabelDto(Label label) {
        return new LabelDto(
                label.getId(),
                label.getName()
        );
    }
}
