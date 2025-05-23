package com.example.UI;

import com.example.SqlQueryRepository;
import com.example.SqlQuery;
import com.example.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class SqlQueryUI {

  private final SqlQueryRepository sqlQueryRepository = new SqlQueryRepository();

  public void start(Stage primaryStage, User user) {
    VBox root = new VBox(10);
    root.setPadding(new Insets(20));

    Label label = new Label("Виконання SQL-запиту");
    TextArea queryArea = new TextArea();
    queryArea.setPromptText("Введіть SQL-запит...");

    Button executeBtn = new Button("Виконати і зберегти");
    Button backBtn = new Button("Вийти в головне меню");

    TableView<List<String>> resultTable = new TableView<>();
    resultTable.setPrefHeight(200);

    TextArea messageArea = new TextArea();
    messageArea.setEditable(false);
    messageArea.setPrefHeight(100);

    executeBtn.setOnAction(e -> {
      String sql = queryArea.getText().trim();
      if (sql.isEmpty()) {
        showAlert("Помилка", "Поле запиту не може бути порожнім.");
        return;
      }
      try {
        if (sql.toLowerCase().startsWith("select")) {
          // Виконуємо SELECT, виводимо в таблицю
          List<String[]> rows = sqlQueryRepository.executeSelect(sql);
          if (rows.isEmpty()) {
            messageArea.setText("Результат порожній.");
            resultTable.getColumns().clear();
            resultTable.getItems().clear();
          } else {
            // Очистити таблицю і створити колонки за заголовками
            resultTable.getColumns().clear();
            resultTable.getItems().clear();

            String[] headers = rows.get(0);
            for (int i = 0; i < headers.length; i++) {
              final int colIndex = i;
              TableColumn<List<String>, String> col = new TableColumn<>(headers[i]);
              col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIndex)));
              resultTable.getColumns().add(col);
            }
            // Додати дані
            for (int i = 1; i < rows.size(); i++) {
              resultTable.getItems().add(Arrays.asList(rows.get(i)));
            }
            messageArea.clear();
          }
        } else {
          // Виконуємо INSERT/UPDATE/DELETE - повертаємо кількість змінених рядків
          int affected = sqlQueryRepository.executeUpdate(sql);
          messageArea.setText("Успішно виконано. Змінено рядків: " + affected);
          resultTable.getColumns().clear();
          resultTable.getItems().clear();
        }
        // Зберігаємо SQL-запит у базу
        SqlQuery query = new SqlQuery();
        query.setUserId(user.getId());
        query.setQueryText(sql);
        sqlQueryRepository.save(query);
      } catch (Exception ex) {
        messageArea.setText("Помилка виконання SQL:\n" + ex.getMessage());
        resultTable.getColumns().clear();
        resultTable.getItems().clear();
      }
    });

    backBtn.setOnAction(e -> {
      Scene scene = new Scene(new MainMenuUI(primaryStage).getView(), 500, 400);
      primaryStage.setScene(scene);
    });

    root.getChildren().addAll(label, queryArea, executeBtn, resultTable, messageArea, backBtn);
    primaryStage.setScene(new Scene(root, 600, 600));
  }

  private void showAlert(String title, String msg) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
    alert.setTitle(title);
    alert.showAndWait();
  }
}
