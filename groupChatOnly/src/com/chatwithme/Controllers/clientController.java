package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Timer;

public class clientController {
    public JFXTextField msgField;
    public TextArea msgPane;

    DataOutputStream outputStream;
    DataInputStream inputStream;
    Socket localSocket;

    public void initialize() throws IOException {



        /*localSocket = new Socket();
        InetAddress inetAddress=InetAddress.getByName("localhost");
        //the port should be greater or equal to 0, else it will throw an error
        int port=6000;
        //calling the constructor of the SocketAddress class
        SocketAddress socketAddress=new InetSocketAddress(inetAddress, port);
        //binding the  socket with the inetAddress and port number
        localSocket.bind(socketAddress);

        // address to the destination --> localhost:8000
        SocketAddress destinationAddress =new InetSocketAddress(inetAddress, 8000);
        localSocket.connect(destinationAddress);

        System.out.println("Connected to : " + localSocket.getPort() + " from " + localSocket.getLocalAddress() + " port : " + localSocket.getLocalPort());

        outputStream = new DataOutputStream(localSocket.getOutputStream());
        inputStream = new DataInputStream(localSocket.getInputStream());

        Timer timer = new Timer();
        timer.schedule(new ListenerThread(inputStream,"server", msgPane,timer),1000,2000);*/
    }

    public void sendMsg(ActionEvent actionEvent) throws IOException {
        Socket localSocket1 = new Socket("localhost",8000);
        Socket localSocket2 = new Socket("localhost",8000);
        Socket localSocket3 = new Socket("localhost",8000);
        Socket localSocket4 = new Socket("localhost",8000);
        Socket localSocket5 = new Socket("localhost",8000);
        Socket localSocket6 = new Socket("localhost",8000);

    }

    public void clear(ActionEvent actionEvent) {
        msgPane.clear();
    }
}
