package com.example;

/**
 * Клас {@code User} представляє користувача в системі.
 * Містить основні поля, як-от ID, ім'я користувача, email, пароль і ID ролі.
 */
public class User {
  private long id;
  private String username;
  private String email;
  private String password;
  private int roleId;

  /**
   * Створює нового користувача з типовими значеннями:
   * id = 0, порожні рядки для імені, email та пароля, роль USER (2).
   */
  public User() {
    this.id = 0;
    this.username = "";
    this.email = "";
    this.password = "";
    this.roleId = 2; // 2 = USER (за замовчуванням)
  }

  /**
   * Створює користувача з вказаним ім'ям та email.
   * Пароль встановлюється порожнім, роль — USER.
   *
   * @param username ім'я користувача
   * @param email електронна адреса
   */
  public User(String username, String email) {
    this.id = 0;
    this.username = username;
    this.email = email;
    this.password = "";
    this.roleId = 2; // 2 = USER
  }

  /**
   * Створює користувача з повними параметрами, крім roleId.
   * Роль за замовчуванням встановлюється як USER.
   *
   * @param id ідентифікатор користувача
   * @param username ім'я користувача
   * @param email електронна адреса
   * @param password пароль
   */
  public User(long id, String username, String email, String password) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.roleId = 2; // можна параметризувати окремо
  }

  /**
   * Повертає ідентифікатор користувача.
   *
   * @return ID користувача
   */
  public long getId() {
    return id;
  }

  /**
   * Встановлює ідентифікатор користувача.
   *
   * @param id ID користувача
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Повертає ім'я користувача.
   *
   * @return ім'я користувача
   */
  public String getName() {
    return username;
  }

  /**
   * Встановлює ім'я користувача.
   *
   * @param username ім'я користувача
   */
  public void setName(String username) {
    this.username = username;
  }

  /**
   * Повертає email користувача.
   *
   * @return email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Встановлює email користувача.
   *
   * @param email електронна адреса
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Повертає пароль користувача.
   *
   * @return пароль
   */
  public String getPassword() {
    return password;
  }

  /**
   * Встановлює пароль користувача.
   *
   * @param password пароль
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Повертає ID ролі користувача.
   *
   * @return ID ролі
   */
  public int getRoleId() {
    return roleId;
  }

  /**
   * Встановлює ID ролі користувача.
   *
   * @param roleId ID ролі
   */
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  /**
   * Повертає текстове представлення користувача.
   *
   * @return рядок з усіма полями користувача
   */
  @Override
  public String toString() {
    return "User{id=" + id +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", roleId=" + roleId + "}";
  }
}
