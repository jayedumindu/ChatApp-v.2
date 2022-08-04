package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

public class serverController {

    @FXML
    public JFXTextField clientName;

    DataOutputStream outputStream;
    DataInputStream inputStream;
    Socket localSocket;
    ArrayList<Integer> serverSockets = new ArrayList<>();

    public void initialize() throws IOException {
       /* // setting up the server
        System.out.println("server up and running!");
        int PORT = 8000;
        serverSocket  = new ServerSocket(PORT).;
        // wait for an connection to be established --> binds it to a local socket --> returns the remote socket
        Thread serverWaitingThread = new Thread(() -> {
            try {
                localSocket = serverSocket.accept();
                System.out.println("connection succeeded!");

                outputStream = new DataOutputStream(localSocket.getOutputStream());
                inputStream = new DataInputStream(localSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverWaitingThread.start();*/
    }

/*    public void sendMsg(ActionEvent actionEvent) throws IOException {
        String msg = msgField.getText();
        msgPane.appendText("\nMe : " + msg);
        outputStream.writeUTF(msg);
        outputStream.flush();
        msgField.clear();
    }*/

/*    public void clear(ActionEvent actionEvent) {
        msgPane.clear();
    }*/

    public void openUpClient(ActionEvent actionEvent) throws IOException {
        Stage clientStage = new Stage();
        Scene client = new Scene(FXMLLoader.load(this.getClass().getClassLoader().getResource("com/chatwithme/view/client.fxml")));
        clientStage.setScene(client);
        clientStage.setTitle("Client App");
        clientStage.show();

        /*Timer timer = new Timer();
        timer.schedule(new ListenerThread(inputStream,"client", msgPane,timer),1000, 2000);*/
    }

}
