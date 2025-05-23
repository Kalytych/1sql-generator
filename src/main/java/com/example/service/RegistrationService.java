package com.example.service;

import com.example.UserRepository;
import com.example.User;
import com.example.Role;
import com.example.RoleRepository;
import com.example.infrastructure.HashUtil;

import java.util.List;

/**
 * Сервіс для реєстрації користувачів з підтвердженням електронної пошти.
 */
public class RegistrationService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final HashUtil hashUtil;
  private final VerificationService verificationService;

  public RegistrationService(UserRepository userRepository, HashUtil hashUtil,
      VerificationService verificationService, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.hashUtil = hashUtil;
    this.verificationService = verificationService;
    this.roleRepository = roleRepository;
  }

  /**
   * Надсилає код підтвердження на вказаний email.
   */
  public void sendVerificationCode(String email) throws Exception {
    verificationService.sendCode(email);
  }

  /**
   * Реєструє нового користувача після перевірки коду підтвердження.
   *
   * @param name ім'я користувача
   * @param email електронна пошта користувача
   * @param password пароль користувача у відкритому вигляді
   * @param verificationCode код підтвердження з пошти
   * @param roleId ідентифікатор ролі користувача
   * @throws Exception якщо код не підтверджено або роль не знайдена
   */
  public void register(String name, String email, String password, String verificationCode, int roleId) throws Exception {
    if (!verificationService.verify(email, verificationCode)) {
      throw new Exception("Невірний код підтвердження!");
    }

    Role role = roleRepository.findById(roleId);
    if (role == null) {
      throw new Exception("Роль з таким ID не існує!");
    }

    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPassword(hashUtil.hash(password));
    user.setRoleId(roleId);

    userRepository.save(user);
  }

  /**
   * Повертає список усіх доступних ролей.
   */
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }
}
