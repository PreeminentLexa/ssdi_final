package com.example.finalproject;

import java.net.ServerSocket;

public class ConnectedServer implements Runnable {
    private ServerSocket socket;
    public ConnectedServer(ServerSocket serverSocket){
        socket = serverSocket;
    }
    @Override
    public void run() {

    }
}
