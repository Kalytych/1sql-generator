package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

  @Email(message = "Невірний формат електронної пошти")
  @NotBlank(message = "Електронна пошта не може бути порожньою")
  private String email;

  @NotBlank(message = "Пароль не може бути порожнім")
  private String password;
}
