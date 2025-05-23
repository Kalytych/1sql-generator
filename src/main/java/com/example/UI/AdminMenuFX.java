package com.example.UI;

import com.example.service.UserService;
import com.example.SqlQueryRepository;
import com.example.SqlQuery;
import com.example.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class AdminMenuFX  {
  private final UserService userService;
  private final SqlQueryRepository sqlQueryRepository;


  private Stage primaryStage;
  private User currentUser;



  public AdminMenuFX(UserService userService, SqlQueryRepository sqlQueryRepository) {
    this.userService = userService;
    this.sqlQueryRepository = sqlQueryRepository;
  }

  public void start(Stage primaryStage, User user) {
    this.primaryStage = primaryStage;
    this.currentUser = user;

    TabPane tabPane = new TabPane();

    Tab usersTab = new Tab("Користувачі");
    usersTab.setContent(createUsersTabContent());
    usersTab.setClosable(false);

    Tab executeSqlTab = new Tab("Виконати SQL");
    executeSqlTab.setContent(createExecuteSqlTabContent());
    executeSqlTab.setClosable(false);

    Tab savedQueriesTab = new Tab("Збережені запити");
    savedQueriesTab.setContent(createSavedQueriesTabContent());
    savedQueriesTab.setClosable(false);

    tabPane.getTabs().addAll(usersTab, executeSqlTab, savedQueriesTab);

    VBox root = new VBox(10);
    root.setPadding(new Insets(10));
    root.getChildren().addAll(tabPane);

    // Кнопка "Головне меню"
    Button backToMainMenuBtn = new Button("Головне меню");
    backToMainMenuBtn.setOnAction(e -> {
      // Повертаємось до головного меню
      MainMenuUI mainMenu = new MainMenuUI(primaryStage);
      Scene scene = new Scene(mainMenu.getView(), 400, 300);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Головне меню");
    });

    root.getChildren().add(backToMainMenuBtn);

    Scene scene = new Scene(root, 700, 550);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Адмін меню - " + user.getName());
    primaryStage.show();
  }


  private VBox createUsersTabContent() {
    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(10));

    List<User> users = userService.getAllUsers();
    ObservableList<User> usersObservable = FXCollections.observableArrayList(users);

    TableView<User> userTable = new TableView<>(usersObservable);
    TableColumn<User, String> nameCol = new TableColumn<>("Ім'я");
    nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

    TableColumn<User, String> emailCol = new TableColumn<>("Email");
    emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

    userTable.getColumns().addAll(nameCol, emailCol);
    userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button deleteBtn = new Button("Видалити вибраного користувача");
    deleteBtn.setOnAction(e -> {
      User selectedUser = userTable.getSelectionModel().getSelectedItem();
      if (selectedUser != null) {
        boolean deleted = userService.deleteUserById(selectedUser.getId());
        if (deleted) {
          usersObservable.remove(selectedUser);
          showAlert("Успіх", "Користувача видалено.");
        } else {
          showAlert("Помилка", "Не вдалося видалити користувача.");
        }
      } else {
        showAlert("Помилка", "Оберіть користувача для видалення.");
      }
    });

    vbox.getChildren().addAll(userTable, deleteBtn);
    return vbox;
  }

  private VBox createExecuteSqlTabContent() {
    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(10));

    TextArea sqlInput = new TextArea();
    sqlInput.setPromptText("Введіть SQL-запит...");

    Button executeBtn = new Button("Виконати");
    TextArea outputArea = new TextArea();
    outputArea.setEditable(false);

    executeBtn.setOnAction(e -> {
      String sql = sqlInput.getText();
      try {
        String result = sqlQueryRepository.executeSql(sql);
        outputArea.setText(result);
      } catch (Exception ex) {
        outputArea.setText("Помилка виконання SQL:\n" + ex.getMessage());
      }
    });

    vbox.getChildren().addAll(new Label("SQL-запит:"), sqlInput, executeBtn, new Label("Результат:"), outputArea);
    return vbox;
  }

  private VBox createSavedQueriesTabContent() {
    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(10));

    List<SqlQuery> savedQueries = sqlQueryRepository.findAll(); // замість getAllSavedQueries()
    ObservableList<SqlQuery> queriesObservable = FXCollections.observableArrayList(savedQueries);

    ListView<SqlQuery> queryListView = new ListView<>(queriesObservable);
    queryListView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(SqlQuery item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? "" : item.getQueryText());
      }
    });

    TextArea outputArea = new TextArea();
    outputArea.setEditable(false);

    Button executeSavedBtn = new Button("Виконати вибраний запит");
    executeSavedBtn.setOnAction(e -> {
      SqlQuery selected = queryListView.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          String result = sqlQueryRepository.executeSql(selected.getQueryText());
          outputArea.setText(result);
        } catch (Exception ex) {
          outputArea.setText("Помилка виконання SQL:\n" + ex.getMessage());
        }
      } else {
        showAlert("Помилка", "Оберіть запит для виконання.");
      }
    });

    vbox.getChildren().addAll(new Label("Збережені SQL-запити:"), queryListView, executeSavedBtn, new Label("Результат:"), outputArea);
    return vbox;
  }


  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
    alert.setTitle(title);
    alert.showAndWait();
  }
}
