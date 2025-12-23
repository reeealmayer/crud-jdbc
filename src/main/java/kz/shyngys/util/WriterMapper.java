package kz.shyngys.util;

import kz.shyngys.model.Post;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.PostCreateRequestDto;
import kz.shyngys.model.dto.PostShortDto;
import kz.shyngys.model.dto.WriterCreateRequestDto;
import kz.shyngys.model.dto.WriterFullResponseDto;
import kz.shyngys.model.dto.WriterUpdateRequestDto;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WriterMapper {
    private WriterMapper() {
    }

    public static Writer toWriter(WriterCreateRequestDto requestDto) {
        Writer writer = new Writer();
        writer.setFirstName(requestDto.getFirstName());
        writer.setLastName(requestDto.getLastName());
        List<PostCreateRequestDto> requestDtoPosts = requestDto.getPosts();
        if (!CollectionUtils.isEmpty(requestDtoPosts)) {
            List<Post> posts = requestDtoPosts.stream().map(PostMapper::toPost).toList();
            writer.setPosts(posts);
        }
        return writer;
    }

    public static Writer toWriter(WriterUpdateRequestDto requestDto) {
        Writer writer = new Writer();
        writer.setId(requestDto.getId());
        writer.setFirstName(requestDto.getFirstName());
        writer.setLastName(requestDto.getLastName());
        List<PostShortDto> postShortDtos = requestDto.getPosts();
        if (!CollectionUtils.isEmpty(postShortDtos)) {
            List<Post> posts = postShortDtos.stream().map(PostMapper::toPost).toList();
            writer.setPosts(posts);
        }
        return writer;
    }

    public static WriterFullResponseDto toWriterFullResponseDto(Writer writer) {
        WriterFullResponseDto writerFullResponseDto = new WriterFullResponseDto();
        writerFullResponseDto.setId(writer.getId());
        writerFullResponseDto.setFirstName(writer.getFirstName());
        writerFullResponseDto.setLastName(writer.getLastName());
        List<Post> posts = writer.getPosts();
        if (!CollectionUtils.isEmpty(posts)) {
            List<PostShortDto> postShortDtos = posts.stream().map(PostMapper::toPostShortResponseDto).toList();
            writerFullResponseDto.setPosts(postShortDtos);
        }
        return writerFullResponseDto;
    }

    public static Writer toWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("w.id"));
        writer.setFirstName(resultSet.getString("w.first_name"));
        writer.setLastName(resultSet.getString("w.last_name"));
        return writer;
    }
}
