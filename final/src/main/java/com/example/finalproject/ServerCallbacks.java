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


    /** setUID - Callback from UID command. Used to set the unique ID
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void setUID(String input, String[] pastPoppedInputs, int flags) {

    }

    /** setGameSettings - Callback from GST command. Used to get game settings from the server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void setGameSettings(String input, String[] pastPoppedInputs, int flags) {
    }

    /** setGameCode - Callback from GCD command. Used to get game code from the server
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void setGameCode(String input, String[] pastPoppedInputs, int flags) {
//        Utility.controller.Callback_gameCodeReceived(input);
    }

    /** userJoined - Callback from USJ command. Used when a user joins
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void userJoined(String input, String[] pastPoppedInputs, int flags) {
//        Utility.controller.Callback_userJoined(
//                pastPoppedInputs[0],
//                pastPoppedInputs[1],
//                Integer.parseInt(input)
//        );
    }

    /** userLeft - Callback from USJ command. Used when a user leaves
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void userLeft(String input, String[] pastPoppedInputs, int flags) {
//        Utility.controller.Callback_userLeft(input);
    }

    /** userLeft - Callback from USJ command. Used when a user leaves
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void gameStarted(String input, String[] pastPoppedInputs, int flags) {
    }

    /** userLeft - Callback from USJ command. Used when a user leaves
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void questionsFinished(String input, String[] pastPoppedInputs, int flags) {
    }

    /** userLeft - Callback from USJ command. Used when a user leaves
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void someoneAnswered(String input, String[] pastPoppedInputs, int flags) {
//        Utility.controller.Callback_someoneAnswered(Integer.parseInt(input));
    }

    /** userLeft - Callback from USJ command. Used when a user leaves
     * @param input A String, the content of this command
     * @param pastPoppedInputs A String array, the previous inputs that were popped off of the stack
     * @param flags An integer, a set of flags, defined in ConnectedClient.java
     */
    public static void userScores(String input, String[] pastPoppedInputs, int flags) {
    }
}
