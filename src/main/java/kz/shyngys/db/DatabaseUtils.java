package kz.shyngys.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
    private static Connection connection;
    private static Connection transactionConnection;

    private DatabaseUtils() {
    }

    /**
     * Получить connection с autoCommit = true для обычных операций
     */
    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD
                );
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось создать соединение с БД", e);
            }
        }
        return connection;
    }

    /**
     * Получить connection с autoCommit = false для транзакций
     */
    private static Connection getTransactionConnection() {
        if (transactionConnection == null) {
            try {
                transactionConnection = DriverManager.getConnection(
                        DatabaseProperties.URL,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD
                );
                transactionConnection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось создать транзакционное соединение с БД", e);
            }
        }
        return transactionConnection;
    }

    /**
     * Получить PreparedStatement для обычных операций
     */
    public static PreparedStatement getPreparedStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать PreparedStatement", e);
        }
    }

    /**
     * Получить PreparedStatement с возвратом сгенерированных ключей
     */
    public static PreparedStatement getPreparedStatementWithGeneratedKeys(String sql) {
        try {
            return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать PreparedStatement с generated keys", e);
        }
    }

    /**
     * Получить PreparedStatement для транзакций
     */
    public static PreparedStatement getTransactionPreparedStatement(String sql) {
        try {
            return getTransactionConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать транзакционный PreparedStatement", e);
        }
    }

    /**
     * Получить PreparedStatement для транзакций с возвратом сгенерированных ключей
     */
    public static PreparedStatement getTransactionPreparedStatementWithGeneratedKeys(String sql) {
        try {
            return getTransactionConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать транзакционный PreparedStatement с generated keys", e);
        }
    }

    /**
     * Commit транзакции
     */
    public static void commit() {
        try {
            if (transactionConnection != null) {
                transactionConnection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось выполнить commit", e);
        }
    }

    /**
     * Rollback транзакции
     */
    public static void rollback() {
        try {
            if (transactionConnection != null) {
                transactionConnection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось выполнить rollback", e);
        }
    }

    /**
     * Закрыть все соединения
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
            if (transactionConnection != null && !transactionConnection.isClosed()) {
                transactionConnection.close();
                transactionConnection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось закрыть соединение с БД", e);
        }
    }
}
