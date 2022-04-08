import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectedClient implements Runnable {
    private Socket socket; // The client's socket object
    private PrintWriter out; // The output writer
    private BufferedReader in; // The input reader
    private boolean shouldClose = false; // This is true if the connection was opened, but not cleaned up
    private String username = null; // The username of this user (This might be good initializing info)

    /** ConnectedClient, the constructor
     * @param clientSocket A Socket, the client
     */
    public ConnectedClient(Socket clientSocket){
        this.socket = clientSocket;
        System.out.println("Client connecting "+getID());
        ConnectedClient cc_this = this;
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                // When the application closes, make sure to terminate connections gracefully.
                cc_this.close();
            }
        });
        this.shouldClose = true;

        try {
            this.out = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** setUsername - setter for the username of this client
     * @param username A String, the new username of this client
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** getID - Either the "IP:Port" or "Username (IP:Port)"
     * @return A String, The print name
     */
    public String getID() {
        String IPPrint = this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort();
        if(null != this.username){
            return this.username + " ("+IPPrint+")";
        }
        return IPPrint;
    }

    private Game game;
    public Game getGame(){
        return this.game;
    }
    public void setGame(Game game){
        this.game = game;
    }

    /** sendMessage - Sends a message to this client
     * @param message A String, the message
     */
    public void sendMessage(String message){
        if(null == this.out){
            System.out.println("Not connected to server, unable to send message.");
            return;
        }
        this.out.println(message);
        this.out.flush();
    }

    private static Pattern leadingInt = Pattern.compile("^([0-9]+)(.*)$"); // Takes a leading integer from a value in match(1) and the rest in match(2)

    private InputStack inputStack; // A stack of previous messages, things are added like PSHMessage
    /** handleInput - Handles messages from the client to the server
     * @param input A String, the line from the client
     */
    public void handleInput(String input){
        String cmd = input.substring(0,3);
        input = input.substring(3);
        String[] pastInputs = null; // Only used for commands that need lots of messages in a row - not sure if we'll use this, but it seems good to have
        int inputFlags = 0x0000;

        if(cmd.equals("POP")){
            inputFlags = inputFlags | ClientCallbacks.FLAG_MULTIPLEINPUTS;
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
                    inputFlags = inputFlags | ClientCallbacks.FLAG_INPUTSTACKOVERFLOW;
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
            case ClientCallbacks.CODE_PUSH: // push the input to the inputStack, because it'll be needed later
                inputStack = new InputStack(inputStack, input);
                break;
            case ClientCallbacks.CODE_SETUSERNAME: // set the username of this client
                ClientCallbacks.setUsername(this, input, pastInputs, inputFlags);
                break;
            case ClientCallbacks.CODE_CREATEGAME:
                ClientCallbacks.createGame(this, input, pastInputs, inputFlags);
                break;
            case ClientCallbacks.CODE_JOINGAME:
                ClientCallbacks.joinGame(this, input, pastInputs, inputFlags);
                break;
            case ClientCallbacks.CODE_BEGINGAME:
                ClientCallbacks.startGame(this, input, pastInputs, inputFlags);
                break;
            case ClientCallbacks.CODE_SENDQUESTIONANSWER:
                ClientCallbacks.sendQuestionAnswer(this, input, pastInputs, inputFlags);
                break;
            case ClientCallbacks.CODE_ANSWERQUESTION:
                ClientCallbacks.answerQuestion(this, input, pastInputs, inputFlags);
                break;
            default:
                System.out.println("Unhandled command \""+cmd+"\"");
        }
    }

    /** run - This is the main thread, it is where the reader is opened, and this loops to deal with any input from the client
     */
    @Override
    public void run() {
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
