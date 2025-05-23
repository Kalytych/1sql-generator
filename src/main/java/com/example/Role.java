package com.example;

import lombok.Data;

/**
 * Представляє роль користувача в системі.
 *
 * <p>Клас містить унікальний ідентифікатор ролі та її назву.</p>
 *
 * @author
 */
@Data
public class Role {

  /**
   * Унікальний ідентифікатор ролі.
   */
  private int id;

  /**
   * Назва ролі (наприклад, "Адміністратор", "Користувач").
   */
  private String name;
}
