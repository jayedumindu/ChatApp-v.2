package com.chatwithme.Thread;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

            try {
                if(in.available()>0){

                    stop();

                    if (in.readByte()==0){

                        byte[] header = new byte[4];
                        in.read(header);

                        ByteBuffer buffer = ByteBuffer.wrap(header);
                        int len = buffer.getInt();

                        byte[] payload = new byte[len];
                        in.read(payload);
                        String msg = new String(payload, StandardCharsets.UTF_8);
                        Label msgLbl = new Label(msg);
                        Platform.runLater(() -> {
                            msgArea.getChildren().add(msgLbl);
                        });

                    }else {

                        byte[] header = new byte[4];
                        byte[] temp_header = new byte[4];
                        in.read(temp_header);
                        in.read(header);

                        ByteBuffer imgDataBuffer = ByteBuffer.wrap(header);
                        int img_pay_len = imgDataBuffer.getInt();

                        ByteBuffer senderDataBuffer = ByteBuffer.wrap(temp_header);
                        int sender_pay_len = senderDataBuffer.getInt();

                        byte[] sender_payload = new byte[sender_pay_len];
                        in.read(sender_payload);

                        byte[] img_payload = new byte[img_pay_len];
                        in.read(img_payload);

                        ByteArrayInputStream bis = new ByteArrayInputStream(img_payload);
                        BufferedImage imageData = ImageIO.read(bis);

                        Image image = SwingFXUtils.toFXImage(imageData,null);

                        GridPane imagePane = new GridPane();
                        ImageView view = new ImageView(image);
                        Label sender = new Label(new String(sender_payload,StandardCharsets.UTF_8).concat(" :"));

                        sender.getStyleClass().add("custom-label");
                        imagePane.getStyleClass().add("custom-image1");
                        imagePane.setMaxWidth(250);
                        imagePane.add(view,0,1);
                        imagePane.add(sender,0,0);

                        view.setFitHeight(250);
                        view.setFitWidth(250);
                        view.setSmooth(true);
                        view.setPreserveRatio(true);
                        Platform.runLater(() -> {
                            msgArea.getChildren().add(imagePane);
                        });

                    }

                    resume();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void resume() {
        Timer timer = new Timer();
        timer.schedule(new ListenerThread(in,msgArea,timer),0,1000);
    }

    public void stop() {
        timer.cancel();
    }

}
