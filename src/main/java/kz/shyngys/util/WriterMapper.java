package kz.shyngys.util;

import kz.shyngys.model.Post;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.model.dto.WriterShortDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WriterMapper {
    private WriterMapper() {
    }

    public static Writer toWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("w.id"));
        writer.setFirstName(resultSet.getString("w.first_name"));
        writer.setLastName(resultSet.getString("w.last_name"));
        return writer;
    }

    public static Writer toWriter(WriterShortDto dto) {
        Writer writer = new Writer();
        writer.setId(dto.getId());
        writer.setFirstName(dto.getFirstName());
        writer.setLastName(dto.getLastName());
        return writer;
    }

    public static Writer toWriter(WriterFullDto dto) {
        Writer writer = new Writer();
        writer.setId(dto.getId());
        writer.setFirstName(dto.getFirstName());
        writer.setLastName(dto.getLastName());

        if (dto.getPosts() != null) {
            List<Post> posts = dto.getPosts().stream()
                    .map(postDto -> {
                        Post post = PostMapper.toPost(postDto);
                        post.setWriter(writer); // Устанавливаем связь
                        return post;
                    })
                    .collect(Collectors.toList());
            writer.setPosts(posts);
        }

        return writer;
    }

    public static WriterShortDto toWriterShortDto(Writer writer) {
        return new WriterShortDto(
                writer.getId(),
                writer.getFirstName(),
                writer.getLastName()
        );
    }

    public static WriterFullDto toWriterFullDto(Writer writer) {
        List<PostDto> postDtos = Collections.emptyList();

        if (writer.getPosts() != null) {
            postDtos = writer.getPosts().stream()
                    .map(PostMapper::toPostDto)
                    .collect(Collectors.toList());
        }

        return new WriterFullDto(
                writer.getId(),
                writer.getFirstName(),
                writer.getLastName(),
                postDtos
        );
    }
}
