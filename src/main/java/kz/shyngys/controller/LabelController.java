package kz.shyngys.controller;

import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.service.LabelService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    public List<LabelDto> getAll() {
        return labelService.getAll()
                .stream()
                .toList();
    }

    public LabelDto getById(Long id) {
        return labelService.getById(id);
    }

    public LabelDto create(String name) {
        LabelDto label = LabelDto.builder()
                .name(name)
                .build();
        return labelService.save(label);
    }

    public LabelDto update(Long id, String name) {
        LabelDto label = LabelDto.builder()
                .id(id)
                .name(name)
                .build();
        return labelService.update(label);
    }

    public void delete(Long id) {
        labelService.deleteById(id);
    }
}
