package kz.shyngys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriterUpdateRequestDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<PostShortDto> posts;
}
