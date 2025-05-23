package com.example.service;

import com.example.User;
import com.example.UserRepository;
import java.util.Scanner;

public class AuthService {
  private static final Scanner scanner = new Scanner(System.in);
  private final DatabaseService dbService;
  private final UserRepository userRepository;

  public AuthService(DatabaseService dbService, UserRepository userRepository) {
    this.dbService = dbService;
    this.userRepository = userRepository;
  }

  public void authenticateUser() {
    System.out.print("Введіть email: ");
    String email = scanner.nextLine();
    System.out.print("Введіть пароль: ");
    String password = scanner.nextLine();

    User user = userRepository.findByEmail(email);

    if (user != null && user.getPassword().equals(password)) {
      System.out.println("Авторизація пройшла успішно, " + user.getName());

      if (user.getRoleId() == 1) { // ADMIN
        System.out.println("1 - Виконати SQL");
        System.out.println("2 - Видалити користувача");
        System.out.println("3 - Очистити SQL-запити");

        int choice = scanner.nextInt();
        scanner.nextLine(); // очистка буфера

        switch (choice) {
          case 1 -> executeSqlQueries(user);
          case 2 -> {
            System.out.print("Введіть ID користувача для видалення: ");
            int id = scanner.nextInt();
            userRepository.deleteById(id);
          }
          case 3 -> {
            // реалізація очищення SQL-запитів
            System.out.println("SQL-запити видалено.");
          }
          default -> System.out.println("Невідома команда");
        }
      } else {
        executeSqlQueries(user); // звичайний користувач
      }

    } else {
      System.out.println("Невірний email або пароль.");
    }
  }

  private void executeSqlQueries(User user) {
    while (true) {
      System.out.print("Введіть SQL-запит (або 'exit' для виходу): ");
      String query = scanner.nextLine();

      if (query.equalsIgnoreCase("exit")) break;

      dbService.executeQuery(query);
      // або dbService.executeAndSave(query, user); якщо зберігаєш
    }
  }
}
