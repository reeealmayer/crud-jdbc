package kz.shyngys;

import kz.shyngys.exception.WriterNotFoundException;
import kz.shyngys.repository.JdbcWriterRepositoryImpl;
import kz.shyngys.repository.WriterRepository;

public class Main {


    public static void main(String[] args) {
        WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
        try {
//            Writer byId = writerRepository.getById(5L);
//            System.out.println(byId);

//            List<Writer> all = writerRepository.getAll();
//            System.out.println(all.toString());

//            Writer writer = new Writer();
//            writer.setFirstName("China");
//            writer.setLastName("Esenbaev");
//            Writer save = writerRepository.save(writer);
//            System.out.println(save);

//            Writer writer = new Writer();
//            writer.setId(10L);
//            writer.setFirstName("Arthus");
//            writer.setLastName("Ivanov");
//            Writer save = writerRepository.update(writer);
//            System.out.println(save);

//            Writer writer = new Writer();
//            writer.setId(4L);
//            writerRepository.deleteById(writer);
        } catch (WriterNotFoundException e) {
            e.printStackTrace();
        }
    }
}
