package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT user_id, username, email, password_hash FROM user");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password_hash"));
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public User findByEmail(String email) {
    String sql = "SELECT user_id, username, email, password_hash FROM user WHERE email = ?";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, email);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          User user = new User();
          user.setId(rs.getInt("user_id"));
          user.setUsername(rs.getString("username"));
          user.setEmail(rs.getString("email"));
          user.setPassword(rs.getString("password_hash"));
          return user;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void save(User user) {
    String sql = "INSERT INTO user (username, email, password_hash) VALUES (?, ?, ?)";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, user.getPassword());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
