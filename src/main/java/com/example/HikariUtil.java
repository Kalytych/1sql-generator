package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariUtil {
  private static final HikariDataSource dataSource;

  static {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:sqlite:identifier.sqlite");
    config.setMaximumPoolSize(5);
    dataSource = new HikariDataSource(config);
  }

  public static DataSource getDataSource() {
    return dataSource;
  }
}
