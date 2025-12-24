package kz.shyngys.service.impl;

import kz.shyngys.model.Label;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.service.LabelService;
import kz.shyngys.util.LabelMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public LabelDto getById(Long id) {
        Label label = labelRepository.getById(id);
        return LabelMapper.toLabelDto(label);
    }

    @Override
    public List<LabelDto> getAll() {
        List<Label> labels = labelRepository.getAll();
        return labels.stream().map(LabelMapper::toLabelDto).toList();
    }

    @Override
    public LabelDto save(LabelDto request) {
        Label label = LabelMapper.toLabel(request);
        Label saved = labelRepository.save(label);
        return LabelMapper.toLabelDto(saved);
    }

    @Override
    public LabelDto update(LabelDto request) {
        Label label = LabelMapper.toLabel(request);
        Label updated = labelRepository.update(label);
        return LabelMapper.toLabelDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Label label = new Label();
        label.setId(id);
        labelRepository.deleteById(label);
    }
}
