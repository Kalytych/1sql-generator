package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

  @Test
  void testSetAndGet() {
    User user = new User();
    user.setId(1);
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPassword("1234");

    assertEquals(1, user.getId());
    assertEquals("testuser", user.getUsername());
    assertEquals("test@example.com", user.getEmail());
    assertEquals("1234", user.getPassword());
  }
}
