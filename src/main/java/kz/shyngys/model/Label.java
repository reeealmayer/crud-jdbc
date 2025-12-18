package kz.shyngys.model;

import java.util.Objects;

public class Label {
    private Long id;
    private String name;
    private Long postId;

    public Label(String name, Long postId) {
        this.name = name;
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", postId=" + postId +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Label label = (Label) object;
        return Objects.equals(id, label.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
