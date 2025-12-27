package kz.shyngys.service.impl;

import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Post;
import kz.shyngys.model.Status;
import kz.shyngys.model.Writer;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.utils.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceImplTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostServiceImpl postService;

    private Post post;
    private PostDto postDto;
    private Writer writer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        writer = TestDataUtils.createWriter();

        post = TestDataUtils.createPost();

        postDto = TestDataUtils.createPostDto();
    }

    @Test
    void getById_success() {
        //given
        Long postId = 1L;

        //when
        when(postRepository.getById(postId)).thenReturn(post);

        //then
        PostDto result = postService.getById(postId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test content", result.getContent());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(1L, result.getWriterId());
        verify(postRepository, times(1)).getById(postId);
    }

    @Test
    void getById_whenPostNotFound_throwsNotFoundException() {
        //given
        Long postId = 999L;

        //when
        when(postRepository.getById(postId)).thenThrow(new NotFoundException("Post not found with id: " + postId));

        //then
        assertThrows(NotFoundException.class, () -> postService.getById(postId));
        verify(postRepository, times(1)).getById(postId);
    }

    @Test
    void getAll_success() {
        //given
        Post post2 = Post.builder()
                .id(2L)
                .content("Second post")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .writer(writer)
                .labels(new ArrayList<>())
                .build();

        List<Post> posts = List.of(post, post2);

        //when
        when(postRepository.getAll()).thenReturn(posts);

        //then
        List<PostDto> result = postService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Test content", result.get(0).getContent());
        assertEquals("Second post", result.get(1).getContent());
        verify(postRepository, times(1)).getAll();
    }

    @Test
    void getAll_whenEmpty_returnsEmptyList() {
        //when
        when(postRepository.getAll()).thenReturn(new ArrayList<>());

        //then
        List<PostDto> result = postService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postRepository, times(1)).getAll();
    }

    @Test
    void save_withWriterId_success() {
        //given
        PostDto request = PostDto.builder()
                .content("New post")
                .status(Status.ACTIVE)
                .writerId(1L)
                .labels(new ArrayList<>())
                .build();

        Post savedPost = Post.builder()
                .id(1L)
                .content("New post")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .writer(writer)
                .labels(new ArrayList<>())
                .build();

        //when
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        //then
        PostDto result = postService.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New post", result.getContent());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(1L, result.getWriterId());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postRepository, never()).getById(any());
    }

    @Test
    void save_withoutWriterId_fetchesWriterFromExistingPost() {
        //given
        PostDto request = PostDto.builder()
                .id(1L)
                .content("Updated content")
                .status(Status.ACTIVE)
                .writerId(null)
                .labels(new ArrayList<>())
                .build();

        Post savedPost = Post.builder()
                .id(1L)
                .content("Updated content")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .writer(writer)
                .labels(new ArrayList<>())
                .build();

        //when
        when(postRepository.getById(1L)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        //then
        PostDto result = postService.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated content", result.getContent());
        assertEquals(1L, result.getWriterId());
        verify(postRepository, times(1)).getById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void update_withWriterId_success() {
        //given
        PostDto request = PostDto.builder()
                .id(1L)
                .content("Updated content")
                .status(Status.ACTIVE)
                .writerId(1L)
                .labels(new ArrayList<>())
                .build();

        Post updatedPost = Post.builder()
                .id(1L)
                .content("Updated content")
                .created(post.getCreated())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .writer(writer)
                .labels(new ArrayList<>())
                .build();

        //when
        when(postRepository.update(any(Post.class))).thenReturn(updatedPost);

        //then
        PostDto result = postService.update(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated content", result.getContent());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(1L, result.getWriterId());
        verify(postRepository, times(1)).update(any(Post.class));
        verify(postRepository, never()).getById(any());
    }

    @Test
    void update_withoutWriterId_fetchesWriterFromExistingPost() {
        //given
        PostDto request = PostDto.builder()
                .id(1L)
                .content("Updated content")
                .status(Status.ACTIVE)
                .writerId(null)
                .labels(new ArrayList<>())
                .build();

        Post updatedPost = Post.builder()
                .id(1L)
                .content("Updated content")
                .created(post.getCreated())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .writer(writer)
                .labels(new ArrayList<>())
                .build();

        //when
        when(postRepository.getById(1L)).thenReturn(post);
        when(postRepository.update(any(Post.class))).thenReturn(updatedPost);

        //then
        PostDto result = postService.update(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated content", result.getContent());
        assertEquals(1L, result.getWriterId());
        verify(postRepository, times(1)).getById(1L);
        verify(postRepository, times(1)).update(any(Post.class));
    }

    @Test
    void deleteById_success() {
        //given
        Long postId = 1L;

        //when
        doNothing().when(postRepository).deleteById(any(Post.class));

        //then
        assertDoesNotThrow(() -> postService.deleteById(postId));
        verify(postRepository, times(1)).deleteById(any(Post.class));
    }

    @Test
    void deleteById_whenPostNotFound_throwsNotFoundException() {
        //given
        Long postId = 999L;

        //when
        doThrow(new NotFoundException("Post not found with id: " + postId))
                .when(postRepository).deleteById(any(Post.class));

        //then
        assertThrows(NotFoundException.class, () -> postService.deleteById(postId));
        verify(postRepository, times(1)).deleteById(any(Post.class));
    }


}