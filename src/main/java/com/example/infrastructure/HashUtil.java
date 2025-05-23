package com.example.infrastructure;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Утилітний клас для хешування та перевірки паролів за допомогою алгоритму BCrypt.
 */
public class HashUtil {

  /**
   * Генерує BCrypt-хеш для переданого пароля.
   *
   * @param password звичайний текстовий пароль
   * @return хешований пароль у форматі BCrypt
   */
  public static String hash(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }

  /**
   * Перевіряє, чи відповідає введений пароль хешованому значенню.
   *
   * @param password звичайний (нехешований) пароль
   * @param hashedPassword хешований пароль, з яким порівнюється
   * @return {@code true}, якщо паролі збігаються; інакше {@code false}
   */
  public boolean matches(String password, String hashedPassword) {
    return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
  }

  /**
   * Перевіряє відповідність між звичайним і хешованим паролем.
   * <p>Функціонально ідентичний методу {@link #matches(String, String)}</p>
   *
   * @param plainPassword звичайний пароль
   * @param hashedPassword хешований пароль
   * @return {@code true}, якщо паролі збігаються; інакше {@code false}
   */
  public boolean verifyPassword(String plainPassword, String hashedPassword) {
    return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
  }

  /**
   * Хешує пароль з використанням алгоритму BCrypt.
   * <p>Функціонально ідентичний методу {@link #hash(String)}</p>
   *
   * @param password звичайний пароль
   * @return хешований пароль
   */
  public String hashPassword(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }
}
