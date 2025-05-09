package com.example.service;

import com.example.User;
import com.example.UserRepository;
import com.example.dto.UserLoginRequest;
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

  public boolean login(UserLoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail());
    if (user == null) return false;
    return hashUtil.matches(request.getPassword(), user.getPassword());
  }
}
