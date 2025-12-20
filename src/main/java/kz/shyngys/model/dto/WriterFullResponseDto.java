package kz.shyngys.model.dto;

import kz.shyngys.model.Post;
import kz.shyngys.model.Writer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriterFullResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;

    public static WriterFullResponseDto mapFromEntity(Writer writer) {
        WriterFullResponseDto writerFullResponseDto = new WriterFullResponseDto();
        writerFullResponseDto.setId(writer.getId());
        writerFullResponseDto.setFirstName(writer.getFirstName());
        writerFullResponseDto.setLastName(writer.getLastName());
        writerFullResponseDto.setPosts(writer.getPosts());
        return writerFullResponseDto;
    }
}
