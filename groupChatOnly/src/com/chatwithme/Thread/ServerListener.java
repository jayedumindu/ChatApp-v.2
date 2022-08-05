package com.chatwithme.Thread;

import com.chatwithme.util.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.TimerTask;

public class ServerListener extends TimerTask {

    Server server;

    public ServerListener(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            Socket localSocket = server.accept();
            localSocket.getOutputStream();
            localSocket.getInputStream();
        } catch (IOException e) {
            System.out.println("Server Error : " + e.getMessage());
        }finally {
            System.out.println("client connected!");
        }
    }
}
