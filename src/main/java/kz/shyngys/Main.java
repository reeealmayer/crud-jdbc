package kz.shyngys;

import kz.shyngys.controller.WriterController;
import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.JdbcWriterRepositoryImpl;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.WriterServiceImpl;
import kz.shyngys.view.WriterView;

public class Main {


    public static void main(String[] args) {

        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
        WriterService writerService = new WriterServiceImpl(writerRepository);
        WriterController writerController = new WriterController(writerService);
        WriterView writerView = new WriterView(writerController);
        writerView.showMenu();

        DatabaseUtils.closeConnection();
    }
}
