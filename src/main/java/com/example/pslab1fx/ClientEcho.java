package com.example.pslab1fx;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.example.pslab1fx.Server.isThreadRunning;
import static com.example.pslab1fx.ServerController.ECHO;
import static com.example.pslab1fx.ServerController.INFO;

public class ClientEcho implements Runnable {
    private final Integer id;
    private final String address;
    private final Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private final ServerController c;

    public ClientEcho(int id, Socket socket, ServerController c) {
        this.id = id;
        this.socket = socket;
        this.address = socket.getInetAddress().toString() + ":" + socket.getPort();
        this.c = c;
    }

    @Override
    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            // pobierz wiadomosc, wyslij wiadomosc
            while (isThreadRunning) {
                String messageFromClient = input.readUTF();
                Platform.runLater(() -> c.sendAlert(ECHO, "Client #" + id + " Message [" + messageFromClient.getBytes().length + " bytes]: " + messageFromClient));

                output.writeUTF(messageFromClient);
                Platform.runLater(() -> c.sendAlert(ECHO, "Sending message back to client #" + id));
            }

            Platform.runLater(() -> c.sendAlert(INFO, "Client #" + id +" disconnected by server stop"));
            c.removeClientFromList(socket);
        } catch (IOException e) {
            Platform.runLater(() -> c.sendAlert(INFO, "Client #" + id + " disconnected"));
            try {
                c.removeClientFromList(socket);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getIp() {
        int sub = address.indexOf(":");
        return address.substring(0, sub);
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public DataInputStream getInput() {
        return input;
    }
}
