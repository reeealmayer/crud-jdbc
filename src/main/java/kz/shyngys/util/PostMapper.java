package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.dto.LabelCreateRequestDto;
import kz.shyngys.model.dto.PostCreateRequestDto;
import kz.shyngys.model.dto.PostShortDto;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class PostMapper {
    private PostMapper() {
    }

    public static Post toPost(PostCreateRequestDto requestDto) {
        Post post = new Post();
        post.setContent(requestDto.getContent());
        post.setStatus(requestDto.getStatus());
        List<LabelCreateRequestDto> requestDtoLabels = requestDto.getLabels();
        if (!CollectionUtils.isEmpty(requestDtoLabels)) {
            List<Label> labels = requestDtoLabels.stream().map(LabelMapper::toLabel).toList();
            post.setLabels(labels);
        }
        return post;
    }

    public static PostShortDto toPostShortResponseDto(Post post) {
        return new PostShortDto(post.getId(), post.getContent(), post.getStatus());
    }

    public static Post toPost(PostShortDto postShortDto) {
        Post post = new Post();
        post.setId(postShortDto.getId());
        post.setContent(postShortDto.getContent());
        post.setStatus(postShortDto.getStatus());
        return post;
    }
}
