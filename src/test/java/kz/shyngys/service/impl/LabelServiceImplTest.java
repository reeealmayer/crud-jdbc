package kz.shyngys.service.impl;

import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Label;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.utils.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LabelServiceImplTest {

    @Mock
    LabelRepository labelRepository;

    @InjectMocks
    LabelServiceImpl labelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByIdShouldReturnLabel() {
        //given
        Label label = TestDataUtils.createLabel(1L, "name");

        //when
        when(labelRepository.getById(anyLong())).thenReturn(label);

        //then
        LabelDto labelDto = labelService.getById(1L);

        assertNotNull(labelDto);
        assertEquals(1L, labelDto.getId());
        assertEquals("name", labelDto.getName());
    }

    @Test
    void getByIdShouldThrowException() {
        //given

        //when
        when(labelRepository.getById(anyLong())).thenThrow(new NotFoundException("Label не найден с id 1L"));

        //then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> labelService.getById(1L));

        assertEquals("Label не найден с id 1L", exception.getMessage());
    }

    @Test
    void getAllShouldReturnLabels() {
        //given
        Label label1 = TestDataUtils.createLabel(1L, "name1");
        Label label2 = TestDataUtils.createLabel(2L, "name2");

        //when
        when(labelRepository.getAll()).thenReturn(List.of(label1, label2));

        //then
        List<LabelDto> result = labelService.getAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("name1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("name2", result.get(1).getName());
    }

    @Test
    void getAllShouldThrowException() {
        //given

        //when
        when(labelRepository.getAll()).thenThrow(new NotFoundException("В таблице labels нет записей"));

        //then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> labelService.getAll());

        assertEquals("В таблице labels нет записей", exception.getMessage());
    }

    @Test
    void saveShouldSuccess() {
        //given
        LabelDto request = TestDataUtils.createLabelDto(1L, "new Label");

        Label label = TestDataUtils.createLabel(1L, request.getName());

        //when
        when(labelRepository.save(any())).thenReturn(label);

        //then
        LabelDto saved = labelService.save(request);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("new Label", saved.getName());
    }

    @Test
    void updateShouldSuccess() {
        //given
        LabelDto request = TestDataUtils.createLabelDto(1L, "new Label");

        Label label = TestDataUtils.createLabel(1L, request.getName());

        //when
        when(labelRepository.update(any())).thenReturn(label);

        //then
        LabelDto saved = labelService.update(request);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("new Label", saved.getName());
    }

    @Test
    void deleteShouldSuccess() {
        //given
        Label label = TestDataUtils.createLabel(1L, null);

        //when
        doNothing().when(labelRepository).deleteById(label);

        //then
        labelService.deleteById(label.getId());
        verify(labelRepository, times(1)).deleteById(label);
    }

}