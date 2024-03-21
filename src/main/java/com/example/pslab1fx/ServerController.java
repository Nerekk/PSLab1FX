package com.example.pslab1fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Label labelStatus;
    @FXML
    private Label labelInfo;

    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonStop;

    @FXML
    private TextArea serverTextArea;
    @FXML
    private TextField serverTextField;
    public final static String STATUS_ON = "ON";
    public final static String STATUS_OFF = "OFF";

    private Server server;

    @FXML
    protected void serverStart() {
        Integer port;
        try {
            port = Integer.parseInt(serverTextField.getText());
        } catch (NumberFormatException e) {
            setLabelInfo("Given port is not number!");
            throw new RuntimeException(e);
        }
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
    }

    public void setLabelStatus(String s) {
        labelStatus.setText(s);
    }

    public void setLabelInfo(String s) {
        labelInfo.setText(s);
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
}