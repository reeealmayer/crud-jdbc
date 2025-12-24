package kz.shyngys.service;

import kz.shyngys.model.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto getById(Long id);

    List<PostDto> getAll();

    PostDto save(PostDto request);

    PostDto update(PostDto request);

    void deleteById(Long id);
}