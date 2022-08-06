package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    // file handling
    FileChooser fileChooser;

    private Timer timer;


    public void initialize() throws IOException {

        Platform.runLater(() -> {
           // add a listener for scrollbar to be at the end
            msgBox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
        });

        timer = new Timer();
        fileChooser = new FileChooser();

        client = new Client(clientName,"localhost",8000);

        // TODO : open up a listener for server
        timer.schedule(new ListenerThread(new DataInputStream(client.getInputStream()),msgBox),0,1000);

    }

    public void sendMsg(MouseEvent actionEvent) throws IOException {

        byte e = 0;
        client.getOut().write(e);

        client.getOut().writeUTF(clientName + " :\n" + msgField.getText());

    }

    public void clear(ActionEvent actionEvent) throws IOException {
        byte e = 0;
        client.getOut().write(e);
    }

    public void initData(String name) {
        this.clientName = name;
    }

    public void uploadPhoto(MouseEvent mouseEvent) throws IOException {
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        // file path
        String filename = selectedFile.getName();
        String ext = filename.substring(filename.lastIndexOf(".")+1);
        System.out.println(filename);

        // write img-meta-data before sending image data
        byte e = -1;
        client.getOut().write(e);

        BufferedImage finalImage  = ImageIO.read(selectedFile);
        //BufferedImage finalImage  = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ImageIO.write(finalImage,ext,bout);
        client.getOut().write(bout.toByteArray());
        client.getOut().flush();
        bout.flush();
        bout.reset();
    }
}
