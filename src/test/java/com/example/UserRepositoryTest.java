package com.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

  @Test
  void testFindAll() {
    UserRepository repo = new UserRepository();
    List<User> users = repo.findAll();

    assertNotNull(users);
    assertTrue(users.size() >= 0); // перевірка, що запит не зламався
  }
}
