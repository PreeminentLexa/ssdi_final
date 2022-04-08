package com.example.finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private int retryCount = 5; // Amount of times it attempts to connect
    private int retryDelay = 5; // Seconds before retrying connection

    private String IP;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private boolean shouldClose = false;
    public Connection(String address){
        String[] data = address.split(":");
        this.IP = data[0];
        this.port = Integer.parseInt(data[1]);
        connect();
    }
    public Connection(String IP, int port){
        this.IP = IP;
        this.port = port;
        connect();
    }

    public String getAddress(){
        return this.IP+":"+this.port;
    }

    /** connect - This is the method to connect to a server
     */
    public void connect(){
        if(this.isConnected()){
            System.out.println("Failed to connect");
            System.out.println("Connection to "+this.IP+":"+this.port+" already exists");
            return;
        }
        System.out.println("Connecting to "+this.IP+":"+this.port);
        Connection con_this = this;
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                con_this.close();
            }
        });
        connect(1);
    }

    /** connect - This is an internal method, and shouldn't be touched, this is the looping portion of a connection attempt
     * @param count An integer, the amount of times this has looped, starting at 1
     */
    private void connect(int count){
        try {
            this.socket = new Socket(this.IP, this.port);
            this.out = new PrintWriter(this.socket.getOutputStream());
            this.shouldClose = true;
            System.out.println("Connection successful!");
        } catch (IOException e) {
            if(this.retryCount > count){
                System.out.println("Failed to connect, retrying in "+this.retryDelay+" seconds...");
                try {
                    Thread.sleep(this.retryDelay*1000);
                    connect(count+1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Failed to connect to "+this.IP+":"+this.port);
            }
        }
    }

    /** isConnected checks whether this Connection is connected, and hasn't been cleaned up
     * @return A boolean, true if we're actively connected
     */
    public boolean isConnected(){
        if(null == this.socket){
            return false;
        }
        return this.socket.isConnected() && this.shouldClose;
    }

    public void setUsername(String username){
        if(!isConnected()){
            System.err.println("Failed to set username. Not connected to server");
            return;
        }
        sendMessage(
                ServerCallbacks.CODE_SETUSERNAME+username
        );
    }
    public void setImage(int imageNumber) {
        if(!isConnected()){
            System.err.println("Failed to set username. Not connected to server");
            return;
        }
        sendMessage(
                ServerCallbacks.CODE_SETIMAGE+imageNumber
        );
    }

    /** createGame - Used to send a message to the server, requesting the creation of a game
     * @param password A String, either the password, or null
     * @param rounds An integer, the amount of rounds in this game
     * @param questioner An integer, 1 for random questioner each round, 2 for a questioner specified by this game host later
     */
    public void createGame(String password, int rounds, int questioner){
        if(!isConnected()){
            System.err.println("Failed to create game. Not connected to server");
            return;
        }
        if(null == password){
            password = "";
        }
        sendMessage(
                "PSH"+rounds+"\r\n"+
                "PSH"+questioner+"\r\n"+
                "POP2CGM"+password
        );
        // WORK ON: create game object, then wait for code return
    }

    public void joinGame(String code, String password) {
        if(null == password){
            password = "";
        }
        sendMessage(
                "PSH"+code+"\r\n"+
                "POP1JGM"+password
        );
    }

    /**
     * @param message The message to write
     */
    public void sendMessage(String message){
        if(null == this.out){
            System.out.println("Not connected to server, unable to send message.");
            return;
        }
        this.out.println(message);
        this.out.flush();
    }
    public void close(){
        if(null != this.out){
            this.out.close();
            this.out = null;
        }
        if(socket.isConnected() && this.shouldClose) {
            System.out.println("Disconnecting from "+this.IP+":"+this.port);
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.shouldClose = false;
    }
}
