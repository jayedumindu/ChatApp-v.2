package com.chatwithme.Thread;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

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
