package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Timer;

public class clientController {

    @FXML
    public JFXTextField msgField;
    public Text username;
    public VBox msgBox;

    // TODO : hv to encapsulate client
    Client client;
    String clientName;

    private Timer timer;


    public void initialize() throws IOException {

        Platform.runLater(() -> {
           /* username.setText("username : " + clientName);*/
        });

        timer = new Timer();

        client = new Client(clientName,"localhost",8000);

        // TODO : open up a listener for server
        timer.schedule(new ListenerThread(new DataInputStream(client.getInputStream()),msgBox),0,1000);

    }

    public void sendMsg(MouseEvent actionEvent) throws IOException {

        client.getOut().writeUTF(clientName + " :\n" + msgField.getText());

    }

    public void clear(ActionEvent actionEvent) {
        /*msgPane.clear();*/
    }

    public void initData(String name) {
        this.clientName = name;
    }

    public void uploadPhoto(MouseEvent mouseEvent) {
    }
}
