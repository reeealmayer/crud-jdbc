package kz.shyngys.controller;

import kz.shyngys.model.dto.PostDto;
import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.model.dto.WriterShortDto;
import kz.shyngys.service.WriterService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WriterController {
    private final WriterService writerService;

    public List<WriterShortDto> getAll() {
        return writerService.getAll();
    }

    public WriterFullDto getById(Long id) {
        return writerService.getById(id);
    }

    public WriterFullDto create(String firstName, String lastName, List<PostDto> posts) {
        WriterFullDto writerFullDto = new WriterFullDto();
        writerFullDto.setFirstName(firstName);
        writerFullDto.setLastName(lastName);
        writerFullDto.setPosts(posts);
        return writerService.save(writerFullDto);
    }

    public WriterFullDto update(Long id, String firstName, String lastName, List<PostDto> posts) {
        WriterFullDto writerFullDto = new WriterFullDto();
        writerFullDto.setId(id);
        writerFullDto.setFirstName(firstName);
        writerFullDto.setLastName(lastName);
        writerFullDto.setPosts(posts);
        return writerService.update(writerFullDto);
    }

    public void delete(Long id) {
        writerService.deleteById(id);
    }
}
