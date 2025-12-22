package kz.shyngys.repository.impl;

import kz.shyngys.model.Post;
import kz.shyngys.repository.PostRepository;

import java.util.List;

public class JdbcPostRepositoryImpl implements PostRepository {
    @Override
    public Post getById(Long aLong) {
        return null;
    }

    @Override
    public List<Post> getAll() {
        return List.of();
    }

    @Override
    public Post save(Post post) {
        return null;
    }

    @Override
    public Post update(Post post) {
        return null;
    }

    @Override
    public void deleteById(Post post) {

    }
}
