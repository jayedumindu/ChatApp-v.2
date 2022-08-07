package com.chatwithme.Thread;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ListenerThread extends TimerTask {

    DataInputStream in;
    VBox msgArea;
    Timer timer;

    public ListenerThread(DataInputStream inputStream,VBox msgArea,Timer timer){
        in = inputStream;
        this.msgArea = msgArea;
        this.timer = timer;
    }

    @Override
    public void run() {
        // listens for other parties to communicate --> infinite loop
            try {
                if(in.available()>0){

                    String msg = in.readUTF();
                    // creating a label to add
                    Label msgLbl = new Label(msg);
                    System.out.println(msg);
                    Platform.runLater(() -> {
                                msgArea.getChildren().add(msgLbl);
                            });

//                    String fileType = in.readUTF();
//                    System.out.println();
                    /*System.out.println("something came!");
                    byte[] bytes = new byte[10000000];
                    in.read(bytes);*/

                    /*ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    BufferedImage bImage2 = ImageIO.read(bis);
                    try {
                        ImageIO.write(bImage2, "png", new File("output.png") );
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            new Alert(Alert.AlertType.ERROR,"file write was not successful : " + e).showAndWait();
                        });
                    }*/


                    /*if(in.readByte()==-1){
                        String fileType = in.readUTF();
                        System.out.println();

                        byte[] bytes = new byte[10000000];
                        in.read(bytes);

                        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                        BufferedImage bImage2 = ImageIO.read(bis);
                        try {
                            ImageIO.write(bImage2, fileType, new File("output.jpg") );
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                new Alert(Alert.AlertType.ERROR,"file write was not successful : " + e).showAndWait();
                            });
                        }
                    }else {
                        String msg = in.readUTF();
                        // creating a label to add
                        Label msgLbl = new Label(msg);
                        System.out.println(msg);
                        Platform.runLater(() -> {
                            msgArea.getChildren().add(msgLbl);
                        });
                    }*/

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void stop() {
        timer.cancel();
    }

}
