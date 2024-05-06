package com.example.pslab1fx;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private IntegerProperty counter = new SimpleIntegerProperty(0);
    private int incrementer = 0;
    @FXML
    private Label clientCounter;

    ObservableList<ClientEcho> clients = FXCollections.observableArrayList();
    @FXML
    private TableView<ClientEcho> clientList;

    @FXML
    private TableColumn<ClientEcho, Integer> clientListId;

    @FXML
    private TableColumn<ClientEcho, String> clientListAddress;

    public final static String STATUS_ON = "ON";
    public final static String STATUS_OFF = "OFF";
    public final static int INFO = 0;
    public final static int ECHO = 1;
    public final static int ERROR = 2;

    private Server server;
    private HashMap<Integer, Thread> threadMap = new HashMap<>();

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

        clientListId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientListAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientList.setItems(clients);

        counter.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> clientCounter.setText(String.valueOf(newValue.intValue())));
        });
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

    public void setCounter(int size) {
        counter.setValue(size);
    }

    public int getCounter() {
        return counter.getValue();
    }

    public synchronized void addClientToList(Socket socket) {
        ClientEcho clientEcho = new ClientEcho(++incrementer, socket, this);

        clients.add(clientEcho);
        setCounter(clients.size());

        Thread clientThread = new Thread(clientEcho);
        threadMap.put(clientEcho.getId(), clientThread);
        clientThread.start();
    }

    public synchronized void removeClientFromList(Socket socket) throws IOException {
        String address = socket.getInetAddress().toString() + ":" + socket.getPort();

        for (int i = 0; i < clients.size(); i++) {
            String clientAddress = clients.get(i).getAddress();

            if (Objects.equals(address, clientAddress)) {
                killClient(i);
                break;
            }
        }
        setCounter(clients.size());
    }

    public synchronized void removeAllClientsFromList() throws IOException {
        int size = clients.size();
        for (int i = 0; i < size; i++) {
            killClient(0);
        }
        setCounter(clients.size());
    }

    private void killClient(int i) throws IOException {
        ClientEcho clientEcho = clients.get(i);
        clientEcho.getSocket().close();
        clientEcho.getInput().close();
        clientEcho.getOutput().close();
        threadMap.get(clientEcho.getId()).interrupt();
        threadMap.remove(clientEcho.getId());
        clients.remove(i);
    }
}