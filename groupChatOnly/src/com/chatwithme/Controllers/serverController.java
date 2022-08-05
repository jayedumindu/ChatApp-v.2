package com.chatwithme.Controllers;

import com.chatwithme.Thread.Flusher;
import com.chatwithme.util.Server;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

public class serverController {

    @FXML
    public JFXTextField clientName;

    Server server;
    Timer timer;

    Socket localSocket;
    public static ArrayList<DataOutputStream> clients = new ArrayList<>();

    public void initialize() throws IOException {

        timer = new Timer();

        // open up the server
        server = new Server(8000, 5);

        // TODO starts listening to client requests
        new Thread(() -> {
            // always waits for a client's request
            while (true) {
                try {
                    localSocket = server.accept();
                    // TODO : have to listen on the input-stream
                    timer.schedule(new Flusher(new DataInputStream(localSocket.getInputStream())),0,1000);
                    clients.add(new DataOutputStream(localSocket.getOutputStream()));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();

    }

    public void openUpClient(ActionEvent actionEvent) throws IOException {

        // opening chat-area for client
        Stage clientStage = new Stage(StageStyle.DECORATED);
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/chatwithme/FXML/client.fxml"));
        Scene client = new Scene(loader.load());
        clientController controller = loader.getController();
        clientStage.setScene(client);
        clientStage.setTitle("Client App");

        // passing data via the controller
        try {
            controller.initData(clientName.getText());
        }catch (NullPointerException e){
            System.out.println(e);
        }

        clientStage.show();

    }

}
