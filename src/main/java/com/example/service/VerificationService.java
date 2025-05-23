package com.example.service;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import jakarta.mail.MessagingException;

/**
 * Сервіс для генерації та перевірки кодів підтвердження для електронної пошти.
 */
public class VerificationService {
  private final MailService mailService;
  private final Map<String, String> verificationCodes = new HashMap<>();

  /**
   * Конструктор сервісу верифікації.
   *
   * @param mailService сервіс для відправки листів
   */
  public VerificationService(MailService mailService) {
    this.mailService = mailService;
  }

  /**
   * Генерує шестизначний код підтвердження, зберігає його і надсилає на вказану електронну пошту.
   *
   * @param email адреса електронної пошти для надсилання коду
   * @throws MessagingException якщо сталася помилка при відправці листа
   */
  public void sendCode(String email) throws MessagingException {
    String code = String.valueOf(new Random().nextInt(900_000) + 100_000); // 6 цифр
    verificationCodes.put(email, code);
    mailService.sendVerificationCode(email, code);
  }

  /**
   * Перевіряє, чи співпадає введений код з тим, що було збережено для цієї електронної пошти.
   *
   * @param email електронна пошта для перевірки
   * @param inputCode код, введений користувачем
   * @return true, якщо код вірний; false інакше
   */
  public boolean verify(String email, String inputCode) {
    return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(inputCode);
  }
}
