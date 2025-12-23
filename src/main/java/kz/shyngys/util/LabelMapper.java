package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.dto.LabelCreateRequestDto;

public class LabelMapper {
    private LabelMapper() {
    }

    public static Label toLabel(LabelCreateRequestDto requestDto) {
        Label label = new Label();
        label.setName(requestDto.getName());
        return label;
    }
}
