package kz.shyngys.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Post {
    private Long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Status status;
    private Writer writer;
    private List<Label> labels;
}
