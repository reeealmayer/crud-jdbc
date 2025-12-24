package kz.shyngys.service.impl;

import kz.shyngys.model.Post;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.service.PostService;
import kz.shyngys.util.PostMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostDto getById(Long id) {
        Post post = postRepository.getById(id);
        return PostMapper.toPostDto(post);
    }

    @Override
    public List<PostDto> getAll() {
        List<Post> posts = postRepository.getAll();
        return posts.stream().map(PostMapper::toPostDto).toList();
    }

    @Override
    public PostDto save(PostDto request) {
        if (request.getWriterId() == null) {
            Post post = postRepository.getById(request.getId());
            request.setWriterId(post.getWriter().getId());
        }
        Post saved = postRepository.save(PostMapper.toPost(request));
        return PostMapper.toPostDto(saved);
    }

    @Override
    public PostDto update(PostDto request) {
        if (request.getWriterId() == null) {
            Post post = postRepository.getById(request.getId());
            request.setWriterId(post.getWriter().getId());
        }
        Post updated = postRepository.update(PostMapper.toPost(request));
        return PostMapper.toPostDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Post post = new Post();
        post.setId(id);
        postRepository.deleteById(post);
    }
}
