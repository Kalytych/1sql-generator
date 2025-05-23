package com.example;

import com.example.infrastructure.HikariUtil;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Клас для запуску міграцій бази даних.
 * Виконує SQL-скрипти з файлу ddl.sql для ініціалізації структури бази даних.
 */
public class MigrationRunner {

  /**
   * Запускає міграції бази даних, читаючи SQL-запити з файлу ddl.sql
   * та послідовно виконуючи їх у базі даних.
   * Виводить у консоль статус виконання.
   */
  public static void runMigrations() {
    try (Connection conn = HikariUtil.getConnection();
        Statement stmt = conn.createStatement()) {

      String ddl = Files.readString(
          Path.of("C:\\\\Users\\\\kalyt\\\\DataGripProjects\\\\sql_generator\\\\migrations\\\\ddl.sql"),
          StandardCharsets.UTF_8
      );

      for (String sql : ddl.split(";")) {
        if (!sql.trim().isEmpty()) {
          stmt.execute(sql);
        }
      }
      System.out.println("✅ Міграції виконано успішно.");

    } catch (Exception e) {
      System.err.println("❌ Помилка при запуску міграцій:");
      e.printStackTrace();
    }
  }
}
