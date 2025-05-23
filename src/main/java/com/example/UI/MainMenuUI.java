package com.example.UI;

import com.example.*;
import com.example.infrastructure.HashUtil;
import com.example.service.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainMenuUI {
  private final VBox root = new VBox(10);
  private final Stage primaryStage;

  private final UserRepository userRepository = new UserRepository();
  private final HashUtil hashUtil = new HashUtil();
  private final MailService mailService = new MailService();
  private final VerificationService verificationService = new VerificationService(mailService);
  private final RoleRepository roleRepository = new RoleRepository();
  private final RegistrationService registrationService = new RegistrationService(userRepository, hashUtil, verificationService, roleRepository);
  private final AuthenticationService authService = new AuthenticationService(userRepository, hashUtil);
  private final UserService userService = new UserService(userRepository, hashUtil);
  private final SqlQueryRepository sqlQueryRepository = new SqlQueryRepository();
  private final QueryTypeRepository queryTypeRepository = new QueryTypeRepository();

  public MainMenuUI(Stage primaryStage) {
    this.primaryStage = primaryStage;
    root.setPadding(new Insets(20));

    Label title = new Label("Головне меню");
    title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Button registerBtn = new Button("Реєстрація");
    Button loginBtn = new Button("Вхід");
    Button deleteBtn = new Button("Видалити користувача");
    Button exitBtn = new Button("Вийти");

    registerBtn.setMaxWidth(Double.MAX_VALUE);
    loginBtn.setMaxWidth(Double.MAX_VALUE);
    deleteBtn.setMaxWidth(Double.MAX_VALUE);
    exitBtn.setMaxWidth(Double.MAX_VALUE);

    registerBtn.setOnAction(e -> showRegistrationForm());
    loginBtn.setOnAction(e -> showLoginForm());
    deleteBtn.setOnAction(e -> showDeleteForm());
    exitBtn.setOnAction(e -> primaryStage.close());

    root.getChildren().addAll(title, registerBtn, loginBtn, deleteBtn, exitBtn);
  }

  public VBox getView() {
    return root;
  }

  private void showRegistrationForm() {
    Stage dialog = new Stage();
    VBox form = new VBox(10);
    form.setPadding(new Insets(15));

    TextField nameField = new TextField();
    TextField emailField = new TextField();
    PasswordField passField = new PasswordField();
    TextField codeField = new TextField();

    ComboBox<String> roleCombo = new ComboBox<>();
    roleCombo.getItems().addAll("Адмін", "Користувач");

    Button sendCodeBtn = new Button("Надіслати код");
    Button submit = new Button("Зареєструватися");

    // Кнопка для надсилання коду
    sendCodeBtn.setOnAction(e -> {
      String email = emailField.getText();
      if (email == null || !email.contains("@")) {
        showAlert("Помилка", "Введіть коректний email перед надсиланням коду.");
        return;
      }

      try {
        verificationService.sendCode(email);  // ← Надсилання коду
        showAlert("Успіх", "Код підтвердження надіслано на вашу пошту.");
      } catch (Exception ex) {
        showAlert("Помилка", "Не вдалося надіслати код: " + ex.getMessage());
      }
    });

    // Кнопка реєстрації
    submit.setOnAction(e -> {
      String name = nameField.getText();
      String email = emailField.getText();
      String password = passField.getText();
      String code = codeField.getText();
      String selectedRole = roleCombo.getValue();

      if (selectedRole == null || selectedRole.isEmpty()) {
        showAlert("Помилка", "Оберіть роль.");
        return;
      }

      int roleId = selectedRole.equals("Адмін") ? 1 : 2;

      try {
        registrationService.register(name, email, password, code, roleId);
        showAlert("Успіх", "Реєстрація успішна!");
        dialog.close();
      } catch (Exception ex) {
        showAlert("Помилка", ex.getMessage());
      }
    });

    form.getChildren().addAll(
        new Label("Ім’я користувача:"), nameField,
        new Label("Email:"), emailField, sendCodeBtn,
        new Label("Пароль:"), passField,
        new Label("Код підтвердження:"), codeField,
        new Label("Роль:"), roleCombo,
        submit
    );

    dialog.setScene(new Scene(form));
    dialog.setTitle("Реєстрація");
    dialog.show();
  }



  private void showLoginForm() {
    Stage dialog = new Stage();
    VBox form = new VBox(10);
    form.setPadding(new Insets(15));

    TextField emailField = new TextField();
    PasswordField passField = new PasswordField();
    Button login = new Button("Увійти");

    login.setOnAction(e -> {
      try {
        User user = authService.login(emailField.getText(), passField.getText());
        if (user != null) {
          dialog.close();
          if (user.getRoleId() == 1) {
            new AdminMenuFX(userService, sqlQueryRepository).start(primaryStage, user);
          } else {
            new SqlQueryUI().start(primaryStage, user);
          }
        } else {
          showAlert("Помилка", "Невірний email або пароль!");
        }
      } catch (Exception ex) {
        showAlert("Помилка", ex.getMessage());
      }
    });

    form.getChildren().addAll(
        new Label("Email:"), emailField,
        new Label("Пароль:"), passField,
        login
    );

    dialog.setScene(new Scene(form));
    dialog.setTitle("Вхід");
    dialog.show();
  }

  private void showDeleteForm() {
    Stage dialog = new Stage();
    VBox form = new VBox(10);
    form.setPadding(new Insets(15));

    TextField usernameField = new TextField();
    PasswordField passwordField = new PasswordField();
    Button deleteBtn = new Button("Видалити");

    deleteBtn.setOnAction(e -> {
      boolean deleted = userService.deleteByUsernameAndPassword(usernameField.getText(), passwordField.getText());
      if (deleted) {
        showAlert("Успіх", "Користувача видалено.");
        dialog.close();
      } else {
        showAlert("Помилка", "Користувача не знайдено або пароль невірний.");
      }
    });

    form.getChildren().addAll(
        new Label("Ім’я користувача:"), usernameField,
        new Label("Пароль:"), passwordField,
        deleteBtn
    );

    dialog.setScene(new Scene(form));
    dialog.setTitle("Видалення користувача");
    dialog.show();
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
    alert.setTitle(title);
    alert.showAndWait();
  }
}
