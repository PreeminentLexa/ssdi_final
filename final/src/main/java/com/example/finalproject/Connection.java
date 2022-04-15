package com.example.finalproject;

import javafx.application.Platform;
import org.controlsfx.validation.Severity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Math.max;

public class Connection {
    private int retryCount = 5; // Amount of times it attempts to connect
    private int retryDelay = 5; // Seconds before retrying connection

    private String IP;
    private int port;
    private ConnectedServer server;
    private Socket socket;
    private PrintWriter out;
    private boolean shouldClose = false;
    public Connection(String address){
        String[] data = address.split(":");
        if(data.length != 2){
            Utility.Think.setConnectEndedFlag(true, false);
            return;
        }
        this.IP = data[0];
        this.port = Integer.parseInt(data[1]);
//        Platform.runLater(() -> this.connect());
        connect();
    }
    public Connection(String IP, int port){
        this.IP = IP;
        this.port = port;
//        Platform.runLater(() -> this.connect());
        connect();
    }

    private Thread callback;
    public void setConnectCallback(Thread callback){
        this.callback = callback;
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> con_this.close())); // cleanup connection if the application shuts down
        new Thread(() -> {
            con_this.connect(1);
            if(con_this.isConnected()){
                con_this.server = new ConnectedServer(con_this.socket);
                new Thread(con_this.server).start();
            }
        }).start();
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
            Utility.Think.setConnectEndedFlag(true, true);
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
                Utility.Think.setConnectEndedFlag(true, false);
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
     * @param settings A String array, an arbitrary amount of settings, in the form "key|type|value" where the type is "s", "i", "f" for string, integer, float
     */
    public void createGame(String password, String[] settings){
        if(!isConnected()){
            System.err.println("Failed to create game. Not connected to server");
            return;
        }
        if(null == password){
            password = "";
        }
        String message = "";
        int settingCount = 0;
        for(String setting : settings){
            if(3 != setting.split("\\|").length){
                System.err.println("Ignoring bad setting: "+setting+".");
            } else {
                message += ServerCallbacks.CODE_PUSH+setting+"\r\n";
                settingCount++;
            }
        }
        sendMessage(
                message+
                ServerCallbacks.CODE_POP+settingCount+
                ServerCallbacks.CODE_CREATEGAME+password
        );
    }

    public void joinGame(String code, String password) {
        if(null == password){
            password = "";
        }
        sendMessage(
                ServerCallbacks.CODE_PUSH+code+"\r\n"+
                ServerCallbacks.CODE_POP+"1"+
                ServerCallbacks.CODE_JOINGAME+password
        );
    }

    public void leaveGame(){
        sendMessage(ServerCallbacks.CODE_LEAVEGAME);
    }

    public void startGame(){
        sendMessage(ServerCallbacks.CODE_BEGINGAME);
    }

    public void sendAnswer(int answer, long epoc){
        sendMessage(
                ServerCallbacks.CODE_PUSH+epoc+"\r\n"+
                ServerCallbacks.CODE_POP+"1"+
                ServerCallbacks.CODE_ANSWERQUESTION+answer
        );
    }

    public void sendQNA(String q, String a1, String a2, String a3, String a4, int correct){
        sendMessage(
                ServerCallbacks.CODE_PUSH+q+"\r\n"+
                ServerCallbacks.CODE_PUSH+a1+"\r\n"+
                ServerCallbacks.CODE_PUSH+a2+"\r\n"+
                ServerCallbacks.CODE_PUSH+a3+"\r\n"+
                ServerCallbacks.CODE_PUSH+a4+"\r\n"+
                ServerCallbacks.CODE_POP+"5"+
                ServerCallbacks.CODE_SENDQUESTIONANSWER+correct
        );
    }






    // WORK ON write message functions

    /**
     * @param message The message to write
     */
    public void sendMessage(String message){
        if(message.equals("")){return;}
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
        if(isConnected()) {
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
