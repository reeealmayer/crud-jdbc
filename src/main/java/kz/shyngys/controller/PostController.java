package kz.shyngys.controller;

import kz.shyngys.model.Status;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    public List<PostDto> getAll() {
        return postService.getAll()
                .stream()
                .filter(post -> post.getStatus().equals(Status.ACTIVE))
                .toList();
    }

    public PostDto getById(Long id) {
        return postService.getById(id);
    }

    public PostDto create(String content, Long writerId, List<LabelDto> labels) {
        PostDto postDto = PostDto.builder()
                .content(content)
                .writerId(writerId)
                .status(Status.ACTIVE)
                .labels(labels)
                .build();
        return postService.save(postDto);
    }

    public PostDto update(Long id, Long writerId, String content, List<LabelDto> labels) {
        PostDto postDto = PostDto.builder()
                .id(id)
                .content(content)
                .writerId(writerId)
                .status(Status.ACTIVE)
                .labels(labels)
                .build();
        return postService.update(postDto);
    }

    public void delete(Long id) {
        postService.deleteById(id);
    }

    public PostDto addLabelsToPost(Long id, List<LabelDto> labels) {
        PostDto postDto = PostDto.builder()
                .id(id)
                .status(Status.ACTIVE)
                .labels(labels)
                .build();
        return postService.update(postDto);
    }
}
