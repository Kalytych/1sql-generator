package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    String sql = """
        SELECT u.id, u.name, u.email, u.password, u.role_id, r.name AS role_name
        FROM users u
        JOIN roles r ON u.role_id = r.id
        """;

    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public User findByEmail(String email) {
    String sql = """
        SELECT u.id, u.name, u.email, u.password, u.role_id, r.name AS role_name
        FROM users u
        JOIN roles r ON u.role_id = r.id
        WHERE u.email = ?
        """;

    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, email);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          User user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
          user.setEmail(rs.getString("email"));
          user.setPassword(rs.getString("password"));
          user.setRoleId(rs.getInt("role_id"));
          return user;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public User findByName(String name) {
    String sql = """
        SELECT u.id, u.name, u.email, u.password, u.role_id
        FROM users u
        WHERE u.name = ?
        """;
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, name);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          User user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
          user.setEmail(rs.getString("email"));
          user.setPassword(rs.getString("password"));
          user.setRoleId(rs.getInt("role_id"));
          return user;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * Знаходить користувача за ID.
   *
   * @param id ідентифікатор користувача
   * @return об'єкт {@link User}, якщо знайдено; інакше {@code null}
   */
  public User findById(long id) {
    String sql = """
        SELECT u.id, u.name, u.email, u.password, u.role_id
        FROM users u
        WHERE u.id = ?
        """;
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          User user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
          user.setEmail(rs.getString("email"));
          user.setPassword(rs.getString("password"));
          user.setRoleId(rs.getInt("role_id"));
          return user;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void save(User user) {
    String sql = "INSERT INTO users (name, email, password, role_id) VALUES (?, ?, ?, ?)";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, user.getName());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, user.getPassword());
      stmt.setInt(4, user.getRoleId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteById(long id) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean deleteByUsername(String name) {
    String sql = "DELETE FROM users WHERE name = ?";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, name);
      int affectedRows = stmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // За потреби синонім:
  public List<User> getAllUsers() {
    return findAll();
  }
}
