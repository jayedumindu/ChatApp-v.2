package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.Thread.ServerListener;
import com.chatwithme.util.Server;
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

    Server server;
    Timer timer;

    DataOutputStream outputStream;
    DataInputStream inputStream;
    Socket localSocket;
    ArrayList<Integer> serverSockets = new ArrayList<>();

    public void initialize() throws IOException {

        timer = new Timer();

        // open up the server
        server = new Server(8000,5);

        // starts listening to client requests
        timer.schedule(new ServerListener(server),1000, 2000);

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

        Thread serverWaitingThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000,5);
                serverSocket.accept();
                System.out.println("accepted client 1");
                serverSocket.accept();
                System.out.println("accepted client 2");
                serverSocket.accept();
                System.out.println("accepted client 3");
                serverSocket.accept();
                System.out.println("accepted client 4");
                serverSocket.accept();
                System.out.println("accepted client 5");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverWaitingThread.start();





        Stage clientStage = new Stage();
        Scene client = new Scene(FXMLLoader.load(this.getClass().getClassLoader().getResource("com/chatwithme/FXML/client.fxml")));
        clientStage.setScene(client);
        clientStage.setTitle("Client App");
        clientStage.show();

        /*Timer timer = new Timer();
        */
    }

}
