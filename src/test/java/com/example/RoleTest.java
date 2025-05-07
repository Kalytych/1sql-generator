package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

  @Test
  void testRoleFields() {
    Role role = new Role();
    role.setId(2);
    role.setName("ADMIN");

    assertEquals(2, role.getId());
    assertEquals("ADMIN", role.getName());
  }
}
