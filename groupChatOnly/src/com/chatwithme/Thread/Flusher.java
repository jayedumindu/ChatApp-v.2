package com.chatwithme.Thread;

import com.chatwithme.Controllers.serverController;
import org.apache.commons.lang.ArrayUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
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
        /*System.out.println("server listening");*/
        try {
            if(inputStream.available()>0){

                // TODO : just flush the incoming stream

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
        inputStream.read(header);

        ByteBuffer buffer = ByteBuffer.wrap(header);
        int len = buffer.getInt();

        byte[] payload = new byte[len];
        inputStream.read(payload);

        for (OutputStream out: serverController.clients.values()) {
            out.write(b);
            out.write(ArrayUtils.addAll(header,payload));
            out.flush();
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
