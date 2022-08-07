package com.chatwithme.Thread;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.TimerTask;

import com.chatwithme.Controllers.serverController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
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

                // TODO : just flush the incoming stream

                // temporary buffer
                //byte[] payload = IOUtils.toByteArray(inputStream);
                /*byte[] arr = new byte[2048];
                inputStream.read(arr);

                for (DataOutputStream out : serverController.clients) {
                    out.write(arr);
                    out.flush();
                }*/

                //String fileType = inputStream.readUTF();
                //System.out.println(fileType);
                // waiting for an image
                /*System.out.println("something came - server");
                byte[] bytes = new byte[10000000];
                inputStream.read(bytes);*/
                byte[] payload = IOUtils.toByteArray(inputStream);
                for (DataOutputStream out: serverController.clients.values()) {
                    out.write(payload);
                    out.flush();
                }
                //ByteArrayOutputStream output = new ByteArrayOutputStream();

                //IOUtils.copy(inputStream,output);
                /*byte[] dump = new byte[2048];
                byte conf = 0;
                do {
                     conf = inputStream.readByte();
                     //dump.a
                }while(conf!=-1);

                for (DataOutputStream out: serverController.clients) {
                    out.write(dump);
                    out.flush();
                }*/

               /* int len = inputStream.readByte();
                System.out.println(len);
                if(len>7) {

                    byte[] dataArray = new byte[len];
                    *//* for (int index = 0; index<len; index++){
                        dataArray[index] = inputStream.readFully();
                    }*//*
                    inputStream.readFully(dataArray);
                    for (DataOutputStream out : serverController.clients) {
                        out.write(dataArray);
                        out.flush();
                    }
                }*/



                // analyzing the first byte
                /*if(inputStream.readByte()==-1){
                    String fileType = inputStream.readUTF();
                    System.out.println(fileType);
                    // waiting for an image
                    byte[] bytes = new byte[10000000];
                    inputStream.read(bytes);
                    for (DataOutputStream out: serverController.clients) {
                        out.write(-1);
                        out.writeUTF(fileType);
                        out.flush();
                        out.write(bytes);
                        out.flush();
                    }
                }else {
                   String msg = inputStream.readUTF();
                    // flush to all output-streams
                    for (DataOutputStream out: serverController.clients) {
                        out.write(0);
                        out.writeUTF(msg);
                    }
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
