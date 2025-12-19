package kz.shyngys.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

import static kz.shyngys.db.DatabaseProperties.CHANGE_LOG_FILE;
import static kz.shyngys.db.DatabaseProperties.DRIVER;
import static kz.shyngys.db.DatabaseProperties.PASSWORD;
import static kz.shyngys.db.DatabaseProperties.URL;
import static kz.shyngys.db.DatabaseProperties.USERNAME;

public class DatabaseMigration {
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