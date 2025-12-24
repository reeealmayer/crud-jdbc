package kz.shyngys;

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

public class Main {


    public static void main(String[] args) {

//        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
//        WriterService writerService = new WriterServiceImpl(writerRepository);
//        WriterController writerController = new WriterController(writerService);
//        WriterView writerView = new WriterView(writerController);
//        writerView.showMenu();

//        PostRepository postRepository = new JdbcPostRepositoryImpl();
//        PostService postService = new PostServiceImpl(postRepository);
//        PostController postController = new PostController(postService);
//        PostView postView = new PostView(postController);
//        postView.showMenu();

        LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
        LabelService labelService = new LabelServiceImpl(labelRepository);
        LabelController labelController = new LabelController(labelService);
        LabelView labelView = new LabelView(labelController);
        labelView.showMenu();

        DatabaseUtils.closeConnection();
    }
}
