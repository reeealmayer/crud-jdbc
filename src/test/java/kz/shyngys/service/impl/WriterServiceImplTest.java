package kz.shyngys.service.impl;

import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.model.dto.WriterShortDto;
import kz.shyngys.repository.WriterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WriterServiceImplTest {

    @Mock
    WriterRepository writerRepository;

    @InjectMocks
    WriterServiceImpl writerService;

    private Writer writer;
    private WriterFullDto writerFullDto;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        post = Post.builder()
                .id(1L)
                .content("Test post")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .labels(new ArrayList<>())
                .build();

        writer = Writer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .posts(List.of(post))
                .build();

        PostDto postDto = PostDto.builder()
                .id(1L)
                .content("Test post")
                .created(post.getCreated())
                .updated(post.getUpdated())
                .status(Status.ACTIVE)
                .writerId(1L)
                .labels(new ArrayList<>())
                .build();

        writerFullDto = WriterFullDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .posts(List.of(postDto))
                .build();
    }

    @Test
    void getById_success() {
        //given
        Long writerId = 1L;

        //when
        when(writerRepository.getById(writerId)).thenReturn(writer);

        //then
        WriterFullDto result = writerService.getById(writerId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertNotNull(result.getPosts());
        assertEquals(1, result.getPosts().size());
        verify(writerRepository, times(1)).getById(writerId);
    }

    @Test
    void getById_whenWriterNotFound_throwsNotFoundException() {
        //given
        Long writerId = 999L;

        //when
        when(writerRepository.getById(writerId))
                .thenThrow(new NotFoundException("Writer not found with id: " + writerId));

        //then
        assertThrows(NotFoundException.class, () -> writerService.getById(writerId));
        verify(writerRepository, times(1)).getById(writerId);
    }

    @Test
    void getAll_success() {
        //given
        Writer writer2 = Writer.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .posts(new ArrayList<>())
                .build();

        List<Writer> writers = List.of(writer, writer2);

        //when
        when(writerRepository.getAll()).thenReturn(writers);

        //then
        List<WriterShortDto> result = writerService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        verify(writerRepository, times(1)).getAll();
    }

    @Test
    void getAll_whenEmpty_returnsEmptyList() {
        //when
        when(writerRepository.getAll()).thenReturn(new ArrayList<>());

        //then
        List<WriterShortDto> result = writerService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(writerRepository, times(1)).getAll();
    }

    @Test
    void save_success() {
        //given
        WriterFullDto request = WriterFullDto.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .posts(new ArrayList<>())
                .build();

        Writer savedWriter = Writer.builder()
                .id(3L)
                .firstName("Alice")
                .lastName("Johnson")
                .posts(new ArrayList<>())
                .build();

        //when
        when(writerRepository.save(any(Writer.class))).thenReturn(savedWriter);

        //then
        WriterFullDto result = writerService.save(request);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Alice", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        assertNotNull(result.getPosts());
        assertTrue(result.getPosts().isEmpty());
        verify(writerRepository, times(1)).save(any(Writer.class));
    }

    @Test
    void save_withPosts_success() {
        //given
        PostDto postDto = PostDto.builder()
                .content("New post")
                .status(Status.ACTIVE)
                .labels(new ArrayList<>())
                .build();

        WriterFullDto request = WriterFullDto.builder()
                .firstName("Bob")
                .lastName("Brown")
                .posts(List.of(postDto))
                .build();

        Post newPost = Post.builder()
                .id(2L)
                .content("New post")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .labels(new ArrayList<>())
                .build();

        Writer savedWriter = Writer.builder()
                .id(4L)
                .firstName("Bob")
                .lastName("Brown")
                .posts(List.of(newPost))
                .build();

        //when
        when(writerRepository.save(any(Writer.class))).thenReturn(savedWriter);

        //then
        WriterFullDto result = writerService.save(request);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Bob", result.getFirstName());
        assertEquals("Brown", result.getLastName());
        assertNotNull(result.getPosts());
        assertEquals(1, result.getPosts().size());
        verify(writerRepository, times(1)).save(any(Writer.class));
    }

    @Test
    void update_success() {
        //given
        WriterFullDto request = WriterFullDto.builder()
                .id(1L)
                .firstName("John Updated")
                .lastName("Doe Updated")
                .posts(new ArrayList<>())
                .build();

        Writer updatedWriter = Writer.builder()
                .id(1L)
                .firstName("John Updated")
                .lastName("Doe Updated")
                .posts(new ArrayList<>())
                .build();

        //when
        when(writerRepository.update(any(Writer.class))).thenReturn(updatedWriter);

        //then
        WriterFullDto result = writerService.update(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe Updated", result.getLastName());
        verify(writerRepository, times(1)).update(any(Writer.class));
    }

    @Test
    void update_whenWriterNotFound_throwsNotFoundException() {
        //given
        WriterFullDto request = WriterFullDto.builder()
                .id(999L)
                .firstName("Unknown")
                .lastName("Writer")
                .posts(new ArrayList<>())
                .build();

        //when
        when(writerRepository.update(any(Writer.class)))
                .thenThrow(new NotFoundException("Writer not found with id: 999"));

        //then
        assertThrows(NotFoundException.class, () -> writerService.update(request));
        verify(writerRepository, times(1)).update(any(Writer.class));
    }

    @Test
    void deleteById_success() {
        //given
        Long writerId = 1L;

        //when
        when(writerRepository.getById(writerId)).thenReturn(writer);
        doNothing().when(writerRepository).deleteById(any(Writer.class));

        //then
        assertDoesNotThrow(() -> writerService.deleteById(writerId));
        verify(writerRepository, times(1)).getById(writerId);
        verify(writerRepository, times(1)).deleteById(any(Writer.class));
    }

    @Test
    void deleteById_whenWriterNotFound_throwsNotFoundException() {
        //given
        Long writerId = 999L;

        //when
        when(writerRepository.getById(writerId))
                .thenThrow(new NotFoundException("Writer not found with id: " + writerId));

        //then
        assertThrows(NotFoundException.class, () -> writerService.deleteById(writerId));
        verify(writerRepository, times(1)).getById(writerId);
        verify(writerRepository, never()).deleteById(any(Writer.class));
    }

    @Test
    void deleteById_whenDeleteFails_throwsException() {
        //given
        Long writerId = 1L;

        //when
        when(writerRepository.getById(writerId)).thenReturn(writer);
        doThrow(new RuntimeException("Delete failed"))
                .when(writerRepository).deleteById(any(Writer.class));

        //then
        assertThrows(RuntimeException.class, () -> writerService.deleteById(writerId));
        verify(writerRepository, times(1)).getById(writerId);
        verify(writerRepository, times(1)).deleteById(any(Writer.class));
    }
}