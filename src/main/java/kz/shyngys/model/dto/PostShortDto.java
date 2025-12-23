package kz.shyngys.model.dto;

import kz.shyngys.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostShortDto {
    private Long id;
    private String content;
    private Status status;
}
