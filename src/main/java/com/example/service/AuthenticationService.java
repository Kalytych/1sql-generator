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
    if (user == null) {
      return false;
    }
    return hashUtil.matches(password, user.getPassword());
  }
}

