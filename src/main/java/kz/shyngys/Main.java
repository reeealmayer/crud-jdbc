package kz.shyngys;

import kz.shyngys.model.Writer;
import kz.shyngys.repository.JdbcWriterRepositoryImpl;
import kz.shyngys.repository.WriterRepository;

public class Main {


    public static void main(String[] args) {
        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
        Writer byId = writerRepository.getById(30L);
        System.out.println(byId);
    }
}
