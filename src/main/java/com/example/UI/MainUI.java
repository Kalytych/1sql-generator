package com.example.UI;

import java.util.List;
import com.example.service.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.example.UserRepository;
import com.example.infrastructure.HashUtil;
import com.example.RoleRepository;
import com.example.SqlQueryRepository;
import com.example.QueryTypeRepository;
import com.example.MigrationRunner;
import com.example.User;
import com.example.SqlQuery;


 public class MainUI extends Application {

  private UserRepository userRepository = new UserRepository();
  private HashUtil hashUtil = new HashUtil();
  private MailService mailService = new MailService();
  private VerificationService verificationService = new VerificationService(mailService);
  private RoleRepository roleRepository = new RoleRepository();
  private RegistrationService registrationService = new RegistrationService(userRepository,
      hashUtil, verificationService, roleRepository);
  private AuthenticationService authService = new AuthenticationService(userRepository, hashUtil);
  private UserService userService = new UserService(userRepository, hashUtil);
  private SqlQueryRepository sqlQueryRepository = new SqlQueryRepository();
  private QueryTypeRepository queryTypeRepository = new QueryTypeRepository();

  private Stage primaryStage;

  public static void main(String[] args) {
    MigrationRunner.runMigrations();
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    showMainMenu();
  }

  private void showMainMenu() {
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label title = new Label("ГОЛОВНЕ МЕНЮ");
    Button btnRegister = new Button("Реєстрація");
    Button btnLogin = new Button("Вхід");
    Button btnDeleteUser = new Button("Видалити користувача");
    Button btnExit = new Button("Вийти");

    root.getChildren().addAll(title, btnRegister, btnLogin, btnDeleteUser, btnExit);

    btnRegister.setOnAction(e -> showRegistrationForm());
    btnLogin.setOnAction(e -> showLoginForm());
    btnDeleteUser.setOnAction(e -> showDeleteUserForm());
    btnExit.setOnAction(e -> primaryStage.close());

    Scene scene = new Scene(root, 300, 250);
    primaryStage.setTitle("SQL Генератор - Головне меню");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

   private void showRegistrationForm() {
     VBox root = new VBox(10);
     root.setPadding(new Insets(15));

     Label lblName = new Label("Ім’я користувача:");
     TextField tfName = new TextField();

     Label lblEmail = new Label("Email:");
     TextField tfEmail = new TextField();

     Label lblPassword = new Label("Пароль:");
     PasswordField pfPassword = new PasswordField();

     Label lblCode = new Label("Код підтвердження:");
     TextField tfCode = new TextField();

     Label lblRole = new Label("Роль:");
     ComboBox<String> cbRole = new ComboBox<>();
     cbRole.getItems().addAll("Користувач", "Адмін");

     Button btnSubmit = new Button("Зареєструватися");
     Button btnBack = new Button("Назад");

     Label lblMessage = new Label();

     root.getChildren().addAll(lblName, tfName, lblEmail, tfEmail, lblPassword, pfPassword,
         lblCode, tfCode, lblRole, cbRole, btnSubmit, btnBack, lblMessage);

     btnSubmit.setOnAction(e -> {
       String username = tfName.getText();
       String email = tfEmail.getText();
       String password = pfPassword.getText();
       String code = tfCode.getText();
       String roleName = cbRole.getValue();

       if (roleName == null) {
         lblMessage.setText("Оберіть роль.");
         return;
       }

       int roleId = roleName.equals("Адмін") ? 1 : 2;

       try {
         registrationService.register(username, email, password, code, roleId);
         lblMessage.setText("Реєстрація успішна!");
       } catch (Exception ex) {
         lblMessage.setText("Помилка: " + ex.getMessage());
       }
     });

     btnBack.setOnAction(e -> showMainMenu());

     Scene scene = new Scene(root, 400, 450);
     primaryStage.setScene(scene);
   }


   private void showLoginForm() {
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblEmail = new Label("Email:");
    TextField tfEmail = new TextField();

    Label lblPassword = new Label("Пароль:");
    PasswordField pfPassword = new PasswordField();

    Button btnLogin = new Button("Увійти");
    Button btnBack = new Button("Назад");

    Label lblMessage = new Label();

    root.getChildren()
        .addAll(lblEmail, tfEmail, lblPassword, pfPassword, btnLogin, btnBack, lblMessage);

    btnLogin.setOnAction(e -> {
      String email = tfEmail.getText();
      String password = pfPassword.getText();

      User user = authService.login(email, password);
      if (user != null) {
        lblMessage.setText("Успішний вхід! Вітаємо, " + user.getName());

        if (user.getRoleId() == 1) {
          showAdminMenu(user);
        } else {
          showUserSqlMenu(user);
        }
      } else {
        lblMessage.setText("Невірний email або пароль!");
      }
    });

    btnBack.setOnAction(e -> showMainMenu());

    Scene scene = new Scene(root, 350, 300);
    primaryStage.setScene(scene);
  }

  private void showDeleteUserForm() {
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblUsername = new Label("Ім’я користувача:");
    TextField tfUsername = new TextField();

    Label lblPassword = new Label("Пароль:");
    PasswordField pfPassword = new PasswordField();

    Button btnDelete = new Button("Видалити");
    Button btnBack = new Button("Назад");

    Label lblMessage = new Label();

    root.getChildren()
        .addAll(lblUsername, tfUsername, lblPassword, pfPassword, btnDelete, btnBack, lblMessage);

    btnDelete.setOnAction(e -> {
      String username = tfUsername.getText();
      String password = pfPassword.getText();

      boolean deleted = userService.deleteByUsernameAndPassword(username, password);
      if (deleted) {
        lblMessage.setText("Користувача видалено.");
      } else {
        lblMessage.setText("Користувача не знайдено або пароль невірний.");
      }
    });

    btnBack.setOnAction(e -> showMainMenu());

    Scene scene = new Scene(root, 350, 300);
    primaryStage.setScene(scene);
  }

  private void showAdminMenu(User user) {
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblTitle = new Label("Меню Адміністратора: " + user.getName());

    Button btnExecuteSql = new Button("Виконати SQL запит");
    Button btnDeleteUser = new Button("Видалити користувача");
    Button btnDeleteSql = new Button("Видалити SQL запит");
    Button btnLogout = new Button("Вийти");

    Label lblMessage = new Label();

    root.getChildren()
        .addAll(lblTitle, btnExecuteSql, btnDeleteUser, btnDeleteSql, btnLogout, lblMessage);

    btnExecuteSql.setOnAction(e -> executeSqlQueryDialog(lblMessage));
    btnDeleteUser.setOnAction(e -> deleteUserDialog(lblMessage));
    btnDeleteSql.setOnAction(e -> deleteSqlQueryDialog(lblMessage));
    btnLogout.setOnAction(e -> showMainMenu());

    Scene scene = new Scene(root, 400, 350);
    primaryStage.setScene(scene);
  }

  private void executeSqlQueryDialog(Label lblMessage) {
    Stage dialog = new Stage();
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lbl = new Label("Введіть SQL запит:");
    TextArea taQuery = new TextArea();

    Button btnExecute = new Button("Виконати");
    Button btnClose = new Button("Закрити");

    Label lblResult = new Label();

    root.getChildren().addAll(lbl, taQuery, btnExecute, btnClose, lblResult);

    btnExecute.setOnAction(e -> {
      String query = taQuery.getText();
      try {
        // Виконання SQL (аналог executeSqlQuery)
        int rowsAffected = sqlQueryRepository.executeUpdate(query);
        lblResult.setText("Операція успішна. Змінено рядків: " + rowsAffected);
        lblMessage.setText("");
      } catch (Exception ex) {
        lblResult.setText("Помилка виконання: " + ex.getMessage());
      }
    });

    btnClose.setOnAction(e -> dialog.close());

    Scene scene = new Scene(root, 400, 300);
    dialog.setScene(scene);
    dialog.setTitle("Виконати SQL запит");
    dialog.show();
  }

  private void deleteUserDialog(Label lblMessage) {
    Stage dialog = new Stage();
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblUsername = new Label("Ім’я користувача:");
    TextField tfUsername = new TextField();

    Button btnDelete = new Button("Видалити");
    Button btnClose = new Button("Закрити");

    Label lblResult = new Label();

    root.getChildren().addAll(lblUsername, tfUsername, btnDelete, btnClose, lblResult);

    btnDelete.setOnAction(e -> {
      String username = tfUsername.getText();
      boolean deleted = userService.deleteByUsername(username);
      lblResult.setText(deleted ? "Користувача видалено." : "Користувача не знайдено.");
      lblMessage.setText("");
    });

    btnClose.setOnAction(e -> dialog.close());

    Scene scene = new Scene(root, 300, 200);
    dialog.setScene(scene);
    dialog.setTitle("Видалити користувача");
    dialog.show();
  }

  private void deleteSqlQueryDialog(Label lblMessage) {
    Stage dialog = new Stage();
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblId = new Label("ID SQL запиту:");
    TextField tfId = new TextField();

    Button btnDelete = new Button("Видалити");
    Button btnClose = new Button("Закрити");

    Label lblResult = new Label();

    root.getChildren().addAll(lblId, tfId, btnDelete, btnClose, lblResult);

    btnDelete.setOnAction(e -> {
      try {
        int id = Integer.parseInt(tfId.getText());
        sqlQueryRepository.deleteById(id);
        lblResult.setText("SQL запит видалено, якщо такий існував.");
        lblMessage.setText("");
      } catch (NumberFormatException ex) {
        lblResult.setText("Невірний формат ID.");
      }
    });

    btnClose.setOnAction(e -> dialog.close());

    Scene scene = new Scene(root, 300, 200);
    dialog.setScene(scene);
    dialog.setTitle("Видалити SQL запит");
    dialog.show();
  }

  private void showUserSqlMenu(User user) {
    Stage dialog = new Stage();
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblTitle = new Label("Меню Користувача: " + user.getName());

    Button btnAddQuery = new Button("Додати SQL запит");
    Button btnViewQueries = new Button("Переглянути SQL запити");
    Button btnLogout = new Button("Вийти");

    Label lblMessage = new Label();

    root.getChildren().addAll(lblTitle, btnAddQuery, btnViewQueries, btnLogout, lblMessage);

    btnAddQuery.setOnAction(e -> addSqlQueryDialog(user, lblMessage));
    btnViewQueries.setOnAction(e -> viewSqlQueriesDialog(user));
    btnLogout.setOnAction(e -> {
      dialog.close();
      showMainMenu();
    });

    Scene scene = new Scene(root, 400, 300);
    dialog.setScene(scene);
    dialog.setTitle("Меню користувача");
    dialog.show();
  }

  private void addSqlQueryDialog(User user, Label lblMessage) {
    Stage dialog = new Stage();
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblQuery = new Label("SQL запит:");
    TextArea taQuery = new TextArea();

    Button btnAdd = new Button("Додати");
    Button btnClose = new Button("Закрити");

    Label lblResult = new Label();

    root.getChildren().addAll(lblQuery, taQuery, btnAdd, btnClose, lblResult);

    btnAdd.setOnAction(e -> {
      String query = taQuery.getText();
      try {
        int queryTypeId = 1; // або отримай динамічно, якщо потрібно
        sqlQueryRepository.save(query, queryTypeId, user.getId());
        lblResult.setText("Запит додано!");
        lblMessage.setText("");
      } catch (Exception ex) {
        lblResult.setText("Помилка: " + ex.getMessage());
      }
    });

    btnClose.setOnAction(e -> dialog.close());

    Scene scene = new Scene(root, 400, 300);
    dialog.setScene(scene);
    dialog.setTitle("Додати SQL запит");
    dialog.show();
  }

  private void viewSqlQueriesDialog(User user) {
    Stage dialog = new Stage();
    VBox root = new VBox(10);
    root.setPadding(new Insets(15));

    Label lblTitle = new Label("Ваші SQL запити");

    ListView<String> listView = new ListView<>();
    List<SqlQuery> queries = sqlQueryRepository.getQueriesByUserId(user.getId());

    // Додаємо у ListView текст кожного запиту
    for (SqlQuery query : queries) {
      listView.getItems().add(query.getQueryText());
    }


    Button btnClose = new Button("Закрити");

    root.getChildren().addAll(lblTitle, listView, btnClose);

    btnClose.setOnAction(e -> dialog.close());

    Scene scene = new Scene(root, 500, 400);
    dialog.setScene(scene);
    dialog.setTitle("Перегляд SQL запитів");
    dialog.show();
  }
}

