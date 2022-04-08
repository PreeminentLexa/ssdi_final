package com.example.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class RunServer {
    private static int port = 8081;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        while(true){
            System.out.println("The server has been started on port "+port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connecting "+getName(clientSocket));
            InputStream inStream = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inStream);
            BufferedReader in = new BufferedReader(reader);
            String line = null;
            try{
                while((line = in.readLine()) != null){
                    System.out.println(line);
                }
            } catch (IOException e){
                if(e.getMessage().equals("Connection reset")){
                    System.out.println("Connection from "+getName(clientSocket)+" was reset.");
                }
                System.err.println("Problem reading from "+getName(clientSocket)+", "+e.getMessage());
            }

            System.out.println("Closing connection");
            in.close();
            reader.close();
            clientSocket.close();
        }
    }


    public static String getName(Socket client) {
        return client.getInetAddress().getHostAddress() + ":" + client.getPort();
    }
}
