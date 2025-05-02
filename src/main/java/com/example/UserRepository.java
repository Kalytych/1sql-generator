package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }
}
