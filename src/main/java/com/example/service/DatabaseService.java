package com.example.service;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.infrastructure.HikariUtil;

/**
 * Сервіс для виконання SQL-запитів до бази даних.
 * <p>
 * Використовує пул підключень HikariCP через {@link HikariUtil}.
 */
public class DatabaseService {
  private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

  /**
   * Виконує переданий SQL-запит.
   * <p>
   * Якщо запит повертає результат, логують успішне виконання.
   * Якщо результату немає, логують попередження.
   * У випадку помилки виконується логування помилки.
   *
   * @param queryText SQL-запит для виконання
   */
  public void executeQuery(String queryText) {
    try (Connection connection = HikariUtil.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(queryText);
      boolean isExecuted = stmt.execute();

      if (isExecuted) {
        logger.info("SQL query executed successfully: " + queryText);
      } else {
        logger.warn("No results returned for the query: " + queryText);
      }
    } catch (SQLException e) {
      logger.error("Error executing SQL query", e);
    }
  }
}
