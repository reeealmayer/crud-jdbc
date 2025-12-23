package kz.shyngys.util;

import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.LabelCreateRequestDto;
import kz.shyngys.model.dto.PostCreateRequestDto;
import kz.shyngys.model.dto.PostShortDto;
import kz.shyngys.model.dto.WriterCreateRequestDto;
import kz.shyngys.model.dto.WriterFullResponseDto;
import kz.shyngys.model.dto.WriterUpdateRequestDto;
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
            List<PostShortDto> postShortDtos = posts.stream().map(Mapper::toPostShortResponseDto).toList();
            writerFullResponseDto.setPosts(postShortDtos);
        }
        return writerFullResponseDto;
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

    public static Writer toWriter(WriterUpdateRequestDto requestDto) {
        Writer writer = new Writer();
        writer.setId(requestDto.getId());
        writer.setFirstName(requestDto.getFirstName());
        writer.setLastName(requestDto.getLastName());
        List<PostShortDto> postShortDtos = requestDto.getPosts();
        if (!CollectionUtils.isEmpty(postShortDtos)) {
            List<Post> posts = postShortDtos.stream().map(Mapper::toPost).toList();
            writer.setPosts(posts);
        }
        return writer;
    }
}
