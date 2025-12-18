package kz.shyngys.model;


import lombok.Data;

import java.util.List;

@Data
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
}
