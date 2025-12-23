package kz.shyngys.service;

import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.model.dto.WriterShortDto;

import java.util.List;

public interface WriterService {
    WriterFullDto getById(Long id);

    List<WriterShortDto> getAll();

    WriterFullDto save(WriterFullDto request);

    WriterFullDto update(WriterFullDto request);

    void deleteById(Long id);
}