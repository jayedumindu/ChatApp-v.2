package com.chatwithme.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Server extends ServerSocket {

    // to map a port number with a client
    HashMap<Integer,Client> userList = new HashMap<>();

    public Server(int port, int backlog) throws IOException {
        super(port,backlog);
    }

}
