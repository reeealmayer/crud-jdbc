package kz.shyngys.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {
    }

    public static Connection getInstance() {
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
