package com.example.pslab1fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Label labelStatus;

    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonStop;

    @FXML
    private TextArea serverTextArea;
    @FXML
    private TextField serverTextField;

    @FXML
    private Label clientCounter;

    ObservableList<String> clients = FXCollections.observableArrayList();
    @FXML
    private ListView<String> clientList;

    public final static String STATUS_ON = "ON";
    public final static String STATUS_OFF = "OFF";
    public final static int INFO = 0;
    public final static int ECHO = 1;
    public final static int ERROR = 2;

    private Server server;

    public Integer getPort() {
        Integer port;
        try {
            port = Integer.parseInt(serverTextField.getText());
        } catch (NumberFormatException e) {
            sendAlert(ERROR, "Given port is not number!");
            return -1;
        }
        return port;
    }

    @FXML
    protected void serverStart() {
        Integer port = getPort();
        if (port == -1) return;

        server.start(port);
        switchButtonsLock();
        setLabelStatus(STATUS_ON);
    }

    @FXML
    protected void serverStop() {
        server.stop();
        switchButtonsLock();
        setLabelStatus(STATUS_OFF);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.server = new Server(this);
        clientList.setItems(clients);
    }

    public void setLabelStatus(String s) {
        labelStatus.setText(s);
    }

    public void setButtonStartLock(boolean lock) {
        buttonStart.setDisable(lock);
    }

    public void setButtonStopLock(boolean lock) {
        buttonStop.setDisable(lock);
    }

    public void switchButtonsLock() {
        if (buttonStart.isDisabled()) {
            setButtonStartLock(false);
            setButtonStopLock(true);
        } else {
            setButtonStartLock(true);
            setButtonStopLock(false);
        }
    }

    public void sendAlert(int type, String alert) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTimeString = "[" + currentTime.format(formatter) + "]";
        String sta = serverTextArea.getText();

        switch (type) {
            case INFO -> alert = "[INFO] " + alert;
            case ECHO -> alert = "[ECHO] " + alert;
            case ERROR -> alert = "[ERROR] " + alert;
        }

        String info;
        if (sta.isEmpty()) {
            info = currentTimeString + " " + alert;
        } else {
            info = "\n" + currentTimeString + " " + alert;
        }

        serverTextArea.appendText(info);
    }
}