package kz.shyngys.repository.impl;

import kz.shyngys.model.Post;
import kz.shyngys.repository.PostRepository;

import java.util.List;

public class JdbcPostRepositoryImpl implements PostRepository {
    private final String SQL_GET_POST_BY_ID = " select p.id, p.content, p.created, p.updated, p.status, " +
            " l.id, l.name " +
            " from posts p " +
            " left join post_labels pl " +
            " on p.id = pl.post_id " +
            " left join labels l " +
            " on l.id = pl.label_id " +
            " where p.id = ?";

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
