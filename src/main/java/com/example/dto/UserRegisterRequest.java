package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO-клас для обробки запиту реєстрації користувача.
 * <p>
 * Містить ім'я користувача, електронну пошту та пароль.
 * Використовується для передачі даних при створенні нового облікового запису.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

  /**
   * Ім'я користувача.
   * Поле не може бути порожнім.
   */
  @NotBlank(message = "Ім'я користувача не може бути порожнім")
  private String username;

  /**
   * Електронна пошта користувача.
   * Має бути у форматі email та не бути порожньою.
   */
  @Email(message = "Невірний формат електронної пошти")
  @NotBlank(message = "Електронна пошта не може бути порожньою")
  private String email;

  /**
   * Пароль користувача.
   * Поле є обов’язковим для заповнення.
   */
  @NotBlank(message = "Пароль не може бути порожнім")
  private String password;
}
