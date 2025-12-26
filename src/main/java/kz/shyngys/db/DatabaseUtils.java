package kz.shyngys.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static Connection connection;

    private DatabaseUtils() {
    }

    //TODO private и добавить получение ps
    //TODO отдельный connection с autoCommit(false)
    //TODO 3 (?) отдельных ps: generatedkeys, ...
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD);
                return connection;
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось создать соединение с БД " + e);
            }
        } else {
            return connection;
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось закрыть соединение с БД " + e);
            }
        }
    }
}
