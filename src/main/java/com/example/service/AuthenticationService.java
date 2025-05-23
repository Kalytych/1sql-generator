package com.example.service;

import com.example.infrastructure.HashUtil;
import com.example.UserRepository;
import com.example.User;

/**
 * Сервіс для автентифікації користувачів за email та паролем.
 * <p>
 * Використовує {@link UserRepository} для пошуку користувача та {@link HashUtil} для перевірки пароля.
 */
public class AuthenticationService {
  private final UserRepository userRepository;
  private final HashUtil hashUtil;

  /**
   * Конструктор класу {@code AuthenticationService}.
   *
   * @param userRepository репозиторій користувачів для доступу до збережених даних
   * @param hashUtil утиліта для хешування та перевірки паролів
   */
  public AuthenticationService(UserRepository userRepository, HashUtil hashUtil) {
    this.userRepository = userRepository;
    this.hashUtil = hashUtil;
  }

  /**
   * Авторизує користувача за вказаними електронною поштою та паролем.
   *
   * @param email    електронна пошта користувача
   * @param password пароль у незахешованому вигляді
   * @return об'єкт {@link User}, якщо автентифікація пройшла успішно; інакше {@code null}
   */
  public User login(String email, String password) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      System.out.println("Користувача з такою електронною адресою не знайдено.");
      return null;
    }

    if (!hashUtil.matches(password, user.getPassword())) {
      System.out.println("Неправильний пароль.");
      return null;
    }

    System.out.println("Вхід виконано успішно. Ласкаво просимо, " + user.getName() + "!");
    return user;
  }
}
