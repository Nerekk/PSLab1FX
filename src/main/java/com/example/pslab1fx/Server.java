package com.example.pslab1fx;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import static com.example.pslab1fx.ServerController.*;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private final ServerController c;
    private Thread t1;
    public static boolean isThreadRunning;

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
            listenForClient();
        }
    }

    public void listenForClient() {
        try {
            Platform.runLater(() -> c.sendAlert(INFO, "Waiting for clients.."));
            while (isThreadRunning) {
                Socket socket = serverSocket.accept();
                Platform.runLater(() -> c.sendAlert(INFO, "New client connected!"));
                c.addClientToList(socket);
            }

        } catch (IOException e) {
            // zamkniecie server socketa powoduje wyjście z tej metody za pomocą tego wyjątku
        }
    }

    public void start(Integer port) {
        try {
            this.serverSocket = new ServerSocket();
            SocketAddress address = new InetSocketAddress(port);
            this.serverSocket.bind(address);
        } catch (IOException e) {
            c.sendAlert(ERROR, "Socket creating error!");
            return;
        }
        c.sendAlert(INFO, "Server started");
        t1 = new Thread(this::serverThread);
        run();
    }

    public void stop() {
        try {
            isThreadRunning = false;
            this.serverSocket.close();
            c.removeAllClientsFromList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        c.sendAlert(INFO, "Server closed");
    }


}
