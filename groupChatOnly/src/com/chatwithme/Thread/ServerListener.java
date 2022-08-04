package com.chatwithme.Thread;

import com.chatwithme.util.Server;

import java.io.IOException;
import java.util.TimerTask;

public class ServerListener extends TimerTask {

    Server server;

    public ServerListener(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.accept();
        } catch (IOException e) {
            System.out.println("Server Error : " + e.getMessage());
        }
    }
}
