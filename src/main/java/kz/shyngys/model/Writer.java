package kz.shyngys.model;


import java.util.List;
import java.util.Objects;

public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;

    public Writer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Writer writer = (Writer) object;
        return Objects.equals(id, writer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Writer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", posts=" + posts +
                '}';
    }
}
