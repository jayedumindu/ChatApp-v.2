package com.chatwithme.Controllers;

import com.chatwithme.Thread.Flusher;
import com.chatwithme.util.Server;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;

public class serverController {

    @FXML
    public JFXTextField clientName;
    public ImageView startBtn;
    public Text alert;
    Stage stage;

    Server server;
    Thread mainThread;
    Socket localSocket;

    public static HashMap<Integer,DataOutputStream> clients = new HashMap<>();

    public void initialize() throws IOException {

        // open up the server
        server = new Server(8080, 5);

        mainThread = new Thread(() -> {
            // always waits for a client's request
            while (true) {
                try {
                    localSocket = server.accept();
                    Timer timer = new Timer();
                    timer.schedule(new Flusher(new DataInputStream(localSocket.getInputStream()),timer),0,2000);
                    clients.put(localSocket.getPort(), new DataOutputStream(localSocket.getOutputStream()));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        mainThread.start();

        Platform.runLater(() -> {
            stage.setOnCloseRequest(e -> {
                mainThread.interrupt();
                // TODO : have to check why jvm is not exiting from above code block
                System.exit(0);
            });
        });
    }

    public void openUpClient() throws IOException {

        // opening chat-area for client
        Stage clientStage = new Stage(StageStyle.DECORATED);
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/chatwithme/FXML/client.fxml"));
        Scene client = new Scene(loader.load());

        clientStage.setScene(client);
        clientStage.setTitle("User : " + clientName.getText());
        clientStage.setResizable(false);
        clientStage.sizeToScene();
        clientStage.getIcons().add(new Image("/com/chatwithme/CSS/img/logo.png"));

        // passing data via the controller
        try {
            clientController controller = loader.getController();
            controller.initData(clientName.getText(),clientStage);
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        clientStage.show();

    }

    public void initData (Stage stage) {
        this.stage = stage;
    }

    public void validateUser(KeyEvent keyEvent) {
        if(clientName.getText().matches("^[A-Z,a-z]{3,10}$")) {
            if (startBtn.isDisabled()) {
                startBtn.setDisable(false);
            }
            alert.setVisible(false);
        }else {
            if (!startBtn.isDisabled()){
                startBtn.setDisable(true);
            }
            alert.setVisible(true);
        }
    }
}
