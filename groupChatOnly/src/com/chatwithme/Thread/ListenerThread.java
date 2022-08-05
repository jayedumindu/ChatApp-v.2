package com.chatwithme.Thread;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ListenerThread extends TimerTask {

    DataInputStream in;
    TextArea msgArea;
    Timer timer;

    public ListenerThread(DataInputStream inputStream,TextArea msgArea){
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
                    msgArea.appendText("\n"+msg);
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
