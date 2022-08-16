package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import com.pavlobu.emojitextflow.EmojiTextFlow;
import com.pavlobu.emojitextflow.EmojiTextFlowParameters;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
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

    Client client;
    String clientName;
    ListenerThread listener;
    Stage stage;

    int localPort;

    FileChooser fileChooser;

    byte[] payload;
    byte[] header;
    byte[] sender;
    byte[] frame;

    int mouseCounter = 0;

    EmojiTextFlowParameters emojiTextFlowParameters;
    {
        emojiTextFlowParameters = new EmojiTextFlowParameters();
        emojiTextFlowParameters.setEmojiScaleFactor(3);
        emojiTextFlowParameters.setFont(Font.font("Roboto Light", FontPosture.REGULAR, 18));
    }

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
        sender = ByteBuffer.allocate(4).putInt(localPort).array();
        Timer timer = new Timer();
        fileChooser = new FileChooser();

                try {
                    listener = new ListenerThread(new DataInputStream(client.getInputStream()),msgBox,timer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                timer.schedule(listener,0,1000);
    }

    public boolean sendMsg(String clientName) throws IOException, InterruptedException {

        if (!msgField.getText().trim().isEmpty()){

            String msgF = clientName + " :\n" + msgField.getText();
            payload = msgF.getBytes(StandardCharsets.UTF_8);
            int len = payload.length;

            header = ByteBuffer.allocate(4).putInt(len).array();
            frame = ArrayUtils.addAll(header,sender);
            frame = ArrayUtils.addAll(frame,payload);

            client.getOut().write(0);
            client.getOut().write(frame);
            client.getOut().flush();

            Label msgLabel = new Label(msgField.getText());
            msgLabel.setStyle("-fx-background-color: #5181b8; -fx-text-fill: white");
            HBox box = new HBox();
            box.getChildren().add(msgLabel);
            box.setAlignment(Pos.BASELINE_RIGHT);
            msgBox.getChildren().add(box);


            return true;
        } else return false;

    }

    private void sendEmoji(String code) throws IOException {
        String msgF = clientName + " :\n" + code;
        payload = msgF.getBytes(StandardCharsets.UTF_8);
        int len = payload.length;

        header = ByteBuffer.allocate(4).putInt(len).array();
        frame = ArrayUtils.addAll(header,sender);
        frame = ArrayUtils.addAll(frame,payload);

        client.getOut().write(1);
        client.getOut().write(frame);
        client.getOut().flush();

        EmojiTextFlow dialogContainer = new EmojiTextFlow(emojiTextFlowParameters);
        dialogContainer.getStyleClass().add("label");
        dialogContainer.setStyle("-fx-background-color: #5181b8;");
        dialogContainer.parseAndAppend(code);
        HBox box = new HBox();
        box.getChildren().add(dialogContainer);
        box.setAlignment(Pos.BASELINE_RIGHT);
        msgBox.getChildren().add(box);
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
            BufferedImage finalImage;

            try {
                finalImage = ImageIO.read(selectedFile);
            } catch (Exception e) {
                new Alert(Alert.AlertType.NONE,"Please select a file-type associated with images! try again", ButtonType.CLOSE).showAndWait();
                return;
            }

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(finalImage, res[1], bout);

            byte[] temp_payload = clientName.getBytes(StandardCharsets.UTF_8);
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

            HBox box = new HBox();

            GridPane pane  = new GridPane();
            pane.getStyleClass().add("custom-image2");
            pane.setStyle("-fx-background-color: #5181b8;");

            ImageView view = new ImageView(SwingFXUtils.toFXImage(finalImage,null));
            view.setFitHeight(250);
            view.setFitWidth(250);
            view.setSmooth(true);
            view.setPreserveRatio(true);

            pane.getChildren().add(view);

            box.setAlignment(Pos.BASELINE_RIGHT);
            box.getChildren().add(pane);

            msgBox.getChildren().add(box);


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

    public void copyEmojiToMsg(MouseEvent mouseEvent) throws IOException {
        ImageView emoji = (ImageView) mouseEvent.getSource();
        String id = emoji.getId();
        switch (id) {
            case "smile" : sendEmoji(":smiley:"); break;
            case "alien" : sendEmoji(":alien:"); break;
            case "hand" : sendEmoji(":raising_hand:"); break;
            case "smInPain" : sendEmoji(":joy:"); break;
            case "heart" : sendEmoji(":gift_heart:"); break;
            case "party" : sendEmoji(":tada:"); break;
            case "angry" : sendEmoji(":angry:"); break;
            case "hot" : sendEmoji(":cold_sweat:"); break;
            case "neutral" : sendEmoji(":neutral_face:"); break;
            case "poop" : sendEmoji(":poop:"); break;
            case "broken" : sendEmoji(":broken_heart:"); break;
            case "love" : sendEmoji(":heart_eyes:"); break;
        }
    }
}
