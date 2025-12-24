package kz.shyngys.service;

import kz.shyngys.model.dto.LabelDto;

import java.util.List;

public interface LabelService {
    LabelDto getById(Long id);

    List<LabelDto> getAll();

    LabelDto save(LabelDto request);

    LabelDto update(LabelDto request);

    void deleteById(Long id);
}