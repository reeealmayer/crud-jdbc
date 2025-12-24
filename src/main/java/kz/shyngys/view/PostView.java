package kz.shyngys.view;


import kz.shyngys.controller.PostController;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.model.Post;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.model.dto.PostDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostView {
    private final PostController postController;
    private final Scanner scanner = new Scanner(System.in);

    public PostView(PostController postController) {
        this.postController = postController;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Post Management ===");
            System.out.println("1. Показать все Posts");
            System.out.println("2. Найти Post по ID");
            System.out.println("3. Создать Post");
            System.out.println("4. Обновить Post");
            System.out.println("5. Удалить Post");
            System.out.println("6. Добавить Labels к Post");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "6" -> addLabelsToPost();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void showAll() {
        List<PostDto> posts = postController.getAll();
        if (posts.isEmpty()) {
            System.out.println("Нет Posts.");
        } else {
            System.out.println("\nСписок Posts:");
            posts.stream()
                    .forEach(System.out::println);
        }
    }

    private void getById() {
        System.out.print("Введите ID Post: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            PostDto post = postController.getById(id);
            System.out.println("Найдено: " + post);
        } catch (NotFoundException e) {
            System.err.println("Post с ID " + id + " не найден");
        }
    }

    private void create() {
        System.out.print("Введите writerId для нового Post: ");
        Long writerId = Long.parseLong(scanner.nextLine());
        System.out.print("Введите content нового Post: ");
        String content = scanner.nextLine();
        List<LabelDto> labels = createLabels();
        postController.create(content, writerId, labels);
        System.out.println("Создано");
    }

    private void update() {
        System.out.print("Введите ID Post для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новый content: ");
        String content = scanner.nextLine();

        List<LabelDto> labels = createLabels();

        postController.update(id, null, content, labels);
        System.out.println("Обновлено");
    }

    private List<LabelDto> createLabels() {
        List<LabelDto> labels = new ArrayList<>();

        System.out.println("=== Создание Labels ===");
        while (true) {
            System.out.print("Введите название Label (или '0' для завершения): ");
            String name = scanner.nextLine().trim();

            if ("0".equals(name)) {
                break;
            }

            if (name.isEmpty()) {
                System.out.println("Название не может быть пустым. Попробуйте снова.");
                continue;
            }

            System.out.print("Введите id Label ");

            LabelDto label = new LabelDto();
            label.setName(name);
            labels.add(label);
            System.out.println("Label добавлен: " + name);
        }
        return labels;
    }

    private void delete() {
        System.out.print("Введите ID Post для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        postController.delete(id);
        System.out.println("Post удалён.");
    }

    private void addLabelsToPost() {
        System.out.print("Введите ID Post для которого нужно добавить Labels: ");
        Long id = Long.parseLong(scanner.nextLine());
        PostDto postDto;
        try {
            postDto = postController.getById(id);
            System.out.println("Найдено: " + postDto);
        } catch (NotFoundException e) {
            System.err.println("Post с ID " + id + " не найден");
            return;
        }

        List<LabelDto> labelDtos = createLabels();

        postController.update(id, null, postDto.getContent(), labelDtos);

        System.out.println("Labels обновлены");
    }
}
