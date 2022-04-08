public class ClientCallbacks {
    // Flags
    public static int FLAG_MULTIPLEINPUTS = 0x0001; // flag for when a command requests previous inputs, something like (POP)(1)(CMD)(Message)
    public static int FLAG_INPUTSTACKOVERFLOW = 0x0002; // flag for when a command requests more inputs than exist
                                                        // E.G.    PSHMessageA, PSHMessageB, PSHMessageC, POP4CMDMessageD
                                                        // This will Push "MessageA", "MessageB", and "MessageC" then attempt to read 4 values from the stack

    // Codes
    public static final String CODE_PUSH = "PSH"; // Multi Line Messages
    public static final String CODE_POP = "POP"; // Multi Line Messages


    public static final String CODE_GIVEUID = "UID"; // S->C
    public static final String CODE_SETUSERNAME = "USR"; // C->S
    public static final String CODE_CREATEGAME = "CGM"; // C->S
    public static final String CODE_JOINGAME = "JGM"; // C->S
    public static final String CODE_GAMESETTINGS = "GST"; // S->C
    public static final String CODE_GAMECODE = "GCD"; // S->C
    public static final String CODE_USERJOINED = "USJ"; // S->C
    public static final String CODE_USERLEFT = "USL"; // S->C
    public static final String CODE_BEGINGAME = "BGM"; // C->S
    public static final String CODE_GAMESTARTED = "GMS"; // S->C
    public static final String CODE_SENDQUESTIONANSWER = "SQA"; // C->S
    public static final String CODE_QUESTIONERFINISHED = "QFN"; // S->C
    public static final String CODE_ANSWERQUESTION = "AQU"; // C->S
    public static final String CODE_ANSWERSGIVEN = "AGV"; // S->C
    public static final String CODE_PLAYERSCORES = "PSC"; // S->C


    // maybe a bunch of static methods with the arguments
    // public static void func(ConnectedClient client, String input, String[] pastPoppedInputs, int flags){}


    /** setUsername - Callback from USR command. Used to set the username of this client
     * @param client A ConnectedClient, the client who sent this command
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void setUsername(ConnectedClient client, String input, String[] pastPoppedInputs, int flags) {
        client.setUsername(input);
    }

    /** createGame - Callback from DBG command, Creates a game with certain settings
     * @param client A ConnectedClient, the client who sent this command
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void createGame(ConnectedClient client, String input, String[] pastPoppedInputs, int flags) {
        GameSettings settings = new GameSettings();
        if(0!=(flags&FLAG_MULTIPLEINPUTS)){
//            pastPoppedInputs is an array of settings in "key|type|value", type must be "s", "i", or "f"
            for(String keyvalue : pastPoppedInputs){
                String[] setting = keyvalue.split("|");
                settings.handleSetting(setting[0],setting[1],setting[2]);
            }
        }

        String password = ("" == input ? null : input);
        Game game = new Game(settings, password);
        game.setHost(client);
    }

    /** joinGame - Callback from JGM command, Used to join a game
     * @param client A ConnectedClient, the client who sent this command
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void joinGame(ConnectedClient client, String input, String[] pastPoppedInputs, int flags) {
        if(0==(flags&FLAG_MULTIPLEINPUTS) || pastPoppedInputs.length != 1) {
            // send error has occured
            return;
        }
        Game gameToJoin = Utility.getGame(pastPoppedInputs[0]);
        if(null == gameToJoin){
            // send no game found
        }
        if(gameToJoin.checkPassword(input)){
            gameToJoin.addUser(client);
            // send connection confirmation
        } else {
            // send incorrect password
        }
    }

    /** startGame - Callback from BGM command, Used to start the game
     * @param client A ConnectedClient, the client who sent this command
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void startGame(ConnectedClient client, String input, String[] pastPoppedInputs, int flags) {
    }

    /** sendQuestion - Callback from SQA command, Used to send a question and answers to the server
     * @param client A ConnectedClient, the client who sent this command
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void sendQuestionAnswer(ConnectedClient client, String input, String[] pastPoppedInputs, int flags) {
        Game game = client.getGame();
        if(null == game){
            // send no game error
            return;
        }
        String question = pastPoppedInputs[0];
        String answer1 = pastPoppedInputs[1];
        String answer2 = pastPoppedInputs[2];
        String answer3 = pastPoppedInputs[3];
        String answer4 = pastPoppedInputs[4];
        int correctAnswer = Integer.parseInt(input);
        game.setCurrentQA(question, answer1, answer2, answer3, answer4, correctAnswer);
    }

    /** sendQuestion - Callback from SQU command, Used to send a question to the server
     * @param client A ConnectedClient, the client who sent this command
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void answerQuestion(ConnectedClient client, String input, String[] pastPoppedInputs, int flags) {
    }
}