package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import com.example.service.UserService;

public class AdminMenu {

  private static final String DB_URL = "jdbc:sqlite:C:\\Users\\kalyt\\DataGripProjects\\sql_generator\\identifier.db";

  private final UserService userService;
  private final SqlQueryRepository sqlQueryRepository;

  public AdminMenu(UserService userService, SqlQueryRepository sqlQueryRepository) {
    this.userService = userService;
    this.sqlQueryRepository = sqlQueryRepository;
  }

  public void start(Scanner scanner) {
    while (true) {
      System.out.println("\n=== Меню Адміністратора ===");
      System.out.println("1 - Виконати SQL запит");
      System.out.println("2 - Видалити користувача");
      System.out.println("3 - Видалити SQL запит");
      System.out.println("4 - Вийти з адмін меню");
      System.out.print("Ваш вибір: ");
      String choice = scanner.nextLine();

      switch (choice) {
        case "1" -> executeSqlQuery(scanner);
        case "2" -> deleteUser(scanner);
        case "3" -> deleteSqlQuery(scanner);
        case "4" -> {
          System.out.println("Вихід з адмін меню.");
          return;
        }
        default -> System.out.println("Невірний вибір, спробуйте ще раз.");
      }
    }
  }

  private void executeSqlQuery(Scanner scanner) {
    System.out.println("Введіть SQL запит:");
    String sql = scanner.nextLine();

    try (Connection connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement()) {

      if (sql.trim().toLowerCase().startsWith("select")) {
        ResultSet rs = statement.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();

        System.out.println("===== ПОЧАТОК ВИВОДУ =====");

        // Вивід заголовків
        for (int i = 1; i <= columnCount; i++) {
          System.out.print(rs.getMetaData().getColumnName(i) + "\t");
        }
        System.out.println();

        // Вивід рядків
        while (rs.next()) {
          for (int i = 1; i <= columnCount; i++) {
            System.out.print(rs.getString(i) + "\t");
          }
          System.out.println();
        }
        rs.close();

        System.out.println("===== КІНЕЦЬ ВИВОДУ =====");

      } else {
        int updateCount = statement.executeUpdate(sql);
        System.out.println("Операція успішно виконана. Змінено рядків: " + updateCount);
      }

    } catch (Exception e) {
      System.out.println("❌ Помилка виконання запиту: " + e.getMessage());
    }
  }



  private void deleteUser(Scanner scanner) {
    System.out.print("Введіть ім'я користувача для видалення: ");
    String username = scanner.nextLine();
    boolean deleted = userService.deleteByUsername(username);
    if (deleted) {
      System.out.println("Користувача видалено.");
    } else {
      System.out.println("Користувача не знайдено.");
    }
  }

  private void deleteSqlQuery(Scanner scanner) {
    System.out.print("Введіть ID SQL запиту для видалення: ");
    String idStr = scanner.nextLine();
    try {
      int id = Integer.parseInt(idStr);
      sqlQueryRepository.deleteById(id);
      System.out.println("SQL запит видалено, якщо такий існував.");
    } catch (NumberFormatException e) {
      System.out.println("Невірний формат ID.");
    }
  }
}
