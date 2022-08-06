package com.chatwithme.Thread;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ListenerThread extends TimerTask {

    DataInputStream in;
    VBox msgArea;
    Timer timer;

    public ListenerThread(DataInputStream inputStream,VBox msgArea){
        in = inputStream;
        this.msgArea = msgArea;
        timer = new Timer();
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
                    /*System.out.println("something  came here!");

                    // implementing image reading
                    BufferedImage img = ImageIO.read(in);

                    ImageView view = new ImageView();*/

                    //view.setImage(img);

                   /* if(msg.equals("over")){
                        msgArea.appendText("\nExiting.........");
                        timer.cancel();
                        return;
                    }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}
