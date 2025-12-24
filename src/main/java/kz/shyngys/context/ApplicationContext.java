package kz.shyngys.context;


import kz.shyngys.controller.LabelController;
import kz.shyngys.controller.PostController;
import kz.shyngys.controller.WriterController;
import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.JdbcLabelRepositoryImpl;
import kz.shyngys.repository.impl.JdbcPostRepositoryImpl;
import kz.shyngys.repository.impl.JdbcWriterRepositoryImpl;
import kz.shyngys.service.LabelService;
import kz.shyngys.service.PostService;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.LabelServiceImpl;
import kz.shyngys.service.impl.PostServiceImpl;
import kz.shyngys.service.impl.WriterServiceImpl;
import kz.shyngys.view.LabelView;
import kz.shyngys.view.PostView;
import kz.shyngys.view.WriterView;

import java.util.Objects;

public class ApplicationContext {
    private static ApplicationContext instance;

    private final WriterView writerView;
    private final LabelView labelView;
    private final PostView postView;

    private ApplicationContext() {
        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
        WriterService writerService = new WriterServiceImpl(writerRepository);
        WriterController writerController = new WriterController(writerService);
        this.writerView = new WriterView(writerController);

        PostRepository postRepository = new JdbcPostRepositoryImpl();
        PostService postService = new PostServiceImpl(postRepository);
        PostController postController = new PostController(postService);
        this.postView = new PostView(postController);

        LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
        LabelService labelService = new LabelServiceImpl(labelRepository);
        LabelController labelController = new LabelController(labelService);
        this.labelView = new LabelView(labelController);
    }

    public static ApplicationContext getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public WriterView getWriterView() {
        return writerView;
    }

    public LabelView getLabelView() {
        return labelView;
    }

    public PostView getPostView() {
        return postView;
    }

    public static void closeContext() {
        DatabaseUtils.closeConnection();
    }
}
