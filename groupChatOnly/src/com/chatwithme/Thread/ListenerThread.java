package com.chatwithme.Thread;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ListenerThread extends TimerTask {

    String oppositionParty;
    DataInputStream in;
    TextArea msgArea;
    Timer timer;

    public ListenerThread(DataInputStream inputStream, String identifier, TextArea msgArea, Timer timer){
        in = inputStream;
        oppositionParty = identifier;
        this.msgArea = msgArea;
        this.timer = timer;
    }

    @Override
    public void run() {
        // listens for other parties to communicate --> infinite loop
        String message;
            try {
                if(in.available()>0){
                    String msg = in.readUTF();
                    msgArea.appendText("\n"+oppositionParty+" : "+msg);
                    if(msg.equals("over")){
                        msgArea.appendText("\nExiting.........");
                        timer.cancel();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}
