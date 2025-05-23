package com.example.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    Scene scene = new Scene(new MainMenuUI(primaryStage).getView(), 500, 400);
    primaryStage.setTitle("SQL Generator App");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
