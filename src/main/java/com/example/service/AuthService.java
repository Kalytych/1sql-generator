package com.example.service;

import com.example.User;
import com.example.UserRepository;
import com.example.infrastructure.HashUtil;
import com.example.infrastructure.MailService;

public class AuthService {
  private final UserRepository userRepository;
  private final HashUtil hashUtil;
  private final MailService mailService;

  public AuthService(UserRepository userRepository, HashUtil hashUtil, MailService mailService) {
    this.userRepository = userRepository;
    this.hashUtil = hashUtil;
    this.mailService = mailService;
  }

  public boolean register(String username, String email, String password) {
    if (userRepository.findByEmail(email) != null) {
      return false; // Email already in use
    }

    String hashedPassword = hashUtil.hash(password);
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(hashedPassword);

    userRepository.save(user);
    mailService.send(email, "Welcome!", "Thank you for registering, " + username + "!");

    return true;
  }

  public boolean authenticate(String email, String password) {
    User user = userRepository.findByEmail(email);
    if (user == null) return false;

    return hashUtil.matches(password, user.getPassword());
  }
}
