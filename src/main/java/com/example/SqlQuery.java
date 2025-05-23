package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlQuery {
  private int id;
  private int queryTypeId;
  private long userId;
  private String queryText;
  private LocalDateTime executedAt;
  private LocalDateTime createdAt;
}
