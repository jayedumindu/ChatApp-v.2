package com.chatwithme;

import com.chatwithme.Controllers.serverController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/chatwithme/FXML/server.fxml"));
        Scene server = new Scene(loader.load());
        primaryStage.setScene(server);
        primaryStage.setTitle("chatwithme™");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image("/com/chatwithme/CSS/img/logo.png"));
        // passing data via the controller
        try {
            serverController controller = loader.getController();
            controller.initData(primaryStage);
        }catch (NullPointerException e){
            System.out.println(e);
        }
        primaryStage.show();

    }
}
