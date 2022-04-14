package com.example.finalproject;

import com.example.finalproject.controllers.UtilControllerBase;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
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
    public static void swapToPage(String resource) {
        swapToPage(resource, null);
    }
    private static void swapToPage(String resource, StackTraceElement ste){
        System.out.print("Swapping to "+resource);


        if(null == ste){
            ste = Thread.currentThread().getStackTrace()[2];
        }
        System.out.println(
                " from "+
                        ste.getClassName()+"."+ste.getMethodName()+
                        "("+ste.getFileName()+":"+ste.getLineNumber()+")"
        );

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
        long wait = Duration.between(Instant.now(), time).toMillis();
//        System.out.println("Waiting for "+wait+"ms");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        if(doCountPage){
            Utility.swapToPage("util-countdown.fxml", ste);
            Utility.controller.Util_CountDownTimeLeft(wait, time);
        }
        if(null != upcomingSwap){
            upcomingSwap.cancel(true);
        }
        Utility.Think.setSchedulePageFlagPickups(resource, ste, callback);
        upcomingSwap = Executors.newSingleThreadScheduledExecutor().schedule(
                () -> {
//                    Utility.swapToPage(resource, ste);
                    Utility.Think.setSchedulePageFlag(true);
                }, wait, TimeUnit.MILLISECONDS
        );
    }

    public static void initialize(Stage stage){
        Utility.stage = stage;
        Utility.swapToPage(Utility.initialScreen);
        Utility.createThinkHook();
    }

    public static void createThinkHook(){
        // This isn't very good code, but I need to trigger stuff
        // in the FX Application Thread from the ConnectedServer read thread

        Platform.runLater(Utility::createThinkHook);
        Think.checkFlags();
    }
    public static class Think {
        public static void checkFlags(){
            if(Think.flag_ConnectionClosed){onConnectionClosedFlag();}
            if(Think.flag_GameClosed){onGameClosedFlag();}
            if(Think.flag_JoinedGame){onJoinedGameFlag();}
            if(Think.flag_JoinFailed){onJoinFailedFlag();}
            if(Think.flag_GameStarted){onGameStartedFlag();}
            if(Think.flag_QuestionerFinished){onQuestionerFinishedFlag();}
            if(Think.flag_SchedulePage){onSchedulePageFlag();}
            if(Think.flag_DoneAnswering){onDoneAnsweringFlag();}
        }

        /////////////////////////////////
        // Connection To Server Closed //
        /////////////////////////////////
        public static boolean flag_ConnectionClosed = false;
        public static void setConnectionClosedFlag(boolean val){
            flag_ConnectionClosed = val;
        }
        private static void onConnectionClosedFlag(){
            Utility.serverConnectionLost();
            flag_ConnectionClosed = false;
        }

        /////////////////////////
        // Current Game Closed //
        /////////////////////////
        public static boolean flag_GameClosed = false;
        public static void setGameClosedFlag(boolean val){
            flag_GameClosed = val;
        }
        private static void onGameClosedFlag(){
            Utility.gameClosed();
            flag_GameClosed = false;
        }

        /////////////////
        // Joined Game //
        /////////////////
        public static boolean flag_JoinedGame = false;
        public static String[] pickup_JoinedGame1 = null;
        public static void setJoinedGameFlag(boolean val, String[] pickup1){
            pickup_JoinedGame1 = pickup1;
            flag_JoinedGame = val;
        }
        private static void onJoinedGameFlag(){
            Utility.awaitingUsers_connected(pickup_JoinedGame1);
            pickup_JoinedGame1 = null;
            flag_JoinedGame = false;
        }

        /////////////////
        // Join Failed //
        /////////////////
        public static boolean flag_JoinFailed = false;
        public static String pickup_JoinFailed1 = null;
        public static void setJoinFailedFlag(boolean val, String pickup1){
            flag_JoinFailed = val;
            pickup_JoinFailed1 = pickup1;
        }
        private static void onJoinFailedFlag(){
            Utility.joinGame_failed(pickup_JoinFailed1);
            flag_JoinFailed = false;
            pickup_JoinFailed1 = null;
        }

        //////////////////
        // Game Started //
        //////////////////
        public static boolean flag_GameStarted = false;
        public static String pickup_GameStarted1 = null;
        public static void setGameStartedFlag(boolean val, String pickup1){
            flag_GameStarted = val;
            pickup_GameStarted1 = pickup1;
        }
        private static void onGameStartedFlag(){
            awaitingUsers_gameStarted(pickup_GameStarted1);
            flag_GameStarted = false;
            pickup_GameStarted1 = null;
        }

        /////////////////////////
        // Questioner Finished //
        /////////////////////////
        public static boolean flag_QuestionerFinished = false;
        public static long pickup_QuestionerFinished1 = -1;
        public static String[] pickup_QuestionerFinished2 = null;
        public static void setQuestionerFinishedFlag(boolean val, long pickup1, String[] pickup2){
            flag_QuestionerFinished = val;
            pickup_QuestionerFinished1 = pickup1;
            pickup_QuestionerFinished2 = pickup2;
        }
        private static void onQuestionerFinishedFlag(){
            Utility.waitingForQuestioner_questionerDone(pickup_QuestionerFinished1, pickup_QuestionerFinished2[0], pickup_QuestionerFinished2[1], pickup_QuestionerFinished2[2], pickup_QuestionerFinished2[3], pickup_QuestionerFinished2[4]);
            flag_QuestionerFinished = false;
            pickup_QuestionerFinished1 = -1;
            pickup_QuestionerFinished2 = null;
        }

        ///////////////////
        // Schedule Page //
        ///////////////////
        public static boolean flag_SchedulePage = false;
        public static String pickup_SchedulePage1 = null;
        public static StackTraceElement pickup_SchedulePage2 = null;
        public static Runnable pickup_SchedulePage3 = null;
        public static void setSchedulePageFlag(boolean val){
            flag_SchedulePage = val;
        }
        public static void setSchedulePageFlagPickups(String pickup1, StackTraceElement pickup2, Runnable pickup3){
            pickup_SchedulePage1 = pickup1;
            pickup_SchedulePage2 = pickup2;
            pickup_SchedulePage3 = pickup3;
        }
        private static void onSchedulePageFlag(){
            Utility.swapToPage(pickup_SchedulePage1, pickup_SchedulePage2);
            if(null != pickup_SchedulePage3){
                pickup_SchedulePage3.run();
            }
            flag_SchedulePage = false;
            pickup_SchedulePage1 = null;
            pickup_SchedulePage2 = null;
            pickup_SchedulePage3 = null;
        }

        ////////////////////
        // Done Answering //
        ////////////////////
        public static boolean flag_DoneAnswering = false;
        public static String[] pickup_DoneAnswering1 = null;
        public static int[] pickup_DoneAnswering2 = null;
        public static void setDoneAnsweringFlag(boolean val, String[] pickup1, int[] pickup2){
            flag_DoneAnswering = val;
            pickup_DoneAnswering1 = pickup1;
            pickup_DoneAnswering2 = pickup2;
        }
        private static void onDoneAnsweringFlag(){
            Utility.allAnsweredOrTimeDone(pickup_DoneAnswering1[0], pickup_DoneAnswering1[1], pickup_DoneAnswering1[2], pickup_DoneAnswering1[3], pickup_DoneAnswering1[4], pickup_DoneAnswering2[0], pickup_DoneAnswering2[1], pickup_DoneAnswering2[2], pickup_DoneAnswering2[3], pickup_DoneAnswering2[4]);
            flag_DoneAnswering = false;
            pickup_DoneAnswering1 = null;
            pickup_DoneAnswering2 = null;
        }

        //////////////////
        // xxxxxxxxxxxx //
        //////////////////
        public static boolean flag_xxxx = false;
        public static String pickup_xxxx1 = null;
        public static void setXxxxFlag(boolean val, String pickup1){
            flag_xxxx = val;
            pickup_xxxx1 = pickup1;
        }
        private static void onXxxxFlag(){
            Utility.joinGame_failed(pickup_xxxx1);
            flag_xxxx = false;
            pickup_xxxx1 = null;
        }
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
    }
    public static void FROMSERVER_serverConnectionLost(){
        Think.setConnectionClosedFlag(true);
    }

    /** gameClosed - From Server - The local user has been disconnected from the current game
     */
    public static void gameClosed(){
        Utility.controller.Callback_gameClosed();
        for(User u : User.getAllUsers()){
            if(!u.isLocalUser()){
                u.delete();
            } else {
                u.setIsHost(false);
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
        // SWAP TO PAGE MUST REFRESH THE VIEW BEFORE WE CREATE A CONNECTION, OR IT'LL STALL BEFORE OPENING THE PENDING PAGE794613
        Utility.server = new Connection(ip);
        // Mutex multi thread loop
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
        Utility.server.createGame(password, settings);
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
        Utility.server.joinGame(code, password);
    }

    /** joinGame_failed - From Server - The request to join a game failed, because the given code isn't a usable game or the password is wrong
     */
    public static void joinGame_failed(String message){
        Utility.swapToPage("f_joingame.fxml");
        Utility.controller.Callback_errorPrevJoinGameInputs(Utility.tempCode, Utility.tempPass);
        Utility.tempCode = null;
        Utility.tempPass = null;
        Utility.controller.Callback_errorMessage(message);
    }

    /** awaitingUsers_connected - From Server - When the local user joins a game (where they are not a host)
     * @param settings A String array, an arbitrary amount of settings, in the form "key|type|value" where the type is "s", "i", "f" for string, integer, float
     */
    public static void awaitingUsers_connected(String[] settings){
        Utility.swapToPage("e_awaitingusers.fxml");
        GameSettings gameSettings = new GameSettings();
        if(null != settings){
            for(String setting : settings){
                String[] settingBits = setting.split("\\|");
                gameSettings.handleSetting(settingBits[0],settingBits[1],settingBits[2]);
            }
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
        if(null != Utility.tempSettings){
            for(String setting : Utility.tempSettings){
                String[] settingBits = setting.split("\\|");
                gameSettings.handleSetting(settingBits[0],settingBits[1],settingBits[2]);
            }
        }
        User.getLocalUser().setIsHost(true);
        Utility.game = new Game(gameSettings);
        Utility.game.setCode(code);
        Utility.controller.Callback_getGameCode(code);
        Utility.controller.Callback_getGameSettings(gameSettings);
        Utility.tempSettings = null;
    }

    /** awaitingUsers_back - From Controller (E) - Return to frame C (the main menu)
     */
    public static void awaitingUsers_back(){
        Utility.server.leaveGame();
    }

    /** awaitingUsers_userJoined - From Server - When a user joins the server that the local client is in
     * @param uid A String, the unique user id of the joined user
     * @param username A String, the username of the joined user
     * @param imageIndex An int, the image index of the joined user
     * @param isHost A boolean, whether this user is the host
     */
    public static void awaitingUsers_userJoined(String uid, String username, int imageIndex, boolean isHost){
        User user = new User(uid, username, imageIndex);
        user.setIsHost(isHost);
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
        if(!User.getLocalUser().isHost()){return;}
        Utility.server.startGame();
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
        Utility.swapToPage("k_waitingforanswerers.fxml");
        waitingForAnswerers_questionAnswers(q, a1, a2, a3, a4, correct);
        Utility.server.sendQNA(q, a1, a2, a3, a4, correct);
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
        System.out.println("answering callback");
        Utility.controller.Callback_getQuestion(q);
        Utility.controller.Callback_getAnswers(a1, a2, a3, a4, -1);
    }

    /** answering_pick - From Controller (I) - Picks an answer and tells the server about it
     * @param answer The number of the selected answer (1-4)
     */
    public static void answering_pick(int answer){
        long epoc = Instant.now().toEpochMilli();
        Utility.server.sendAnswer(answer, epoc);
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
        Utility.server.leaveGame();
    }
}
