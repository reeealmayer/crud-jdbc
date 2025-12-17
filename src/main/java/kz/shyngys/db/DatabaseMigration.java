package kz.shyngys.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseMigration {
    public static final String CHANGE_LOG_FILE = "db/changelog/db.changelog-master.xml";
    public static final String URL = "jdbc:mysql://localhost:3306/testdb";
    public static final String USERNAME = "test";
    public static final String PASSWORD = "test";
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";


    public static void migrate() {
        try {
            Class.forName(DRIVER);

            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    CHANGE_LOG_FILE,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");

            System.out.println("Migration succeed");

            connection.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to run database migrations", e);
        }
    }

    public static void main(String[] args) {
        migrate();
    }
}