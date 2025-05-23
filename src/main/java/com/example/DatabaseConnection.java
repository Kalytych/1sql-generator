package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Клас для встановлення підключення до SQLite бази даних.
 */
public class DatabaseConnection {

  /**
   * URL підключення до бази даних SQLite.
   * Зверніть увагу, що шлях є абсолютним і повинен бути змінений під вашу систему.
   */
  private static final String DB_URL = "jdbc:sqlite:C:\\Users\\kalyt\\DataGripProjects\\sql_generator\\identifier.db";

  /**
   * Метод для встановлення підключення до бази даних.
   *
   * @return Об'єкт Connection, якщо підключення встановлено успішно, інакше null
   */
  public static Connection connect() {
    try {
      // Підключення до бази даних
      Connection conn = DriverManager.getConnection(DB_URL);
      if (conn != null) {
        System.out.println("Підключення до бази даних успішне!");
        return conn;
      }
    } catch (SQLException e) {
      System.out.println("Помилка підключення до бази даних: " + e.getMessage());
    }
    return null;
  }

  /**
   * Тестовий метод main для перевірки підключення до бази даних.
   *
   * @param args аргументи командного рядка
   */
  public static void main(String[] args) {
    Connection connection = connect();
    if (connection != null) {
      System.out.println("Підключення успішне!");
    }
  }
}
