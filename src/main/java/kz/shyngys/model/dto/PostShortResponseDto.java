package kz.shyngys.model.dto;

import kz.shyngys.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostShortResponseDto {
    private Long id;
    private String text;
    private Status status;
}
