package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Timer;


public class clientController {

    @FXML
    public JFXTextField msgField;
    public VBox msgBox;
    public ScrollPane scrollPane;
    public AnchorPane mainPane;

    // TODO : hv to encapsulate client
    Client client;
    String clientName;
    ListenerThread listener;

    // file handling
    FileChooser fileChooser;

    // header-payload
    byte[] head;


    public void initialize() throws IOException {

        Platform.runLater(() -> {
           // add a listener for scrollbar to be at the end
            msgBox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
        });

        client = new Client(clientName,"localhost",8080);

        Timer timer = new Timer();
        fileChooser = new FileChooser();

        // TODO : open up a listener for server
        try {
            listener = new ListenerThread(new DataInputStream(client.getInputStream()),msgBox,timer);
            timer.schedule(listener,0,1000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void reConnect() throws IOException {
        client = new Client(clientName,"localhost",8080);
    }

    public void sendMsg(String msg) throws IOException, InterruptedException {
        // removing client from the list
        serverController.clients.remove(client.getLocalPort());
        client.getOut().writeUTF(msg);
        client.getOut().flush();
        client.getOut().close();
        listener.stop();
        // displaying
        Label msgLbl = new Label(msg);
        System.out.println(msg);
        Platform.runLater(() -> {
            msgBox.getChildren().add(msgLbl);
        });
        reConnect();
    }

    public void clear(ActionEvent actionEvent) throws IOException {
    }

    public void initData(String name) {
        this.clientName = name;
    }

    // TODO : pass the file type before sending file
    public void uploadPhoto(MouseEvent mouseEvent) throws IOException {

        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        if (selectedFile!=null) {
            // file path
            String filename = selectedFile.getName();
            String ext = filename.substring(filename.lastIndexOf(".") + 1);

            // write img-meta-data before sending image data
            //client.getOut().write(-1);

            // passing the file type
            client.getOut().writeUTF(ext);

            BufferedImage finalImage = ImageIO.read(selectedFile);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(finalImage, ext, bout);

            client.getOut().write(bout.toByteArray());
            client.getOut().flush();

            // getting ready for the next round
            bout.flush();
            bout.reset();
        }

    }

    public void sendOnKeyPressed(KeyEvent keyEvent) throws IOException, InterruptedException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            sendMsg(msgField.getText());
            msgField.clear();
        }
    }

    public void sendOnMousePressed(MouseEvent mouseEvent) throws IOException, InterruptedException {
        sendMsg(msgField.getText());
        msgField.clear();
    }
}
