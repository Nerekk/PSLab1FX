package com.example.pslab1fx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ServerSocket serverSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ServerController serverController;

    public Server(ServerController serverController) throws IOException {
        this.serverController = serverController;
        this.serverSocket = new ServerSocket();
    }

    public void start(Integer port) {

    }

    public void stop() throws IOException {
        this.serverSocket.close();
    }


}
