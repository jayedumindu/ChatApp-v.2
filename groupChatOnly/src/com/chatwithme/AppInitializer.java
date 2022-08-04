package com.chatwithme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Scene server = new Scene(FXMLLoader.load(this.getClass().getClassLoader().getResource("com/chatwithme/FXMl/server.fxml")));
        primaryStage.setScene(server);
        primaryStage.setTitle("Server App");
        primaryStage.centerOnScreen();
        primaryStage.show();

    }
}
