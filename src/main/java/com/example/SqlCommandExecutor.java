package com.example;

import java.util.Scanner;

/**
 * Клас для виконання SQL команд, введених користувачем через консоль.
 * <p>Програма приймає SQL-запити від користувача, виконує їх і повторює цикл,
 * поки користувач не введе 'exit'.</p>
 */
public class SqlCommandExecutor {

  /**
   * Точка входу в програму.
   * <p>Читає SQL-запити з консолі та передає їх на виконання.
   * Вихід з програми при введенні 'exit'.</p>
   *
   * @param args аргументи командного рядка (не використовуються)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Введіть SQL запит (або 'exit' для виходу):");
    while (true) {
      String query = scanner.nextLine();
      if (query.equalsIgnoreCase("exit")) {
        break;
      }
      SqlExecutor.executeInsertQuery(query); // або метод для вибірки, в залежності від запиту
    }
  }
}
