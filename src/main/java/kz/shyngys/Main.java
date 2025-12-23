package kz.shyngys;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.repository.impl.JdbcLabelRepositoryImpl;
import kz.shyngys.repository.impl.JdbcWriterRepositoryImpl;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.WriterServiceImpl;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
        WriterService writerService = new WriterServiceImpl(new JdbcWriterRepositoryImpl());
        System.out.println(writerService.getAll().toString());
        WriterFullDto writerFullDto = writerService.getById(23L);
        System.out.println(writerFullDto);
        writerFullDto.setFirstName("Marina");
        writerFullDto.setLastName("Piskova");
        PostDto postDto = new PostDto();
        postDto.setId(44L);
        postDto.setContent("new content");
        postDto.setStatus("ACTIVE");
        postDto.setWriterId(writerFullDto.getId());
        PostDto postDto1 = new PostDto();
        postDto1.setContent("ASDASDDSA");
        postDto1.setStatus("ACTIVE");
        writerFullDto.setPosts(List.of(postDto, postDto1));
        System.out.println(writerService.update(writerFullDto));

        DatabaseUtils.closeConnection();
    }
}
