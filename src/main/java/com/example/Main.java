package com.example;

import com.example.infrastructure.HashUtil;
import com.example.infrastructure.MailService;
import com.example.service.RegistrationService;
import com.example.service.VerificationService;
import com.example.service.AuthenticationService;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    UserRepository userRepository = new UserRepository();
    HashUtil hashUtil = new HashUtil();
    MailService mailService = new MailService();
    VerificationService verificationService = new VerificationService(mailService);
    RegistrationService registrationService = new RegistrationService(userRepository, hashUtil, verificationService);
    AuthenticationService authService = new AuthenticationService(userRepository, hashUtil);

    System.out.println("Виберіть опцію: 1 - Реєстрація, 2 - Вхід");
    String choice = scanner.nextLine();

    if (choice.equals("1")) {
      System.out.print("Введіть ім’я користувача: ");
      String username = scanner.nextLine();
      System.out.print("Введіть email: ");
      String email = scanner.nextLine();
      System.out.print("Введіть пароль: ");
      String password = scanner.nextLine();

      registrationService.register(username, email, password);
    } else if (choice.equals("2")) {
      System.out.print("Email: ");
      String email = scanner.nextLine();
      System.out.print("Пароль: ");
      String password = scanner.nextLine();

      boolean success = authService.login(email, password);
      if (success) {
        System.out.println("Успішний вхід!");
      } else {
        System.out.println("Невірний email або пароль!");
      }
    } else {
      System.out.println("Невірний вибір.");
    }
  }
}
