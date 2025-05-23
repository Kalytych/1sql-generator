package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Клас для виконання SQL-запитів, що змінюють дані (INSERT, UPDATE, DELETE).
 */
public class SqlExecutor {

  /**
   * Виконує SQL-запит, який змінює дані в базі (наприклад, INSERT).
   *
   * @param query SQL-запит для виконання
   */
  public static void executeInsertQuery(String query) {
    try (Connection connection = DatabaseConnection.connect();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.executeUpdate();
      System.out.println("Запит виконано успішно!");
    } catch (SQLException e) {
      System.err.println("Помилка виконання SQL запиту: " + e.getMessage());
    }
  }
}
