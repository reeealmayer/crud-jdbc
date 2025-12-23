package kz.shyngys.service.impl;

import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.model.dto.WriterShortDto;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.service.WriterService;
import kz.shyngys.util.WriterMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WriterServiceImpl implements WriterService {
    private final WriterRepository writerRepository;

    @Override
    public WriterFullDto getById(Long id) {
        Writer writer = writerRepository.getById(id);
        return WriterMapper.toWriterFullDto(writer);
    }

    @Override
    public List<WriterShortDto> getAll() {
        return writerRepository.getAll()
                .stream()
                .map(WriterMapper::toWriterShortDto)
                .toList();
    }

    @Override
    public WriterFullDto save(WriterFullDto request) {
        Writer writer = WriterMapper.toWriter(request);
        writer = writerRepository.save(writer);
        return WriterMapper.toWriterFullDto(writer);
    }

    @Override
    public WriterFullDto update(WriterFullDto request) {
        Writer writer = WriterMapper.toWriter(request);
        writer = writerRepository.update(writer);
        return WriterMapper.toWriterFullDto(writer);
    }

    @Override
    public void delete(Long id) {
        Writer writer = writerRepository.getById(id);
        writerRepository.deleteById(writer);
    }
}