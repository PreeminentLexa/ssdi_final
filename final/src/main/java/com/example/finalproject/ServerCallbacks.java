package com.example.finalproject;

public class ServerCallbacks {
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
    public static final String CODE_SETIMAGE = "IMG"; // C->S
    public static final String CODE_CREATEGAME = "CGM"; // C->S
    public static final String CODE_JOINGAME = "JGM"; // C->S
    public static final String CODE_JOINGAMEFAILED = "JGF"; // S->C
    public static final String CODE_GAMESETTINGS = "GST"; // S->C
    public static final String CODE_GAMECODE = "GCD"; // S->C
    public static final String CODE_HOSTJOINED = "HST"; // S->C
    public static final String CODE_USERJOINED = "USJ"; // S->C
    public static final String CODE_USERLEFT = "USL"; // S->C
    public static final String CODE_BEGINGAME = "BGM"; // C->S
    public static final String CODE_GAMESTARTED = "GMS"; // S->C
    public static final String CODE_SENDQUESTIONANSWER = "SQA"; // C->S
    public static final String CODE_QUESTIONERFINISHED = "QFN"; // S->C
    public static final String CODE_ANSWERQUESTION = "AQU"; // C->S
    public static final String CODE_ANSWERPICKED = "APK"; // S->C
    public static final String CODE_ANSWERSGIVEN = "AGV"; // S->C
    public static final String CODE_PLAYERSCORES = "PSC"; // S->C
    public static final String CODE_GAMEDISCONNECT = "GDC"; // S->C
    public static final String CODE_LEAVEGAME = "LGM"; // C->S


    // maybe a bunch of static methods with the arguments
    // public static void func(ConnectedClient client, String input, String[] pastPoppedInputs, int flags){}


    /** givenUID - Callback from UID command. Used to set the unique ID
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void givenUID(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.getUID(input);
    }

    /** joinGameFailed - Callback from GST command. Used to set the game settings of a joined game
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void joinGameFailed(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.Think.setJoinFailedFlag(true, input);
    }

    /** gameSettings - Callback from GST command. Used to set the game settings of a joined game
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void gameSettings(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.Think.setJoinedGameFlag(true, pastPoppedInputs);
    }

    /** gameCode - Callback from GCD command. Used to set the game code of a created game
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void gameCode(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.awaitingUsers_receivedCode(input);
    }

    /** hostJoined - Callback from HST command. The host joins (this is done to the non host when the non host joins a game)
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void hostJoined(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.awaitingUsers_userJoined(input, pastPoppedInputs[0], Integer.parseInt(pastPoppedInputs[1]), true);
    }

    /** playerJoined - Callback from USJ command. User has joined a game
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void playerJoined(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.awaitingUsers_userJoined(input, pastPoppedInputs[0], Integer.parseInt(pastPoppedInputs[1]), false);
    }

    /** playerLeft - Callback from LGM command. User has left a game
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void playerLeft(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.awaitingUsers_userLeft(input);
    }

    /** gameStarted - Callback from GMS command. Round has started
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void gameStarted(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.Think.setGameStartedFlag(true, input);
    }

    /** questionerFinished - Callback from QFN command. Round has started
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void questionerFinished(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.Think.setQuestionerFinishedFlag(true, Long.parseLong(input), pastPoppedInputs);
    }

    /** answererPickedAnswer - Callback from APK command. Round has started
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void answererPickedAnswer(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.waitingForAnswerers_userPicksAnswer(Integer.parseInt(input));
    }

    /** allAnswersGiven - Callback from AGV command. Round has started
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void allAnswersGiven(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.Think.setDoneAnsweringFlag(true, new String[]{
                pastPoppedInputs[0],
                pastPoppedInputs[1],
                pastPoppedInputs[2],
                pastPoppedInputs[3],
                pastPoppedInputs[4],
        }, new int[]{
                Integer.parseInt(pastPoppedInputs[5]),
                Integer.parseInt(pastPoppedInputs[6]),
                Integer.parseInt(pastPoppedInputs[7]),
                Integer.parseInt(pastPoppedInputs[8]),
                Integer.parseInt(input),

        });
        Utility.waitingForAnswerers_userPicksAnswer(Integer.parseInt(input));
    }











    /** gameDisconnect - Callback from GDC command. Used to disconnect a user from a game forcibly
     * @param server A ConnectedServer, the connected server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void gameDisconnect(ConnectedServer server, String input, String[] pastPoppedInputs, int flags) {
        Utility.Think.setGameClosedFlag(true);
    }
}
