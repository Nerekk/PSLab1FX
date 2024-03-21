package com.example.pslab1fx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ServerSocket serverSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private final ServerController c;

    public Server(ServerController c) {
        this.c = c;
    }

    public void start(Integer port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            c.setLabelInfo("Socket input error!");
            throw new RuntimeException(e);
        }
        c.setLabelInfo("OK!");
    }

    public void stop() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
