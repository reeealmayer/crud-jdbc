package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.model.dto.PostDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostMapper {
    private PostMapper() {
    }

    public static Post toPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("p.id"));
        post.setContent(resultSet.getString("p.content"));
        Timestamp createdTimestamp = resultSet.getTimestamp("p.created");
        if (Objects.nonNull(createdTimestamp)) {
            post.setCreated(createdTimestamp.toLocalDateTime());
        }
        Timestamp updatedTimestamp = resultSet.getTimestamp("p.updated");
        if (Objects.nonNull(updatedTimestamp)) {
            post.setCreated(updatedTimestamp.toLocalDateTime());
        }
        String statusName = resultSet.getString("p.status");
        if (statusName != null) {
            post.setStatus(Status.valueOf(statusName));
        }
        return post;
    }

    public static Post toPost(PostDto dto) {
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent());

        if (dto.getCreated() != null) {
            post.setCreated(dto.getCreated());
        }

        if (dto.getUpdated() != null) {
            post.setUpdated(dto.getUpdated());
        }

        if (dto.getStatus() != null) {
            post.setStatus(Status.valueOf(dto.getStatus()));
        }

        if (dto.getWriterId() != null) {
            Writer writer = new Writer();
            writer.setId(dto.getWriterId());
            post.setWriter(writer);
        }

        if (dto.getLabels() != null) {
            List<Label> labels = dto.getLabels().stream()
                    .map(LabelMapper::toLabel)
                    .collect(Collectors.toList());
            post.setLabels(labels);
        }

        return post;
    }

    public static PostDto toPostDto(Post post) {
        List<LabelDto> labelDtos = Collections.emptyList();

        if (post.getLabels() != null) {
            labelDtos = post.getLabels().stream()
                    .map(LabelMapper::toLabelDto)
                    .collect(Collectors.toList());
        }

        String statusStr = post.getStatus() != null ? post.getStatus().name() : null;
        Long writerId = post.getWriter() != null ? post.getWriter().getId() : null;

        return new PostDto(
                post.getId(),
                post.getContent(),
                post.getCreated(),
                post.getUpdated(),
                statusStr,
                writerId,
                labelDtos
        );
    }
}
