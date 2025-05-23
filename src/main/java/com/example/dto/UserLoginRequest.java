package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO-клас для обробки запиту входу користувача.
 * <p>
 * Містить поля email та password з відповідною валідацією.
 * Використовується у контролерах або сервісах для передачі даних при аутентифікації.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

  /**
   * Електронна пошта користувача.
   * Має бути у форматі email та не порожня.
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
