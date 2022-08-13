package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Timer;


public class clientController {

    @FXML
    public JFXTextField msgField;
    public VBox msgBox;
    public ScrollPane scrollPane;
    public AnchorPane mainPane;
    public Pane emojiContainer;

    // TODO : hv to encapsulate client
    Client client;
    String clientName;
    ListenerThread listener;
    Stage stage;

    // meta-data
    int localPort;
    DataOutputStream localOutputStream;

    // file handling
    FileChooser fileChooser;

    // data handling
    byte[] payload;
    byte[] header;
    byte[] sender;
    byte[] frame;

    int mouseCounter = 0;

    public void initialize() throws IOException {

        Platform.runLater(() -> {
           // add a listener for scrollbar to be at the end
            msgBox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
            stage.setOnCloseRequest(e -> {
                listener.stop();
            });
            try {
                Connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private void Connect() throws IOException {
        client = new Client(clientName,"localhost",8080);

        localPort = client.getLocalPort();
        localOutputStream = serverController.clients.get(localPort);
        sender = ByteBuffer.allocate(4).putInt(localPort).array();
        Timer timer = new Timer();
        fileChooser = new FileChooser();

        // TODO : open up a listener for server
                try {
                    listener = new ListenerThread(new DataInputStream(client.getInputStream()),msgBox,timer,clientName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                timer.schedule(listener,0,1000);
    }

    public boolean sendMsg(String clientName) throws IOException, InterruptedException {

        // TODO : bypass all the empty spaces after and before
        if (!msgField.getText().trim().isEmpty()){

            String msg = clientName + " :\n" + msgField.getText();
            payload = msg.getBytes(StandardCharsets.UTF_16);
            int len = payload.length;

            header = ByteBuffer.allocate(4).putInt(len).array();
            frame = ArrayUtils.addAll(header,sender);
            frame = ArrayUtils.addAll(frame,payload);

            client.getOut().write(0);
            client.getOut().write(frame);
            client.getOut().flush();

            return true;
        } else return false;


    }


    public void initData(String name, Stage stage) {
        this.stage = stage;
        this.clientName = name;
    }


    // TODO : pass the file type before sending file
    public void uploadPhoto(MouseEvent mouseEvent) throws IOException {

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile!=null) {

            String[] res = selectedFile.getName().split("\\.");

            BufferedImage finalImage = ImageIO.read(selectedFile);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(finalImage, res[1], bout);

            byte[] temp_payload = clientName.getBytes(StandardCharsets.UTF_16);
            byte[] temp_header = ByteBuffer.allocate(4).putInt(temp_payload.length).array();

            payload = bout.toByteArray();
            header = ByteBuffer.allocate(4).putInt(payload.length).array();

            frame = ArrayUtils.addAll(temp_header,header);

            frame = ArrayUtils.addAll(frame,sender);
            frame = ArrayUtils.addAll(frame,temp_payload);
            frame = ArrayUtils.addAll(frame,payload);

            client.getOut().write(-1);
            client.getOut().write(frame);
            client.getOut().flush();

        }

    }

    public void sendOnMousePressed(MouseEvent mouseEvent) throws IOException, InterruptedException {
        if(sendMsg(clientName)){
            msgField.clear();
        }
    }

    public void sendOnEnter(KeyEvent keyEvent) throws IOException, InterruptedException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            if(sendMsg(clientName)){
                msgField.clear();
            }
        }
    }

    public void openUpEmojiMenu(MouseEvent mouseEvent) throws IOException {
        mouseCounter++;
        emojiContainer.setVisible(mouseCounter % 2 == 1);
    }

    public void copyEmojiToMsg(MouseEvent mouseEvent) {
        ImageView emoji = (ImageView) mouseEvent.getSource();
        String id = emoji.getId();
        switch (id) {
            case "smile" : msgField.appendText("\uD83D\uDE00"); break;
            case "alien" : msgField.appendText("\uD83D\uDC7D"); break;
            case "hand" : msgField.appendText("\u270B"); break;
            case "smInPain" : msgField.appendText("\ud83d\ude08"); break;
            case "heart" : msgField.appendText("\uD83D\uDC9D"); break;
            case "party" : msgField.appendText("\uD83E\uDD73"); break;
            case "angry" : msgField.appendText("\uD83D\uDE20"); break;
            case "hot" : msgField.appendText("\uD83E\uDD75"); break;
            case "neutral" : msgField.appendText("\uD83D\uDE10"); break;
            case "poop" : msgField.appendText("\uD83D\uDCA9"); break;
            case "broken" : msgField.appendText("\uD83D\uDC94"); break;
            case "love" : msgField.appendText("\uD83D\uDE0D"); break;
        }
    }
}
