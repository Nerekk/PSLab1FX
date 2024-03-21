package com.example.pslab1fx;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private final ServerController c;
    private Thread t1 = new Thread(this::serverThread);
    private boolean isThreadRunning;
    private boolean isClientConnected = false;

    public Server(ServerController c) {
        this.c = c;
    }

    @Override
    public void run() {
        isThreadRunning = true;
        t1.start();
    }

    private void serverThread() {
        while (isThreadRunning) {
            waitForClient();
            // zamykanie serwera - metoda przypisana do przycisku
        }
    }

    public void waitForClient() {
        try {

            System.out.println("Waiting for client..");
            socket = serverSocket.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            System.out.println("Client connected!");
            isClientConnected = true;

            // pobierz wiadomosc, wyslij wiadomosc i zakoncz polaczenie z klientem

            socket.close();
        } catch (IOException e) {
            if (isClientConnected)
                System.out.println("Client disconnected!");
        }
    }

    public void start(Integer port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            c.setLabelInfo("Socket input error!");
            throw new RuntimeException(e);
        }
        c.setLabelInfo("Server started");
    }

    public void stop() {
        try {
            isThreadRunning = false;
            this.serverSocket.close();

            if (isClientConnected)
                socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        c.setLabelInfo("Server closed");

    }


}
