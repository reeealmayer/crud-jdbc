package kz.shyngys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriterCreateRequestDto {
    private String firstName;
    private String lastName;
    private List<PostCreateRequestDto> posts;
}
