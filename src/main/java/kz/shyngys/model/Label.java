package kz.shyngys.model;

import lombok.Data;

import java.util.List;

@Data
public class Label {
    private Long id;
    private String name;
    private List<Post> post;
}
