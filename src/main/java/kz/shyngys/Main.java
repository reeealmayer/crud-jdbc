package kz.shyngys;

import kz.shyngys.db.DatabaseUtils;
import kz.shyngys.repository.LabelRepository;
import kz.shyngys.repository.impl.JdbcLabelRepositoryImpl;

public class Main {


    public static void main(String[] args) {
        LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
        DatabaseUtils.closeConnection();
    }
}
