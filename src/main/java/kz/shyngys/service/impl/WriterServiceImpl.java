package kz.shyngys.service.impl;

import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.WriterCreateRequestDto;
import kz.shyngys.model.dto.WriterFullResponseDto;
import kz.shyngys.model.dto.WriterShortResponseDto;
import kz.shyngys.model.dto.WriterUpdateRequestDto;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.service.WriterService;
import kz.shyngys.util.Mapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WriterServiceImpl implements WriterService {

    private final WriterRepository writerRepository;

    @Override
    public WriterFullResponseDto getById(Long id) {
        Writer writer = writerRepository.getById(id);
        return Mapper.toWriterFullResponseDto(writer);
    }

    @Override
    public List<WriterShortResponseDto> getAll() {
        List<Writer> all = writerRepository.getAll();
        return all.stream()
                .map(a -> new WriterShortResponseDto(a.getId(), a.getFirstName(), a.getLastName()))
                .toList();
    }

    @Override
    public WriterFullResponseDto create(WriterCreateRequestDto requestDto) {
        Writer writer = Mapper.toWriter(requestDto);
        Writer savedWriter = writerRepository.save(writer);
        return Mapper.toWriterFullResponseDto(savedWriter);
    }

    @Override
    public WriterShortResponseDto update(WriterUpdateRequestDto requestDto) {
        Writer writer = new Writer();
        writer.setId(requestDto.getId());
        writer.setFirstName(requestDto.getFirstName());
        writer.setLastName(requestDto.getLastName());
        Writer updatedWriter = writerRepository.update(writer);
        return WriterShortResponseDto.mapFromEntity(updatedWriter);
    }

    @Override
    public void deleteById(Long id) {
        Writer writer = new Writer();
        writer.setId(id);
        writerRepository.deleteById(writer);
    }
}
