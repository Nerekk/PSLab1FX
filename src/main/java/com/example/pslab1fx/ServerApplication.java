package com.example.pslab1fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    private static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return ServerApplication.primaryStage;
    }
    private static void setPrimaryStage(Stage stage) {
        ServerApplication.primaryStage = stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Echo server");
        stage.setScene(scene);
        stage.show();

        primaryStage.setOnCloseRequest(event -> {
            closeApp();
        });
    }

    private void closeApp() {
        System.out.println("Zamykanie aplikacji...");
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}