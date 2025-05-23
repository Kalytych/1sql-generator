package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторій для роботи з таблицею ролей (roles) у базі даних.
 * <p>Забезпечує отримання списку ролей та пошук ролі за ідентифікатором.</p>
 */
public class RoleRepository {

  /**
   * Повертає список усіх ролей з бази даних.
   *
   * @return список об'єктів {@link Role}, або порожній список якщо ролі не знайдено
   */
  public List<Role> findAll() {
    List<Role> roles = new ArrayList<>();
    String sql = "SELECT * FROM roles";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Role role = new Role();
        role.setId(rs.getInt("id"));
        role.setName(rs.getString("name"));
        roles.add(role);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return roles;
  }

  /**
   * Знаходить роль за унікальним ідентифікатором.
   *
   * @param id унікальний ідентифікатор ролі
   * @return об'єкт {@link Role} якщо роль знайдена, або {@code null} якщо роль не існує
   */
  public Role findById(int id) {
    String sql = "SELECT * FROM roles WHERE id = ?";
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          Role role = new Role();
          role.setId(rs.getInt("id"));
          role.setName(rs.getString("name"));
          return role;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
