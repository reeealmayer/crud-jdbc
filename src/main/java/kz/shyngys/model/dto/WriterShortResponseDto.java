package kz.shyngys.model.dto;

import kz.shyngys.model.Writer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriterShortResponseDto {
    private Long id;
    private String firstName;
    private String lastName;

    public static WriterShortResponseDto mapFromEntity(Writer writer) {
        return new WriterShortResponseDto(writer.getId(), writer.getFirstName(), writer.getLastName());
    }
}
