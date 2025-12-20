package kz.shyngys.service;

import kz.shyngys.model.dto.WriterCreateRequestDto;
import kz.shyngys.model.dto.WriterFullResponseDto;
import kz.shyngys.model.dto.WriterShortResponseDto;
import kz.shyngys.model.dto.WriterUpdateRequestDto;

import java.util.List;

public interface WriterService {
    WriterFullResponseDto getById(Long id);

    List<WriterShortResponseDto> getAll();

    WriterShortResponseDto create(WriterCreateRequestDto writer);

    WriterShortResponseDto update(WriterUpdateRequestDto writer);

    void deleteById(Long id);
}
