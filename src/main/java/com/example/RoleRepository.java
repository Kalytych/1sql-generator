package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {

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
