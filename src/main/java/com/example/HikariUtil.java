package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Утилітний клас для налаштування та отримання пулу підключень до бази даних
 * за допомогою HikariCP.
 */
public class HikariUtil {

  /**
   * Статичний пул підключень HikariDataSource.
   */
  private static final HikariDataSource dataSource;

  static {
    HikariConfig config = new HikariConfig();
    // Встановлення JDBC URL до SQLite бази даних
    config.setJdbcUrl(
        "jdbc:sqlite:C:\\Users\\kalyt\\DataGripProjects\\sql_generator\\identifier.db");
    // Максимальна кількість одночасних підключень у пулі
    config.setMaximumPoolSize(5);

    dataSource = new HikariDataSource(config);
  }

  /**
   * Повертає DataSource, що представляє пул підключень до бази даних.
   *
   * @return екземпляр HikariDataSource (пул підключень)
   */
  public static DataSource getDataSource() {
    return dataSource;
  }
}
