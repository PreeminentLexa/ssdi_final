package com.example.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectedServer implements Runnable {
    private Socket socket; // The server's socket object
    private BufferedReader in; // The input reader
    private boolean shouldClose = false; // This is true if the connection was opened, but not cleaned up
    public ConnectedServer(Socket serverSocket){
        socket = serverSocket;
        this.shouldClose = true;
    }

    /** getID - IP:Port
     * @return A String, The print name
     */
    public String getID() {
        return this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort();
    }

    private static Pattern leadingInt = Pattern.compile("^([0-9]+)(.*)$"); // Takes a leading integer from a value in match(1) and the rest in match(2)

    private InputStack inputStack; // A stack of previous messages, things are added like PSHMessage
    /** handleInput - Handles messages from the client to the server
     * @param input A String, the line from the client
     */
    private void handleInput(String input){
//        System.out.println(input);
        if(input.equals("")){return;}
        String cmd = input.substring(0,3);
        input = input.substring(3);
        String[] pastInputs = null; // Only used for commands that need lots of messages in a row
        int inputFlags = 0x0000;

        if(cmd.equals(ServerCallbacks.CODE_POP)){
            inputFlags = inputFlags | ServerCallbacks.FLAG_MULTIPLEINPUTS;
            // pop the quantity of inputs from the stack;
            // Message looks like "(POP)(15)(CMD)(Message)"
            Matcher m = leadingInt.matcher(input);
            int popAmount;
            if(m.find()){
                popAmount = Integer.parseInt(m.group(1));
                cmd = m.group(2).substring(0,3);
                input = m.group(2).substring(3);
            } else {
                popAmount = 1;
                cmd = input.substring(0,3);
                input = input.substring(3);
            }
            pastInputs = new String[popAmount];
            for(int i = 0;i < popAmount;i++){
                if(null == inputStack){
                    inputFlags = inputFlags | ServerCallbacks.FLAG_INPUTSTACKOVERFLOW;
                    String[] newPastInputs = new String[i];
                    for(int j = i;j < pastInputs.length;j++){
                        newPastInputs[j] = pastInputs[j];
                    }
                    pastInputs = newPastInputs;
                    break;
                }
                pastInputs[pastInputs.length-1-i] = inputStack.getInput();
                inputStack = inputStack.getPrev();
            }
        }

        // I'd rather do callbacks, but I'm not sure if Java can easily do that (without a class per callback)
        // So this will be hard coded instead. ClientCallbacks is just a library thing to keep organized
        switch(cmd){
            case ServerCallbacks.CODE_PUSH: // push the input to the inputStack, because it'll be needed later
                inputStack = new InputStack(inputStack, input);
                break;
            case ServerCallbacks.CODE_GIVEUID: // after connection, the local client's unique ID (according to the server) is sent
                ServerCallbacks.givenUID(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_JOINGAMEFAILED:
                ServerCallbacks.joinGameFailed(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_GAMESETTINGS:
                ServerCallbacks.gameSettings(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_GAMECODE:
                ServerCallbacks.gameCode(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_HOSTJOINED:
                ServerCallbacks.hostJoined(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_USERJOINED:
                ServerCallbacks.playerJoined(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_USERLEFT:
                ServerCallbacks.playerLeft(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_GAMESTARTED:
                ServerCallbacks.gameStarted(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_QUESTIONERFINISHED:
                ServerCallbacks.questionerFinished(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_ANSWERPICKED:
                ServerCallbacks.answererPickedAnswer(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_ANSWERSGIVEN:
                ServerCallbacks.allAnswersGiven(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_PLAYERSCORES:
                ServerCallbacks.updatePlayerScore(this, input, pastInputs, inputFlags);
                break;
            case ServerCallbacks.CODE_GAMEDISCONNECT:
                ServerCallbacks.gameDisconnect(this, input, pastInputs, inputFlags);
                break;
            default:
                System.out.println("Unhandled command \""+cmd+"\"");
        }













    }

    @Override
    public void run() {
        // WORK ON, read stuff to ServerCallbacks
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String line;
        try {
            while(null != (line = this.in.readLine())){
                handleInput(line);
            }
        } catch (IOException e) {
            System.out.println("Problem reading from "+getID()+", "+e.getMessage());
            Utility.Think.setConnectionClosedFlag(true);
        } finally {
            close();
        }
    }

    public void close(){
        System.out.println("Closing connection with "+getID());
        if(null != this.in){
            try {
                this.in.close();
                this.in = null;
            } catch (IOException e) {}
        }
        if(this.socket.isConnected() && this.shouldClose){
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.shouldClose = false;
    }
}
