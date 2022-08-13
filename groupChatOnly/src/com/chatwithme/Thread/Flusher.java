package com.chatwithme.Thread;

import com.chatwithme.Controllers.serverController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Flusher extends TimerTask {

    // TODO : listen for all incoming data and flush the traffic to each of the clients

    DataInputStream inputStream;
    Timer timer;

    public Flusher(DataInputStream inputStream, Timer timer) {
        this.inputStream = inputStream;
        this.timer = timer;
    }

    @Override
    public void run() {

        try {
            if(inputStream.available()>0){

                stop();

                if (inputStream.readByte()==0){
                    flush((byte) 0);
                }else {
                    flush((byte) -1);
                }

                resume();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void flush(byte b) throws IOException {

        byte[] header = new byte[4];
        byte[] temp_header = new byte[4];
        byte[] sender = new byte[4];

        byte[] frame;

        int pay_len;
        int sender_port;

        if(b==-1){
            // read two headers, get the length
            inputStream.read(temp_header);
            inputStream.read(header);
            ByteBuffer headerBuffer = ByteBuffer.wrap(header);
            ByteBuffer tempHeaderBuffer = ByteBuffer.wrap(temp_header);
            pay_len = headerBuffer.getInt() + tempHeaderBuffer.getInt();
            // read sender
            inputStream.read(sender);
            // collect payload
            ByteBuffer senderBuffer = ByteBuffer.wrap(sender);
            sender_port = senderBuffer.getInt();

            byte[] payload = new byte[pay_len];
            inputStream.read(payload);

            frame = new byte[pay_len + 8];
            System.arraycopy(temp_header, 0, frame, 0, 4);
            System.arraycopy(header, 0, frame, 4, 4);
            System.arraycopy(payload, 0, frame, 8, pay_len);
        }else {
            inputStream.read(header);

            ByteBuffer headerBuffer = ByteBuffer.wrap(header);
            pay_len = headerBuffer.getInt();

            inputStream.read(sender);

            ByteBuffer senderBuffer = ByteBuffer.wrap(sender);
            sender_port = senderBuffer.getInt();

            byte[] payload = new byte[pay_len];
            inputStream.read(payload);

            frame = new byte[pay_len + 4];
            System.arraycopy(header, 0, frame, 0, 4);
            System.arraycopy(payload, 0, frame, 4, pay_len);
        }

        for(Map.Entry<Integer, DataOutputStream> entry : serverController.clients.entrySet()) {

            if(entry.getKey()!=sender_port){
                entry.getValue().write(b);
                entry.getValue().write(frame);
                entry.getValue().flush();
            }

        }
    }

    private void stop() {
        timer.cancel();
        timer.purge();
    }

    private void resume() {
        timer = new Timer();
        timer.schedule(new Flusher(new DataInputStream(inputStream),timer),0,2000);
    }
}
