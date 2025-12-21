package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.LabelCreateRequestDto;
import kz.shyngys.model.dto.PostCreateRequestDto;
import kz.shyngys.model.dto.PostShortResponseDto;
import kz.shyngys.model.dto.WriterCreateRequestDto;
import kz.shyngys.model.dto.WriterFullResponseDto;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class Mapper {
    private Mapper() {
    }

    public static Writer toWriter(WriterCreateRequestDto requestDto) {
        Writer writer = new Writer();
        writer.setFirstName(requestDto.getFirstName());
        writer.setLastName(requestDto.getLastName());
        List<PostCreateRequestDto> requestDtoPosts = requestDto.getPosts();
        if (!CollectionUtils.isEmpty(requestDtoPosts)) {
            List<Post> posts = requestDtoPosts.stream().map(Mapper::toPost).toList();
            writer.setPosts(posts);
        }
        return writer;
    }

    public static Post toPost(PostCreateRequestDto requestDto) {
        Post post = new Post();
        post.setContent(requestDto.getContent());
        post.setStatus(requestDto.getStatus());
        List<LabelCreateRequestDto> requestDtoLabels = requestDto.getLabels();
        if (!CollectionUtils.isEmpty(requestDtoLabels)) {
            List<Label> labels = requestDtoLabels.stream().map(Mapper::toLabel).toList();
            post.setLabels(labels);
        }
        return post;
    }

    public static Label toLabel(LabelCreateRequestDto requestDto) {
        Label label = new Label();
        label.setName(requestDto.getName());
        return label;
    }

    public static WriterFullResponseDto toWriterFullResponseDto(Writer writer) {
        WriterFullResponseDto writerFullResponseDto = new WriterFullResponseDto();
        writerFullResponseDto.setId(writer.getId());
        writerFullResponseDto.setFirstName(writer.getFirstName());
        writerFullResponseDto.setLastName(writer.getLastName());
        List<Post> posts = writer.getPosts();
        if (!CollectionUtils.isEmpty(posts)) {
            List<PostShortResponseDto> postShortResponseDtos = posts.stream().map(Mapper::toPostShortResponseDto).toList();
            writerFullResponseDto.setPosts(postShortResponseDtos);
        }
        return writerFullResponseDto;
    }

    public static PostShortResponseDto toPostShortResponseDto(Post post) {
        return new PostShortResponseDto(post.getId(), post.getContent(), post.getStatus());
    }
}
