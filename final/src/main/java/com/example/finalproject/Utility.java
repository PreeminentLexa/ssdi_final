package com.example.finalproject;

import com.example.finalproject.controllers.UtilControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// This is a static class that acts like a library for control
public class Utility {
    ////////////
    // Config //
    ////////////
    public static String pageTitle = "Quiz game!";
    public static int pageWidth = 600;
    public static int pageHeight = 400;
    public static String stylesheet = Utility.class.getResource("mainstyle.css").toExternalForm();
    public static String initialScreen = "a_connectscreen.fxml";
    public static final int timeOnCorrectAnswerPage = 10;
    public static final int timeOnScoreboardPage = 10; // The server needs a variable for these two added, so it can time the "next round start"



    ////////////////////
    // Global objects //
    ////////////////////
    public static String myUniqueID;
    public static Connection server;
    public static Game game;
    public static Stage stage = null; // The stage to which all pages are parented. Set in FinalProjectApplication.java
    public static UtilControllerBase controller = null;

    /////////////////////
    // UTILITY METHODS //
    /////////////////////

    /** swapToPage - Used to change the primary window to another fxml file
     * @param resource A String, the name of the fxml file (including '.fxml')
     */
    public static void swapToPage(String resource){
        if(null == Utility.stage){
            System.err.println("Utility.stage was not initialized in FinalProjectApplication.java");
            return;
        }

        try {
            URL url = Utility.class.getResource(resource);
            if(null == url){
                System.err.println("Unable to switch to "+resource);
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Scene scene = new Scene(fxmlLoader.load(), Utility.pageWidth, Utility.pageHeight);
            scene.getStylesheets().add(Utility.stylesheet);
            Utility.stage.setTitle(Utility.pageTitle);
            Utility.stage.setScene(scene);
            Utility.stage.show();
            Utility.controller = fxmlLoader.getController();
        } catch(IOException e){
            System.err.println("Unable to switch to "+resource);
            System.err.println(e);
        }
    }

    private static ScheduledFuture upcomingSwap = null;
    /** swapToPageSynced - Used to change the primary window to another fxml file, at a specified time
     * @param resource A String, the name of the fxml file (including '.fxml')
     * @param time A float, the synced time
     */
    public static void swapToPageAtTime(String resource, Instant time){
        Utility.swapToPageAtTime(resource, time, false, null);
    }
    /** swapToPageSynced - Used to change the primary window to another fxml file, at a specified time
     * @param resource A String, the name of the fxml file (including '.fxml')
     * @param time A float, the synced time
     * @param callback A Thread, a callback
     */
    public static void swapToPageAtTime(String resource, Instant time, Runnable callback){
        Utility.swapToPageAtTime(resource, time, false, callback);
    }
    /** swapToPageSynced - Used to change the primary window to another fxml file, at a specified time
     * @param resource A String, the name of the fxml file (including '.fxml')
     * @param time A float, the synced time
     * @param doCountPage A boolean, true to show a count down page
     */
    public static void swapToPageAtTime(String resource, Instant time, boolean doCountPage){
        Utility.swapToPageAtTime(resource, time, doCountPage, null);
    }
    /** swapToPageSynced - Used to change the primary window to another fxml file, at a specified time
     * @param resource A String, the name of the fxml file (including '.fxml')
     * @param time A float, the synced time
     * @param doCountPage A boolean, true to show a count down page
     * @param callback A Thread, a callback
     */
    public static void swapToPageAtTime(String resource, Instant time, boolean doCountPage, Runnable callback){
        long wait = Duration.between(time, Instant.now()).toMillis();
        if(doCountPage){
            Utility.swapToPage("util-countdown.fxml");
            Utility.controller.Util_CountDownTimeLeft(wait, time);
        }
        if(null != upcomingSwap){
            upcomingSwap.cancel(true);
        }
        upcomingSwap.cancel(true);
        upcomingSwap = Executors.newSingleThreadScheduledExecutor().schedule(
                () -> {
                    Utility.swapToPage(resource);
                    if(null != callback){
                        callback.run();
                    }
                }, wait, TimeUnit.MILLISECONDS
        );
    }


    /** serverConnectionLost - From Server - connection lost
     */
    public static void serverConnectionLost(){
        Utility.controller.Callback_connectionLost();
        Utility.swapToPage(Utility.initialScreen);
        for(User u : User.getAllUsers()){
            u.delete();
        }
        Utility.server.close();
        Utility.swapToPage("a_connectscreen.fxml");
    }

    /** gameClosed - From Server - The local user has been disconnected from the current game
     */
    public static void gameClosed(){
        Utility.controller.Callback_gameClosed();
        for(User u : User.getAllUsers()){
            if(!u.isLocalUser()){
                u.delete();
            }
        }
        Utility.game = null;
        Utility.swapToPage("c_mainmenu.fxml");
    }

    /** joinServer - From Controller (A) - Attempts to join the specified IP
     * @param ip A string, the IP (including port)
     * @param username A string, the username of the local user
     * @param imageNumber An integer, the image to use (not implemented)
     */
    public static void joinServer(String ip, String username, int imageNumber){
        Utility.swapToPage("util-pending.fxml");
        Utility.server = new Connection(ip);
        if(Utility.server.isConnected()){
            User.getLocalUser().setUsername(username);
            User.getLocalUser().setImageIndex(imageNumber);
            Utility.server.setUsername(username);
            Utility.server.setImage(imageNumber);
            Utility.joinServer_success();
        } else {
            Utility.joinServer_failed();
        }
    }

    /** getUID - From Server - After a successful connection, the server tells us our unique ID
     * @param uid The ID that the server uses for the local client
     */
    public static void getUID(String uid){
        User.getLocalUser().setUID(uid);
    }

    /** joinServer_failed - From Utility.joinServer - Failed server connection
     */
    public static void joinServer_failed(){
        Utility.swapToPage("a_connectscreen.fxml");
        Utility.controller.Callback_errorMessage("Failed to connect");
    }

    /** joinServer_success - From Utility.joinServer - Successful server connection
     */
    public static void joinServer_success(){
        Utility.swapToPage("c_mainmenu.fxml");
    }

    /** mainMenu_newGame - From Controller (C) - Swaps to frame D
     */
    public static void mainMenu_newGame(){
        Utility.swapToPage("d_creategame.fxml");
    }

    /** mainMenu_joinGame - From Controller (C) - Swaps to frame F
     */
    public static void mainMenu_joinGame(){
        Utility.swapToPage("f_joingame.fxml");
    }

    /** mainMenu_disconnect - From Controller (C) - Swaps to frame A and disconnects server socket
     */
    public static void mainMenu_disconnect(){
        User oldUserObj = User.getLocalUser();
        String ip = Utility.server.getAddress();
        Utility.serverConnectionLost();
        Utility.controller.Callback_previousConnectInputs(ip, oldUserObj.getUsername(), oldUserObj.getImageIndex());
    }

    /** createGame_back - From Controller (D) - Returns to frame C (the main menu)
     */
    public static void createGame_back(){
        Utility.swapToPage("c_mainmenu.fxml");
    }

    /** createGame_create - From Controller (D) - Tells the server to create a game
     * @param settings A String array, an arbitrary amount of settings, in the form "key|type|value" where the type is "s", "i", "f" for string, integer, float
     * @param password A String, the password of the game, null or "" for an unprotected game
     */
    private static String[] tempSettings;
    public static void createGame_create(String[] settings, String password){
        Utility.swapToPage("e_awaitingusers.fxml");
        Utility.tempSettings = settings;
        // WORK ON send settings and password to server
    }

    /** joinGame_back - From Controller (F) - Returns to frame C (the main menu)
     */
    public static void joinGame_back(){
        Utility.swapToPage("c_mainmenu.fxml");
    }

    /** joinGame_join - From Controller (F) - Asks the server to join a game
     * @param code A String, the code of the game
     * @param password A String, the password of the game, it doesn't matter if the game is unprotected
     */
    private static String tempCode;
    private static String tempPass;
    public static void joinGame_join(String code, String password){
        Utility.swapToPage("util-pending.fxml");
        Utility.tempCode = code;
        Utility.tempPass = password;
        // WORK ON send code and password to server
    }

    /** joinGame_failed_code - From Server - The request to join a game failed, because the given code isn't a usable game
     */
    public static void joinGame_failed_code(){
        Utility.swapToPage("f_joingame.fxml");
        Utility.controller.Callback_errorPrevJoinGameInputs(Utility.tempCode, Utility.tempPass);
        Utility.tempCode = null;
        Utility.tempPass = null;
        Utility.controller.Callback_errorMessage("Invalid Code");
    }

    /** joinGame_failed_password - From Server - The request to join a game failed, because the given password was wrong for this game
     */
    public static void joinGame_failed_password(){
        Utility.swapToPage("f_joingame.fxml");
        Utility.controller.Callback_errorPrevJoinGameInputs(Utility.tempCode, Utility.tempPass);
        Utility.tempCode = null;
        Utility.tempPass = null;
        Utility.controller.Callback_errorMessage("Incorrect Password");
    }

    /** awaitingUsers_connected - From Server - When the local user joins a game (where they are not a host)
     * @param settings A String array, an arbitrary amount of settings, in the form "key|type|value" where the type is "s", "i", "f" for string, integer, float
     */
    public static void awaitingUsers_connected(String[] settings){
        Utility.swapToPage("e_awaitingusers.fxml");
        GameSettings gameSettings = new GameSettings();
        for(String setting : settings){
            String[] settingBits = setting.split("|");
            gameSettings.handleSetting(settingBits[0],settingBits[1],settingBits[2]);
        }
        Utility.game = new Game(gameSettings);
        Utility.game.setCode(Utility.tempCode);
        Utility.controller.Callback_getGameCode(Utility.tempCode);
        Utility.controller.Callback_getGameSettings(gameSettings);
        Utility.tempCode = null;
        Utility.tempPass = null;
    }

    /** awaitingUsers_receivedCode - From Server - When the local user creates a game (where they are the host)
     * @param code A String, the code of the created server
     */
    public static void awaitingUsers_receivedCode(String code){
        GameSettings gameSettings = new GameSettings();
        for(String setting : tempSettings){
            String[] settingBits = setting.split("|");
            gameSettings.handleSetting(settingBits[0],settingBits[1],settingBits[2]);
        }
        Utility.game = new Game(gameSettings);
        Utility.game.setCode(code);
        Utility.controller.Callback_getGameCode(Utility.tempCode);
        Utility.controller.Callback_getGameSettings(gameSettings);
        Utility.tempSettings = null;
    }

    /** awaitingUsers_back - From Controller (E) - Return to frame C (the main menu)
     */
    public static void awaitingUsers_back(){
        Utility.swapToPage("c_mainmenu.fxml");
    }

    /** awaitingUsers_userJoined - From Server - When a user joins the server that the local client is in
     * @param uid A String, the unique user id of the joined user
     * @param username A String, the username of the joined user
     * @param imageIndex An int, the image index of the joined user
     */
    public static void awaitingUsers_userJoined(String uid, String username, int imageIndex){
        User user = new User(uid, username, imageIndex);
        Utility.controller.Callback_userJoined(user);
    }

    /** awaitingUsers_userLeft - From Server - When a user leaves the server
     * @param uid A String, the unique user id of the leaving user
     */
    public static void awaitingUsers_userLeft(String uid){
        User user = User.removeUser(uid);
        Utility.controller.Callback_userLeft(user);
    }

    /** awaitingUsers_startGame - From Controller (E) Host only - Used to request that the game begins
     */
    public static void awaitingUsers_startGame(){
        // WORK ON, Send to server
    }

    /** awaitingUsers_gameStarted - From Server - The round has begun
     * @param uid A String, the unique user id of the questioner this round
     */
    public static void awaitingUsers_gameStarted(String uid){
        Utility.game.incrementRound();
        if(User.getLocalUser().getUID().equals(uid)){
            Utility.swapToPage("g_inputquestion.fxml");
        } else {
            Utility.swapToPage("h_waitingforquestioner.fxml");
            Utility.controller.Callback_waitingForQuestioner(User.getUser(uid));
        }
    }

    /** inputQuestion_pickQuestion - From Controller (G) - Used to transition to frame J
     * @param question A String, the question
     */
    private static String tempQuestion;
    public static void inputQuestion_pickQuestion(String question){
        Utility.swapToPage("j_inputanswers.fxml");
        inputAnswer_getQuestion(question);
        Utility.tempQuestion = question;
    }

    /** inputAnswer_back - From Controller (J) - Returns to frame G (safely keeping the question variable)
     */
    public static void inputAnswer_back(){
        Utility.swapToPage("g_inputquestion.fxml");
        Utility.controller.Callback_getQuestion(tempQuestion);
        Utility.tempQuestion = null;
    }

    /** inputAnswer_getQuestion - From Utility.inputQuestion_pickQuestion - Sends the question from frame G to J
     * @param question A String, the question to move from G to J
     */
    public static void inputAnswer_getQuestion(String question){
        Utility.controller.Callback_getQuestion(question);
    }

    /** inputAnswer_pickAnswers - From Controller (J) - Finalizes the questioner phase, by sending the question, answers, and correct answer to the server
     * @param q A String, the question
     * @param a1 A String, the first answer
     * @param a2 A String, the second answer
     * @param a3 A String, the third answer
     * @param a4 A String, the fourth answer
     * @param correct An int, the number of the correct answer (1-4)
     */
    public static void inputAnswer_pickAnswers(String q, String a1, String a2, String a3, String a4, int correct){
        Utility.swapToPage("k_inputanswers.fxml");
        waitingForAnswerers_questionAnswers(q, a1, a2, a3, a4, correct);
        // WORK ON, send to server
    }

    /** waitingForAnswerers_questionAnswers - From Utility.inputAnswer_getQuestion - Moves the question, answers, and correct answer from frame J to K
     * @param q A String, the question
     * @param a1 A String, the first answer
     * @param a2 A String, the second answer
     * @param a3 A String, the third answer
     * @param a4 A String, the fourth answer
     * @param correct An int, the number of the correct answer (1-4)
     */
    public static void waitingForAnswerers_questionAnswers(String q, String a1, String a2, String a3, String a4, int correct){
        Utility.controller.Callback_getQuestion(q);
        Utility.controller.Callback_getAnswers(a1, a2, a3, a4, correct);
    }

    /** waitingForAnswerers_userPicksAnswer - From Server - Tells the questioner/local client/frame K that an answerer has answered
     * @param picked The number of the answer selected by the user (1-4)
     */
    public static void waitingForAnswerers_userPicksAnswer(int picked){
        Utility.controller.Callback_someoneAnswered(picked);
    }

    /** waitingForQuestioner_questionerDone - From Server - Tells all answerers that it's time to answer
     * @param epoc A Long, the unix timestamp of the time we will switch to i_answering.fxml (because it's a race to answer, everyone needs to start at the same time)
     * @param q A String, the Question
     * @param a1 A String, the first answer
     * @param a2 A String, the second answer
     * @param a3 A String, the third answer
     * @param a4 A String, the fourth answer
     */
    public static void waitingForQuestioner_questionerDone(long epoc, String q, String a1, String a2, String a3, String a4){
        Utility.swapToPageAtTime("i_answering.fxml",
                Instant.ofEpochSecond(epoc),
                true,
                () -> Utility.answering(q, a1, a2, a3, a4)
        );
    }

    /** answering - From Utility.waitingForQuestioner_questionerDone (via Utility.swapToPageAtTime) - Gives the question and all answers to Frame I
     * @param q A String, the Question
     * @param a1 A String, the first answer
     * @param a2 A String, the second answer
     * @param a3 A String, the third answer
     * @param a4 A String, the fourth answer
     */
    public static void answering(String q, String a1, String a2, String a3, String a4){
        Utility.controller.Callback_getQuestion(q);
        Utility.controller.Callback_getAnswers(a1, a2, a3, a4, -1);
    }

    /** answering_pick - From Controller (I) - Picks an answer and tells the server about it
     * @param answer The number of the selected answer (1-4)
     */
    public static void answering_pick(int answer){
        long epoc = Instant.now().toEpochMilli();
        // WORK ON, send to server
    }

    /** allAnsweredOrTimeDone - From Server - When all the users have answered, or the the time has ran out
     * @param q A String, the Question
     * @param a1 A String, the first answer
     * @param a2 A String, the second answer
     * @param a3 A String, the third answer
     * @param a4 A String, the fourth answer
     * @param a1num An int, the quantity of users who selected the first answer
     * @param a2num An int, the quantity of users who selected the second answer
     * @param a3num An int, the quantity of users who selected the third answer
     * @param a4num An int, the quantity of users who selected the fourth answer
     * @param correct An int, the correct answer
     */
    public static void allAnsweredOrTimeDone(String q, String a1, String a2, String a3, String a4, int a1num, int a2num, int a3num, int a4num, int correct){
        // When this happens, if we're not on the final round, the server needs to set a timer for (Utility.timeOnCorrectAnswerPage+Utility.timeOnScoreboardPage) seconds, to start the next round
        Utility.swapToPage("l_correctanswerreveal.fxml");
        Utility.controller.Callback_getQuestion(q);
        Utility.controller.Callback_getAnswers(a1, a2, a3, a4, correct);
        Utility.controller.Callback_getAnswerCount(a1num, a2num, a3num, a4num);
        Utility.swapToPageAtTime("m_scoreboard.fxml", Instant.now().plusSeconds(Utility.timeOnCorrectAnswerPage));
        if(Utility.game.isFinalRound()){
            Utility.swapToPageAtTime("n_endgame.fxml", Instant.now().plusSeconds(Utility.timeOnCorrectAnswerPage+Utility.timeOnScoreboardPage));
        }
    }

    /** userScoreReceived - From Server - Updates the score of each user, this is done one at a time whilst the clients are looking at the 10 second correct answer reveal
     * @param uid A String, the unique user ID of the user being updated
     * @param score An int, the absolute score of the user
     */
    public static void userScoreReceived(String uid, int score){
        User.getUser(uid).setScore(score);
    }

    /** endGame_returnToMainMenu - From Controller (N) - Returns to frame C (the main menu)
     */
    public static void endGame_returnToMainMenu(){
        Utility.swapToPage("c_mainmenu.fxml");
    }
}
