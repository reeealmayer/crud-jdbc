package kz.shyngys;

import kz.shyngys.db.DatabaseConnection;
import kz.shyngys.model.Label;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.LabelCreateRequestDto;
import kz.shyngys.model.dto.PostCreateRequestDto;
import kz.shyngys.model.dto.PostShortDto;
import kz.shyngys.model.dto.WriterCreateRequestDto;
import kz.shyngys.model.dto.WriterUpdateRequestDto;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.repository.impl.JdbcLabelRepositoryImpl;
import kz.shyngys.repository.impl.JdbcWriterRepositoryImpl;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.WriterServiceImpl;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
        WriterService writerService = new WriterServiceImpl(writerRepository);
//
//        WriterCreateRequestDto request = new WriterCreateRequestDto();
//        request.setFirstName("new");
//        request.setLastName("new");
//
//        LabelCreateRequestDto label1 = new LabelCreateRequestDto("label1");
//        LabelCreateRequestDto label2 = new LabelCreateRequestDto("label2");
//        LabelCreateRequestDto label3 = new LabelCreateRequestDto("label3");
//
//        PostCreateRequestDto post = new PostCreateRequestDto(Status.ACTIVE, "content", List.of(label1, label2, label3));
//        request.setPosts(List.of(post));
//
//        System.out.println(writerService.create(request));

//        System.out.println(writerService.getById(13L));
//        writerService.deleteById(13L);


//        WriterService writerService = new WriterServiceImpl(writerRepository);
//        writerService.deleteById(7L);
//        LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
//        Label label = new Label();
//        label.setName("new label");
//        Label save = labelRepository.save(label);
//        System.out.println(save);
//        System.out.println(labelRepository.getById(save.getId()));
//        System.out.println(labelRepository.getAll().toString());
//        save.setName("updated label");
//        System.out.println(labelRepository.update(save));
//        labelRepository.deleteById(save);
//        System.out.println(labelRepository.getById(save.getId()));

        PostShortDto post1 = new PostShortDto(null, "content3", Status.ACTIVE);
        PostShortDto post2 = new PostShortDto(null, "content4", Status.ACTIVE);
        List<PostShortDto> posts = List.of(post1, post2);
        WriterUpdateRequestDto writerUpdateRequestDto = new WriterUpdateRequestDto(22L, "newname", "newlastname", posts);
        writerService.update(writerUpdateRequestDto);

        writerService.deleteById(22L);

        DatabaseConnection.closeConnection();
    }
}
