package com.example;

import com.example.infrastructure.HikariUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlQueryRepository {

  private final DataSource dataSource = HikariUtil.getDataSource();

  // Визначення типу запиту за SQL текстом (примітивно)
  private int getQueryTypeId(String sql) {
    sql = sql.trim().toLowerCase();
    if (sql.startsWith("select")) return 1;
    else if (sql.startsWith("insert")) return 2;
    else if (sql.startsWith("update")) return 3;
    else if (sql.startsWith("delete")) return 4;
    else return 0; // інші або не визначено
  }

  public void save(String queryText, int queryTypeId, long userId) {
    String sql = "INSERT INTO sql_queries (user_id, query_type_id, query_text) VALUES (?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, userId);
      stmt.setInt(2, queryTypeId);
      stmt.setString(3, queryText);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void save(SqlQuery query) {
    // Якщо queryTypeId не встановлено, визначаємо автоматично
    if (query.getQueryTypeId() == 0) {
      int typeId = getQueryTypeId(query.getQueryText());
      query.setQueryTypeId(typeId);
    }
    save(query.getQueryText(), query.getQueryTypeId(), query.getUserId());
  }

  public List<SqlQuery> findAll() {
    List<SqlQuery> queries = new ArrayList<>();
    String sql = "SELECT * FROM sql_queries";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        SqlQuery query = new SqlQuery();
        query.setId(rs.getInt("id"));
        query.setUserId(rs.getLong("user_id"));
        query.setQueryTypeId(rs.getInt("query_type_id"));
        query.setQueryText(rs.getString("query_text"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
          query.setExecutedAt(timestamp.toLocalDateTime());
        }

        queries.add(query);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return queries;
  }

  public void deleteById(int id) {
    String sql = "DELETE FROM sql_queries WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        System.out.println("SQL запит з таким ID не знайдено.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Виконати UPDATE/INSERT/DELETE
  public int executeUpdate(String sql) throws SQLException {
    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement()) {
      return stmt.executeUpdate(sql);
    }
  }

  // Виконати SELECT і повернути дані з заголовками колонок
  public List<String[]> executeSelect(String sql) throws SQLException {
    List<String[]> result = new ArrayList<>();

    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      ResultSetMetaData meta = rs.getMetaData();
      int columnCount = meta.getColumnCount();

      // Заголовки колонок
      String[] headers = new String[columnCount];
      for (int i = 1; i <= columnCount; i++) {
        headers[i - 1] = meta.getColumnName(i);
      }
      result.add(headers);

      // Дані рядків
      while (rs.next()) {
        String[] row = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
          row[i - 1] = rs.getString(i);
        }
        result.add(row);
      }
    }

    return result;
  }

  // *** Новий метод для автоматичного виконання SQL запиту ***
  public String executeSql(String sql) throws SQLException {
    String trimmed = sql.trim().toLowerCase();
    if (trimmed.startsWith("select")) {
      List<String[]> rows = executeSelect(sql);
      StringBuilder sb = new StringBuilder();

      for (String[] row : rows) {
        for (String cell : row) {
          sb.append(cell).append("\t");
        }
        sb.append("\n");
      }

      return sb.toString();
    } else {
      int affectedRows = executeUpdate(sql);
      return "Виконано запит. Змінено рядків: " + affectedRows;
    }
  }

  // Метод для отримання усіх збережених запитів (alias для findAll)
  public List<SqlQuery> getAllSavedQueries() {
    return findAll();
  }

  // Отримати запити по userId
  public List<SqlQuery> getQueriesByUserId(long userId) {
    List<SqlQuery> queries = new ArrayList<>();
    String sql = "SELECT * FROM sql_queries WHERE user_id = ?";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, userId);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          SqlQuery query = new SqlQuery();
          query.setId(rs.getInt("id"));
          query.setUserId(rs.getLong("user_id"));
          query.setQueryTypeId(rs.getInt("query_type_id"));
          query.setQueryText(rs.getString("query_text"));

          Timestamp timestamp = rs.getTimestamp("created_at");
          if (timestamp != null) {
            query.setExecutedAt(timestamp.toLocalDateTime());
          }

          queries.add(query);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return queries;
  }
}
