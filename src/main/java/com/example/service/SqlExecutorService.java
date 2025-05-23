package com.example.service;

import com.example.infrastructure.HikariUtil;
import com.example.SqlQueryRepository;
import com.example.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.example.QueryTypeRepository;

public class SqlExecutorService {

  private final SqlQueryRepository sqlQueryRepository = new SqlQueryRepository();
  private final QueryTypeRepository queryTypeRepository = new QueryTypeRepository();

  public void executeAndSave(String sql, User currentUser) {
    try (Connection conn = HikariUtil.getDataSource().getConnection();
        Statement stmt = conn.createStatement()) {

      stmt.execute(sql);
      System.out.println("✅ Запит виконано.");

      // Визначаємо тип SQL-запиту (SELECT, INSERT, UPDATE, DELETE тощо)
      String queryTypeName = getQueryTypeName(sql);
      int queryTypeId = queryTypeRepository.findOrCreateType(queryTypeName);

      // Зберігаємо запит у БД
      sqlQueryRepository.save(sql, queryTypeId, currentUser.getId());

    } catch (SQLException e) {
      System.out.println("❌ Помилка виконання: " + e.getMessage());
    }
  }

  private String getQueryTypeName(String query) {
    String[] tokens = query.trim().split("\\s+");
    return tokens.length > 0 ? tokens[0].toUpperCase() : "UNKNOWN";
  }
}
