package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

public class clientController {

    @FXML
    public JFXTextField msgField;
    public TextArea msgPane;
    public Text username;

    // TODO : hv to encapsulate client
    Client client;
    String clientName;

    private Timer timer;


    public void initialize() throws IOException {

        username.setText("username : " + clientName);

        timer = new Timer();

        client = new Client(clientName,"localhost",8000);

        // TODO : open up a listener for server
        timer.schedule(new ListenerThread(new DataInputStream(client.getInputStream()),msgPane),0,2000);

    }

    public void sendMsg(ActionEvent actionEvent) throws IOException {

        client.getOut().writeUTF(clientName + " : " + msgField.getText());

    }

    public void clear(ActionEvent actionEvent) {
        msgPane.clear();
    }

    public void initData(String name) {
        this.clientName = name;
    }
}
