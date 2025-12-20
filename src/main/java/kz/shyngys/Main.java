package kz.shyngys;

import kz.shyngys.repository.JdbcWriterRepositoryImpl;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.WriterServiceImpl;

public class Main {


    public static void main(String[] args) {
        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
        WriterService writerService = new WriterServiceImpl(writerRepository);
        writerService.deleteById(7L);
    }
}
