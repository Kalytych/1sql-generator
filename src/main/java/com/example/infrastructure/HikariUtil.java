package com.example.infrastructure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Утилітний клас для керування пулом з'єднань з базою даних за допомогою HikariCP.
 * <p>
 * Конфігурація підключена до SQLite-бази даних за вказаним шляхом.
 */
public class HikariUtil {

  /** Статичний пул з'єднань, який ініціалізується один раз при завантаженні класу. */
  private static final HikariDataSource dataSource;

  // Статичний ініціалізатор конфігурує пул з'єднань HikariCP
  static {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:sqlite:C:\\Users\\kalyt\\DataGripProjects\\sql_generator\\identifier.db");
    config.setMaximumPoolSize(5);
    dataSource = new HikariDataSource(config);
  }

  /**
   * Повертає об'єкт {@link DataSource} для отримання з'єднань з базою даних.
   *
   * @return екземпляр {@link DataSource}
   */
  public static DataSource getDataSource() {
    return dataSource;
  }

  /**
   * Отримує нове з'єднання з базою даних із пулу HikariCP.
   *
   * @return з'єднання з базою даних
   * @throws SQLException якщо не вдається встановити з'єднання
   */
  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  /**
   * Закриває пул з'єднань HikariCP.
   * <p>Викликайте цей метод під час завершення роботи додатку для звільнення ресурсів.</p>
   */
  public static void close() {
    dataSource.close();
  }
}
