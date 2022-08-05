package com.chatwithme.Thread;

import com.chatwithme.Controllers.serverController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.TimerTask;

public class Flusher extends TimerTask {

    // TODO : listen for all incoming data and flush the traffic to each of the clients

    DataInputStream inputStream;

    public Flusher(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            if(inputStream.available()>0){
                String msg = inputStream.readUTF();
                // flush to all output-streams
                for (DataOutputStream out: serverController.clients) {
                    out.writeUTF(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
