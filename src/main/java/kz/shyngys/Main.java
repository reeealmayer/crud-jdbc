package kz.shyngys;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.JdbcPostRepositoryImpl;
import kz.shyngys.repository.impl.JdbcWriterRepositoryImpl;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.WriterServiceImpl;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        PostRepository postRepository = new JdbcPostRepositoryImpl();
        Writer writer = new Writer();
        writer.setId(23L);

        Label label1 = new Label();
        label1.setName("Игры");

        Label label2 = new Label();
        label2.setName("Космос");


        Post post = new Post();
        post.setContent("Абсолютно новый пост");
        post.setWriter(writer);
        post.setStatus(Status.UNDER_REVIEW);
        post.setLabels(List.of(label1, label2));

        System.out.println(postRepository.save(post));

        DatabaseUtils.closeConnection();
    }
}
