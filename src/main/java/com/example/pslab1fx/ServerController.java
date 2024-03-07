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

    private Server server;

    @FXML
    protected void serverStart() {
        try {
            Integer port = Integer.parseInt(serverTextField.getText());
            server.start(port);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void serverStop() {
        try {
            server.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.server = new Server(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}