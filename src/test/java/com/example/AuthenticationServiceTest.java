package com.example;

import com.example.service.AuthenticationService;
import com.example.infrastructure.HashUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

  @Test
  void testSuccessfulLogin() {
    // Arrange
    UserRepository mockRepo = mock(UserRepository.class);
    HashUtil mockHashUtil = mock(HashUtil.class);
    AuthenticationService service = new AuthenticationService(mockRepo, mockHashUtil);

    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("hashed");

    when(mockRepo.findByEmail("test@example.com")).thenReturn(user);
    when(mockHashUtil.matches("password", "hashed")).thenReturn(true);

    // Act
    boolean result = service.login("test@example.com", "password");

    // Assert
    assertTrue(result);
  }

  @Test
  void testLoginFailsWithInvalidPassword() {
    UserRepository mockRepo = mock(UserRepository.class);
    HashUtil mockHashUtil = mock(HashUtil.class);
    AuthenticationService service = new AuthenticationService(mockRepo, mockHashUtil);

    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("hashed");

    when(mockRepo.findByEmail("test@example.com")).thenReturn(user);
    when(mockHashUtil.matches("wrong", "hashed")).thenReturn(false);

    assertFalse(service.login("test@example.com", "wrong"));
  }

  @Test
  void testLoginFailsWhenUserNotFound() {
    UserRepository mockRepo = mock(UserRepository.class);
    HashUtil mockHashUtil = mock(HashUtil.class);
    AuthenticationService service = new AuthenticationService(mockRepo, mockHashUtil);

    when(mockRepo.findByEmail("notfound@example.com")).thenReturn(null);

    assertFalse(service.login("notfound@example.com", "password"));
  }
}
