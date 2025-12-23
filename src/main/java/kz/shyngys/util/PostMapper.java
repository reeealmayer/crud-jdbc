package kz.shyngys.util;

import kz.shyngys.model.Post;
import kz.shyngys.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

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
}
