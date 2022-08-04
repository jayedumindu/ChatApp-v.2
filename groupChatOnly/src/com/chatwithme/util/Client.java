package com.chatwithme.util;

import java.io.IOException;
import java.net.Socket;

public class Client extends Socket {

    String name;

    public Client (String name, String host, int port) throws IOException {
        super(host,port);
        this.name = name;
    }

}
