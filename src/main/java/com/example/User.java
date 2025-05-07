package com.example;

import lombok.Data;

@Data
public class User {
  private int id;
  private String username;
  private String email;
  private String password;

  public void setId(int id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
