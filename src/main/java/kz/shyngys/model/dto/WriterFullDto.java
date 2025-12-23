package kz.shyngys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriterFullDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<PostDto> posts;
}