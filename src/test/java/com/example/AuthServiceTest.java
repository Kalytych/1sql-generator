package com.example;

import com.example.infrastructure.HashUtil;
import com.example.infrastructure.MailService;
import com.example.service.AuthService;
import com.example.dto.UserLoginRequest;
import com.example.User;
import com.example.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

  private UserRepository userRepository;
  private HashUtil hashUtil;
  private MailService mailService;
  private AuthService authService;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    hashUtil = mock(HashUtil.class);
    mailService = mock(MailService.class);

    authService = new AuthService(userRepository, hashUtil, mailService);
  }

  @Test
  void testLoginSuccess() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("hashed");

    when(userRepository.findByEmail("test@example.com")).thenReturn(user);
    when(hashUtil.matches("password", "hashed")).thenReturn(true);

    UserLoginRequest request = new UserLoginRequest("test@example.com", "password");

    boolean result = authService.login(request);

    assertTrue(result);
  }
}
