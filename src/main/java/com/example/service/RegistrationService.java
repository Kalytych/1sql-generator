package com.example.service;

import com.example.UserRepository;
import com.example.User;
import com.example.infrastructure.HashUtil;
import java.util.Scanner;


public class RegistrationService {
  private final UserRepository userRepository;
  private final HashUtil hashUtil;
  private final VerificationService verificationService;
  private final Scanner scanner = new Scanner(System.in);

  public RegistrationService(UserRepository userRepository, HashUtil hashUtil, VerificationService verificationService) {
    this.userRepository = userRepository;
    this.hashUtil = hashUtil;
    this.verificationService = verificationService;
  }

  public void register(String username, String email, String password) throws Exception {
    verificationService.sendCode(email);
    System.out.print("Введіть код, надісланий на вашу пошту: ");
    String inputCode = scanner.nextLine();

    if (!verificationService.verify(email, inputCode)) {
      System.out.println("Невірний код підтвердження! Реєстрацію скасовано.");
      return;
    }

    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(hashUtil.hash(password));
    userRepository.save(user);
    System.out.println("Реєстрація успішна!");
  }
}
