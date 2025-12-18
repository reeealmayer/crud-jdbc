package kz.shyngys.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Post {
    private Long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Status status;
    private Long writerId;
    private List<Label> labels;

    public Post(String content, LocalDateTime created, LocalDateTime updated, Status status, Long writerId) {
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.status = status;
        this.writerId = writerId;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Post post = (Post) object;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", status=" + status +
                ", writerId=" + writerId +
                ", labels=" + labels +
                '}';
    }
}
