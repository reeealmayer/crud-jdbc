package kz.shyngys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String status;
    private Long writerId;
    private List<LabelDto> labels;
}