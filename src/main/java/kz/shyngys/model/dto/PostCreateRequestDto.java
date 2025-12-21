package kz.shyngys.model.dto;

import kz.shyngys.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {
    private Status status;
    private String content;
    private List<LabelCreateRequestDto> labels;
}
