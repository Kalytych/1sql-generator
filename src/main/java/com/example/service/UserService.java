package com.example.service;

import com.example.User;
import com.example.UserRepository;
import com.example.infrastructure.HashUtil;

import java.util.List;

public class UserService {
  private final UserRepository userRepository;
  private final HashUtil hashUtil;

  private static final int DEFAULT_USER_ROLE_ID = 2;

  public UserService(UserRepository userRepository, HashUtil hashUtil) {
    this.userRepository = userRepository;
    this.hashUtil = hashUtil;
  }

  // Метод, який повертає список всіх користувачів (синонім для findAll)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Існуючий метод — теж повертає список
  public List<User> findAll() {
    return userRepository.findAll();
  }

  public void printAllUsers() {
    List<User> users = findAll();
    if (users.isEmpty()) {
      System.out.println("Немає жодного користувача.");
    } else {
      System.out.println("\n===== Список користувачів =====");
      for (User user : users) {
        System.out.printf("ID: %d | Ім'я: %s | Email: %s | Роль ID: %d%n",
            user.getId(), user.getName(), user.getEmail(), user.getRoleId());
      }
    }
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User findByUsername(String username) {
    return userRepository.findByName(username);
  }

  public boolean save(User user) {
    if (userRepository.findByEmail(user.getEmail()) != null) {
      System.out.println("Користувач з таким email вже існує.");
      return false;
    }

    if (user.getRoleId() == 0) {
      user.setRoleId(DEFAULT_USER_ROLE_ID);
    }

    String hashedPassword = hashUtil.hashPassword(user.getPassword());
    user.setPassword(hashedPassword);

    userRepository.save(user);
    return true;
  }

  public boolean deleteByUsernameAndPassword(String username, String password) {
    User user = userRepository.findByName(username);
    if (user != null && hashUtil.verifyPassword(password, user.getPassword())) {
      userRepository.deleteById(user.getId());
      return true;
    }
    return false;
  }

  // Метод для видалення користувача за id
  public boolean deleteUserById(long id) {
    User user = userRepository.findById(id);
    if (user != null) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }

  public void deleteById(long id) {
    userRepository.deleteById(id);
    System.out.println("Користувача з ID " + id + " видалено.");
  }

  public boolean deleteByUsername(String username) {
    User user = userRepository.findByName(username);
    if (user != null) {
      userRepository.deleteById(user.getId());
      return true;
    }
    return false;
  }
}
