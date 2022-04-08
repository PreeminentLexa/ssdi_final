import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Game {
    private String code; // I might use the code as the salt too?
    private String password;
    private GameSettings settings;

    private static int maxPlayers = 16;

    private ConnectedClient host;
    private ConnectedClient[] players = new ConnectedClient[maxPlayers-1];
    private ConnectedClient questioner;
    private int connectedPlayers = 1;


    private boolean codeIsSet = false;
    private static String hasherAlgorithm = "SHA-256"; // This is just a random hash, it has no significance.
    private static String validCodeCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static int codeLength = 4;


    private void generateCode(){
        if(this.codeIsSet){
            System.err.println("Unable to change the code of "+this+". Code already set.");
            return;
        }
        Random random = new Random();
        this.code = "";
        for(int i = 0;i < codeLength;i++){
            this.code += validCodeCharacters.charAt(random.nextInt(validCodeCharacters.length()));
        }
        if(Utility.codeIsTaken(this.code)){
            generateCode();
        }
        codeIsSet = true;
    }
    private String hashPassword(String password){
        try {
            MessageDigest hasher = MessageDigest.getInstance(hasherAlgorithm);
            return new String(hasher.digest((this.code+password).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("The hashing algorithm \""+hasherAlgorithm+"\" couldn't be found.");
            System.err.println("Password defaulted to \"\".");
            return "";
        }
    }
    private void setPassword(String password){
        if(null == password){return;}
        this.password = hashPassword(password);
    }
    public boolean checkPassword(String attempt){
        if(null == this.password){return true;}
        return hashPassword(attempt).equals(this.password);
    }
    public Game(GameSettings settings, String password){
        setGameSettings(settings);
        generateCode();
        setPassword(password);
    }
    public void setGameSettings(GameSettings settings){
        this.settings = settings;
    }
    public GameSettings getGameSettings(){
        return this.settings;
    }

    public void setHost(ConnectedClient host){
        this.host = host;
        host.setGame(this);
    }
    public void addUser(ConnectedClient player){
        players[(connectedPlayers++)-1] = player;
    }

    public String getCode() {
        return this.code;
    }

    public void remove() {
        // tell all users to disconnect
    }

    public static final int GAMESTAGE_AWAITINGUSERS = 1;
    public static final int GAMESTAGE_QUESTIONERQUESTIONING = 2;
    public static final int GAMESTAGE_ANSWERERSANSWERING = 3;
    public static final int GAMESTAGE_ANSWERSBEINGREVEALED = 4;
    public static final int GAMESTAGE_FINISHED = 4;
    private int gameStage = GAMESTAGE_AWAITINGUSERS;
    public int getGameStage(){
        return this.gameStage;
    }
    public void setGameStage(int gameStage){
        this.gameStage = gameStage;
    }

    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer;
    public void setCurrentQA(String q, String a1, String a2, String a3, String a4, int correctAnswer){
        if(this.gameStage != GAMESTAGE_QUESTIONERQUESTIONING){
            // tell the questioner that something went wrong
            return;
        }
        this.question = q;
        this.answer1 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
        this.answer4 = a4;
        this.correctAnswer = correctAnswer;
        // Tell all clients that the questioner is done
    }
    private void questionerDone(){
        this.setGameStage(GAMESTAGE_ANSWERERSANSWERING);
    }




    public void messageAllPlayers(String message){
        host.sendMessage(message);
        for(int i = 0;i < connectedPlayers-1;i++){
            players[i].sendMessage(message);
        }
    }
    public void messageHost(String message){
        host.sendMessage(message);
    }
    public void messageNonHosts(String message){
        for(int i = 0;i < connectedPlayers-1;i++){
            players[i].sendMessage(message);
        }
    }
    public void messageQuestioner(String message){
        if(questioner == host){
            host.sendMessage(message);
            return;
        }
        for(int i = 0;i < connectedPlayers-1;i++){
            if(questioner == players[i]){
                players[i].sendMessage(message);
                return;
            }
        }
        // no questioner found
    }
    public void messageNonQuestioner(String message){
        if(questioner != host){
            host.sendMessage(message);
        }
        for(int i = 0;i < connectedPlayers-1;i++){
            if(questioner != players[i]){
                players[i].sendMessage(message);
            }
        }
    }
}
