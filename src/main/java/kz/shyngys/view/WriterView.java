package kz.shyngys.view;


import kz.shyngys.controller.WriterController;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Status;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.model.dto.WriterFullDto;
import kz.shyngys.model.dto.WriterShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriterView {
    private final WriterController writerController;
    private final Scanner scanner = new Scanner(System.in);

    public WriterView(WriterController writerController) {
        this.writerController = writerController;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Writer Management ===");
            System.out.println("1. Показать все Writers");
            System.out.println("2. Найти Writer по ID");
            System.out.println("3. Создать Writer");
            System.out.println("4. Обновить Writer");
            System.out.println("5. Удалить Writer");
            System.out.println("6. Добавить Posts к Writer");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "6" -> addPostsToWriter();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void showAll() {
        List<WriterShortDto> writers = writerController.getAll();
        if (writers.isEmpty()) {
            System.out.println("Нет Writers.");
        } else {
            System.out.println("\nСписок Writers:");
            writers.stream()
                    .forEach(System.out::println);
        }
    }

    private void getById() {
        System.out.print("Введите ID Writer: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            WriterFullDto writer = writerController.getById(id);
            System.out.println("Найдено: " + writer);
        } catch (NotFoundException e) {
            System.err.println("Writer с ID " + id + " не найден");
        }
    }

    private void create() {
        System.out.print("Введите first name нового Writer: ");
        String firstName = scanner.nextLine();
        System.out.print("Введите last name нового Writer: ");
        String lastName = scanner.nextLine();
        List<PostDto> posts = createPosts();
        WriterFullDto created = writerController.create(firstName, lastName, posts);
        writerController.getById(created.getId());
        System.out.println("Создано: ");
    }

    private void update() {
        System.out.print("Введите ID Writer для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новый first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Введите новый last name: ");
        String lastName = scanner.nextLine();

        List<PostDto> posts = updatePosts(id);

        writerController.update(id, firstName, lastName, posts);
        System.out.println("Обновлено");
    }

    private List<PostDto> updatePosts(Long writerId) {
        List<PostDto> posts = new ArrayList<>();

        System.out.println("=== Обновление Posts ===");
        while (true) {
            System.out.print("Введите ID Post для обновления или 0 для завершения или -1 для создания нового поста: ");
            Long id = Long.parseLong(scanner.nextLine());

            if (0 == id) {
                break;
            }

            if (-1 == id) {
                id = null;
            }

            System.out.print("Введите content Post: ");
            String content = scanner.nextLine().trim();

            if (content.isEmpty()) {
                System.out.println("content не может быть пустым. Попробуйте снова.");
                continue;
            }

            PostDto post = PostDto.builder()
                    .id(id)
                    .writerId(writerId)
                    .content(content)
                    .status(Status.ACTIVE).build();
            posts.add(post);
            System.out.println("Post добавлен: " + content);
        }
        return posts;
    }

    private List<PostDto> createPosts() {
        List<PostDto> posts = new ArrayList<>();

        System.out.println("=== Создание Posts ===");
        while (true) {
            System.out.print("Введите content Post (или '0' для завершения): ");
            String content = scanner.nextLine().trim();

            if ("0".equals(content)) {
                break;
            }

            if (content.isEmpty()) {
                System.out.println("content не может быть пустым. Попробуйте снова.");
                continue;
            }

            PostDto post = PostDto.builder()
                    .content(content)
                    .status(Status.ACTIVE).build();
            posts.add(post);
            System.out.println("Post добавлен: " + content);
        }
        return posts;
    }

    private void delete() {
        System.out.print("Введите ID Writer для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        writerController.delete(id);
        System.out.println("Writer удалён.");
    }

    private void addPostsToWriter() {
        System.out.print("Введите ID Writer для которого нужно добавить Posts: ");
        Long id = Long.parseLong(scanner.nextLine());
        WriterFullDto writer;
        try {
            writer = writerController.getById(id);
            System.out.println("Найдено: " + writer);
            List<PostDto> posts = new ArrayList<>();
            while (true) {
                System.out.print("Введите content Post (или '0' для завершения): ");
                String content = scanner.nextLine().trim();

                if ("0".equals(content)) {
                    break;
                }

                if (content.isEmpty()) {
                    System.out.println("content не может быть пустым. Попробуйте снова.");
                    continue;
                }

                PostDto post = PostDto.builder()
                        .content(content)
                        .status(Status.ACTIVE).build();
                posts.add(post);
                System.out.println("Post добавлен: " + content);
            }
            writerController.update(id, writer.getFirstName(), writer.getLastName(), posts);
            System.out.println("Посты добавлены");
        } catch (NotFoundException e) {
            System.err.println("Writer с ID " + id + " не найден");
        }
    }
}
