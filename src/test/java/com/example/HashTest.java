package com.example;

import com.example.infrastructure.HashUtil;

public class HashTest {
  public static void main(String[] args) {
    String password = "123456";

    // Хешуємо пароль
    String hashed = HashUtil.hash(password);
    System.out.println("Хеш: " + hashed);

    // Перевіряємо правильність
    boolean matches = HashUtil.matches(password, hashed);
    System.out.println("Пароль збігається: " + matches);
  }
}
