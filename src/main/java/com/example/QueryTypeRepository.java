package com.example;

import com.example.infrastructure.HikariUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryTypeRepository {

  public QueryTypeRepository() {
    createTableIfNotExists();
  }

  private void createTableIfNotExists() {
    String sql = """
            CREATE TABLE IF NOT EXISTS query_types (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE
            );
        """;
    try (Connection conn = HikariUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.execute();
    } catch (Exception e) {
      System.out.println("❌ Не вдалося створити таблицю query_types: " + e.getMessage());
    }
  }

  /**
   * Знаходить тип запиту або створює новий, якщо його ще не існує.
   *
   * @param typeName назва типу (наприклад: SELECT, INSERT)
   * @return id типу
   */
  public int findOrCreateType(String typeName) {
    try (Connection conn = HikariUtil.getConnection()) {
      // Спроба знайти
      String selectSql = "SELECT id FROM query_types WHERE LOWER(name) = LOWER(?)";
      try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
        selectStmt.setString(1, typeName);
        ResultSet rs = selectStmt.executeQuery();
        if (rs.next()) {
          return rs.getInt("id");
        }
      }

      // Якщо не знайдено — вставити
      String insertSql = "INSERT INTO query_types(name) VALUES(?)";
      try (PreparedStatement insertStmt = conn.prepareStatement(insertSql,
          PreparedStatement.RETURN_GENERATED_KEYS)) {
        insertStmt.setString(1, typeName);
        insertStmt.executeUpdate();
        ResultSet rs = insertStmt.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    } catch (Exception e) {
      System.out.println("❌ Помилка при findOrCreateType: " + e.getMessage());
    }

    return -1; // Якщо не вдалося знайти чи створити
  }
}
