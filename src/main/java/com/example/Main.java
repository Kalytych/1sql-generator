package com.example;

import com.example.infrastructure.HashUtil;
import com.example.service.*;
import com.example.AdminMenu;

import java.sql.*;
import java.util.Scanner;

public class Main {

  private static final String DB_URL = "jdbc:sqlite:C:\\Users\\kalyt\\DataGripProjects\\sql_generator\\identifier.db";

  public static void main(String[] args) throws Exception {
    MigrationRunner.runMigrations();

    Scanner scanner = new Scanner(System.in);
    UserRepository userRepository = new UserRepository();
    HashUtil hashUtil = new HashUtil();
    MailService mailService = new MailService();
    VerificationService verificationService = new VerificationService(mailService);
    RoleRepository roleRepository = new RoleRepository(); // Додано
    RegistrationService registrationService = new RegistrationService(userRepository, hashUtil, verificationService, roleRepository); // Оновлено
    AuthenticationService authService = new AuthenticationService(userRepository, hashUtil);
    UserService userService = new UserService(userRepository, hashUtil);
    SqlQueryRepository sqlQueryRepository = new SqlQueryRepository();
    QueryTypeRepository queryTypeRepository = new QueryTypeRepository();

    while (true) {
      System.out.println("\n=== ГОЛОВНЕ МЕНЮ ===");
      System.out.println("1 - Реєстрація");
      System.out.println("2 - Вхід");
      System.out.println("3 - Видалити користувача");
      System.out.println("4 - Вийти з програми");
      System.out.print("Ваш вибір: ");
      String choice = scanner.nextLine();

      switch (choice) {
        case "1" -> {
          System.out.print("Введіть ім’я користувача: ");
          String username = scanner.nextLine();
          System.out.print("Введіть email: ");
          String email = scanner.nextLine();
          System.out.print("Введіть пароль: ");
          String password = scanner.nextLine();

          // Надіслати код підтвердження на email
          try {
            registrationService.sendVerificationCode(email);
            System.out.println("Код підтвердження надіслано на вашу пошту. Перевірте email.");

            // Запитати код підтвердження
            System.out.print("Введіть код підтвердження: ");
            String verificationCode = scanner.nextLine();

            // Показати список ролей і вибрати роль
            System.out.println("Доступні ролі:");
            var roles = registrationService.getAllRoles();
            for (var role : roles) {
              System.out.println(role.getId() + " - " + role.getName());
            }
            System.out.print("Введіть ID ролі: ");
            int roleId = Integer.parseInt(scanner.nextLine());

            // Викликати метод реєстрації з 5 параметрами
            registrationService.register(username, email, password, verificationCode, roleId);
            System.out.println("Реєстрація успішна!");
          } catch (Exception e) {
            System.out.println("Помилка під час реєстрації: " + e.getMessage());
          }
        }
        case "2" -> {
          System.out.print("Email: ");
          String email = scanner.nextLine();
          System.out.print("Пароль: ");
          String password = scanner.nextLine();

          User user = authService.login(email, password);
          if (user != null) {
            System.out.println("Успішний вхід! Вітаємо, " + user.getName());

            if (user.getRoleId() == 1) {
              System.out.println("Ваша роль: Адміністратор");
              AdminMenu adminMenu = new AdminMenu(userService, sqlQueryRepository);
              adminMenu.start(scanner);  // ось тут зміни
            } else {
              System.out.println("Ваша роль: Користувач");
              executeSqlQuery(scanner, user, sqlQueryRepository, queryTypeRepository);
            }
          } else {
            System.out.println("Невірний email або пароль!");
          }
        }

        case "3" -> {
          System.out.print("Введіть ім’я користувача: ");
          String username = scanner.nextLine();
          System.out.print("Введіть пароль: ");
          String password = scanner.nextLine();
          boolean deleted = userService.deleteByUsernameAndPassword(username, password);
          if (deleted) {
            System.out.println("Користувача видалено.");
          } else {
            System.out.println("Користувача не знайдено або пароль невірний.");
          }
        }

        case "4" -> {
          System.out.println("Програму завершено. До зустрічі!");
          return;
        }

        default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
      }
    }
  }

  private static void executeSqlQuery(Scanner scanner, User user,
      SqlQueryRepository sqlQueryRepository,
      QueryTypeRepository queryTypeRepository) {
    try (Connection connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement()) {

      while (true) {
        System.out.println("\nВведіть SQL запит (або напишіть 'exit' для виходу):");
        String sqlQueryText = scanner.nextLine();

        if (sqlQueryText.trim().equalsIgnoreCase("exit")) {
          System.out.println("Вихід із режиму SQL-запитів.");
          break;
        }

        try {
          if (sqlQueryText.toLowerCase().startsWith("select")) {
            ResultSet resultSet = statement.executeQuery(sqlQueryText);
            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
              for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
              }
              System.out.println();
            }
          } else {
            int rowsAffected = statement.executeUpdate(sqlQueryText);
            System.out.println("Операція успішно виконана. Змінено рядків: " + rowsAffected);
          }

          // Збереження SQL-запиту в БД
          String queryTypeName = getQueryType(sqlQueryText);
          int typeId = queryTypeRepository.findOrCreateType(queryTypeName);

          SqlQuery sqlQuery = new SqlQuery();
          sqlQuery.setQueryText(sqlQueryText);
          sqlQuery.setQueryTypeId(typeId);
          sqlQuery.setUserId(user.getId());

          sqlQueryRepository.save(sqlQuery.getQueryText(), typeId, user.getId());



        } catch (Exception e) {
          System.out.println("❌ Помилка виконання SQL запиту: " + e.getMessage());
        }
      }

    } catch (Exception e) {
      System.out.println("❌ Помилка підключення до бази даних: " + e.getMessage());
    }
  }

  private static String getQueryType(String query) {
    String[] tokens = query.trim().split("\\s+");
    return tokens.length > 0 ? tokens[0].toUpperCase() : "UNKNOWN";
  }
}