package com.example;

import com.example.infrastructure.HashUtil;

public class HashTest {
  public static void main(String[] args) {
    String password = "123456";

    // Створюємо об'єкт HashUtil
    HashUtil hashUtil = new HashUtil();

    // Хешуємо пароль (припускаємо, що метод hash теж нестатичний)
    String hashed = hashUtil.hash(password);
    System.out.println("Хеш: " + hashed);

    // Перевіряємо правильність
    boolean matches = hashUtil.matches(password, hashed);
    System.out.println("Пароль збігається: " + matches);
  }
}
