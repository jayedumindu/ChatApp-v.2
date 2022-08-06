package com.chatwithme.Thread;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.TimerTask;

import com.chatwithme.Controllers.serverController;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;

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

                // analyzing the first byte
                if(inputStream.readByte()==-1){
                    // waiting for an image
                    byte[] bytes = new byte[10000000];
                    inputStream.read(bytes);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    BufferedImage bImage2 = ImageIO.read(bis);
                    ImageIO.write(bImage2, "jpg", new File("output.jpg") );
                }else {
                   String msg = inputStream.readUTF();
                    // flush to all output-streams
                    for (DataOutputStream out: serverController.clients) {
                    out.writeUTF(msg);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
