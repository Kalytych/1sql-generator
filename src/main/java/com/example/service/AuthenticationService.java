package com.example.service;

import com.example.infrastructure.HashUtil;
import com.example.UserRepository;
import com.example.User;

public class AuthenticationService {
  private final UserRepository userRepository;
  private final HashUtil hashUtil;

  public AuthenticationService(UserRepository userRepository, HashUtil hashUtil) {
    this.userRepository = userRepository;
    this.hashUtil = hashUtil;
  }

  public boolean login(String email, String password) {
    User user = userRepository.findByEmail(email);
    return user != null && HashUtil.matches(password, user.getPassword());

  }
}

