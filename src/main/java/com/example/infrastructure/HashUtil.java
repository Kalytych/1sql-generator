package com.example.infrastructure;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtil {

  public static String hash(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }

  public boolean matches(String password, String hashedPassword) {
    return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
  }
}
