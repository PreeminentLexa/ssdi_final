import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Game {
    private String code; // I might use the code as the salt too?
    private String password;
    private GameSettings settings;

    private static int maxPlayers = 16;
    public static final int roundEndTime = 20; // Should be equal to Utility.timeOnCorrectAnswerPage+Utility.timeOnScoreboardPage on the client application.

    private ConnectedClient host;
    private ConnectedClient[] players = new ConnectedClient[maxPlayers-1];
    private ConnectedClient questioner;
    private int connectedPlayers = 1;


    private boolean codeIsSet = false;
    private static final String hasherAlgorithm = "SHA-256"; // This is just a random hash, it has no significance.
//    private static final String validCodeCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String validCodeCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // might take out I, 0, and O, to avoid confusion
    private static final int codeLength = 4;


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
        this.codeIsSet = true;
    }
    public boolean getCodeIsGenerated() {
        return this.codeIsSet;
    }
    private String hashPassword(String password){
        if("" == password || null == password){
            return "";
        }
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
        Utility.addGame(this);
    }
    public void setGameSettings(GameSettings settings){
        this.settings = settings;
    }
    public GameSettings getGameSettings(){
        return this.settings;
    }
    public String getSSetting(String key){
        return this.settings.getSSetting(key);
    }
    public String getSSetting(String key, String def){
        return this.settings.getSSetting(key, def);
    }
    public int getISetting(String key){
        return this.settings.getiSetting(key);
    }
    public int getISetting(String key, int def){
        return this.settings.getiSetting(key, def);
    }
    public float getFSetting(String key){
        return this.settings.getfSetting(key);
    }
    public float getFSetting(String key, float def){
        return this.settings.getfSetting(key, def);
    }
    public String getSettingType(String key){
        return this.settings.getSettingType(key);
    }
    public Object getUNKSetting(String key){
        return this.settings.getUNKSetting(key);
    }
    public Object getUNKSetting(String key, Object def){
        return this.settings.getUNKSetting(key, def);
    }
    public String[] getSettingStrings(){
        return this.settings.getSettingStrings();
    }

    public void setHost(ConnectedClient host){
        if(!isGameOpen()){return;}
        this.host = host;
        host.setGame(this);
        host.sendGameCode(this.getCode());
        informPlayerJoin(host);
    }

    public void addUser(ConnectedClient player){
        if(!isGameOpen()){return;}
        players[(connectedPlayers++)-1] = player;
        player.setGame(this);
        player.sendJoinGameSuccess(this);
        informPlayerJoin(player);
    }

    private boolean ended = false;
    private void endTheGame(){
        ended = true;
    }

    public boolean isGameEnded(){
        return ended;
    }

    public boolean isGameOpen(){
        if(isGameEnded()){
            return false;
        }
        return true;
    }

    public void disconnectUser(ConnectedClient player){
        if(player == host){
            if(isGameEnded()){
                host = null;
                connectedPlayers--;
            } else {
                Utility.deleteGame(this);
                return;
            }
        }
        for(int i = 0;i < connectedPlayers;i++){
            if(players[i].getUniqueID().equals(player.getUniqueID())){
                for(int j = i;j < connectedPlayers-1;j++){
                    players[j] = players[j+1];
                }
                players[connectedPlayers--] = null;
            }
        }
        informPlayerLeave(player);
        if(isGameEnded()){
            if(0 == connectedPlayers){
                Utility.deleteGame(this);
            }
        }
    }

    public String getCode() {
        return this.code;
    }

    public void remove() {
        messageAllPlayers(ClientCallbacks.CODE_GAMEDISCONNECT);
    }

    public static final int GAMESTAGE_AWAITINGUSERS = 1;
    public static final int GAMESTAGE_QUESTIONERQUESTIONING = 2;
    public static final int GAMESTAGE_ANSWERERSANSWERING = 3;
    public static final int GAMESTAGE_ANSWERSBEINGREVEALED = 4;
    public static final int GAMESTAGE_FINISHED = 5;
    private int gameStage = GAMESTAGE_AWAITINGUSERS;
    public int getGameStage(){
        return this.gameStage;
    }
    public void setGameStage(int gameStage){
        this.gameStage = gameStage;

        // If the game is done, call endTheGame() to allow the host to leave without booting
    }

    public void requestGameStart(ConnectedClient client){
        if(client != host){return;}
        gameStage_startRoundFirst();
    }

    public void finishQuestioner(ConnectedClient client, String q, String a1, String a2, String a3, String a4, int correct){
        if(client != questioner){return;}
        setQandA(q, a1, a3, a3, a4, correct);
        gameStage_startAnswering();
    }

    public void doAnswer(ConnectedClient client, int answered, long epoc){
        if(gameStage != GAMESTAGE_ANSWERERSANSWERING){return;}
        if(!usableQnA){return;}
        if(client == questioner){return;}
        if(correct == answered){
            long delta = (this.startTime*1000) - epoc;
            System.out.println(client.getID()+" answered correctly in "+delta+"ms / "+(answerTime()*1000)+"ms");
            // give client points
            // tell all players about client's new points
        }
        answerCounts[answered-1]++;
        if((++pplDone) >= (connectedPlayers-1)){
            gameStage_finishAnswering();
        }
        alertQuestionAnswered(answered);
    }

    private int[] questionerMap = null;
    private int currentQuestioner = 0;
    public ConnectedClient getRandomQuestioner(){
        if(null == questionerMap){ // first time
            questionerMap = new int[connectedPlayers];
            int[] opts = new int[connectedPlayers];
            Random randomizer = new Random();
            for(int i = 0;i < connectedPlayers;i++){
                opts[i] = i;
            }
            for(int i = 0;i < connectedPlayers;i++){
                int bound = connectedPlayers-i-1;
                if(0 == bound){
                    questionerMap[i] = opts[bound];
                }else{
                    int picked = opts[randomizer.nextInt(bound)];
                    questionerMap[i] = picked;
                    int[] newOpts = new int[bound];
                    boolean found = false;
                    for(int j = 0;j < bound+1;j++){
                        if(opts[j] == picked) {
                            found = true;
                        } else {
                            newOpts[j - (found?1:0)] = opts[j];
                        }
                    }
                    opts = newOpts;
                }
            }
        }
        if(questionerMap.length != connectedPlayers){ // someone has left
            int[] newQuestionerMap = new int[connectedPlayers];
            int ticker = 0;
            for(int i = 0;i < questionerMap.length;i++){
                if(questionerMap[i] < connectedPlayers){
                    newQuestionerMap[ticker++] = questionerMap[i];
                }
            }
        }
        int index = questionerMap[currentQuestioner];
        ConnectedClient questioner = index==connectedPlayers?host:players[index];
        currentQuestioner = (currentQuestioner+1)%connectedPlayers;
        return questioner;
    }

    private int round = 0;
    private void gameStage_startRoundFirst(){ // 1 to 2
        if(gameStage != GAMESTAGE_AWAITINGUSERS){return;}
        internalGameStage_startRound();
    }
    private void gameStage_startRoundNext(){ // 4 to 2
        if(gameStage != GAMESTAGE_ANSWERSBEINGREVEALED){return;}
        internalGameStage_startRound();
    }
    private void internalGameStage_startRound(){ // Called internally - shouldn't be touched aside from gameStage_startRoundFirst & gameStage_startRoundNext
        gameStage = GAMESTAGE_QUESTIONERQUESTIONING;
        round++;

        questioner = getRandomQuestioner();
        alertRoundStart(questioner);
    }
    private ScheduledFuture timer = null;
    private void gameStage_startAnswering(){gameStage_startAnswering(0);}
    private void gameStage_startAnswering(int depth){
        if(gameStage != GAMESTAGE_QUESTIONERQUESTIONING){
            return;
        }
        gameStage = GAMESTAGE_ANSWERERSANSWERING;
        Game game = this;
        if(!usableQnA){
            if(depth >= 10){
                // We're really having problems. Time to give up and invalidate the round
                internalGameStage_startRound();
                return;
            }
            if(null != timer){
                timer.cancel(true);
                timer = null;
            }
            timer = Executors.newSingleThreadScheduledExecutor().schedule(
                    () -> game.gameStage_startAnswering(depth+1), 1, TimeUnit.SECONDS
            );
            return;
        }
        alertQuestionsReady();
        if(null != timer){
            timer.cancel(true);
            timer = null;
        }
        timer = Executors.newSingleThreadScheduledExecutor().schedule(
                () -> game.gameStage_finishAnswering(), answerTime(), TimeUnit.SECONDS
        );
    }
    public void gameStage_finishAnswering(){
        if(gameStage != GAMESTAGE_ANSWERERSANSWERING){return;}
        gameStage = GAMESTAGE_ANSWERSBEINGREVEALED;
        if(!usableQnA){ // we can't really be here ever
            internalGameStage_startRound();
            return;
        }
        alertDoneRound();
        Game game = this;
        if(null != timer){
            timer.cancel(true);
            timer = null;
        }
        timer = Executors.newSingleThreadScheduledExecutor().schedule(
                () -> game.gameStage_branchRoundEnd(), roundEndTime, TimeUnit.SECONDS
        );
    }
    public void gameStage_branchRoundEnd(){
        if(gameStage != GAMESTAGE_ANSWERSBEINGREVEALED){return;}
        if(null != timer){
            timer.cancel(true);
            timer = null;
        }
        if(isFinalRound()){
            gameStage_endGame();
        } else {
            gameStage_startRoundNext();
        }
    }
    private void gameStage_endGame(){
        if(gameStage != GAMESTAGE_ANSWERSBEINGREVEALED){return;}
        gameStage = GAMESTAGE_FINISHED;
        endTheGame();
    }
    public boolean isFinalRound(){
        int rounds = getISetting("rounds", 5);
        return rounds <= this.round;
    }
    public int answerTime(){
        return getISetting("answerTime", 30);
    }

    private boolean usableQnA = false;
    private int correct = -1;
    private int pplDone = -1;
    private long startTime = -1;
    private int[] answerCounts = null;
    private String[] qna = null;
    public void setQandA(String q, String a1, String a2, String a3, String a4, int correct){
        this.usableQnA = true;
        this.correct = correct;
        this.pplDone = 0;
        this.answerCounts = new int[]{0, 0, 0, 0};
        this.qna = new String[]{q,a1,a2,a3,a4};
    }
    public void voidQandA(){
        this.usableQnA = false;
        this.correct = -1;
        this.pplDone = -1;
        this.startTime = -1;
        this.answerCounts = null;
        this.qna = null;
    }




    public void alertRoundStart(ConnectedClient questioner){
        messageAllPlayers(
                ClientCallbacks.CODE_GAMESTARTED+questioner.getUniqueID()
        );
    }

    public void alertQuestionAnswered(int answer){
        messageQuestioner(
                ClientCallbacks.CODE_ANSWERPICKED+answer
        );
    }

    public void alertQuestionsReady(){
        this.startTime = Instant.now().getEpochSecond()+5;
        if(!usableQnA){return;}
        messageNonQuestioner(
                ClientCallbacks.CODE_PUSH+qna[0]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[1]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[2]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[3]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[4]+"\r\n"+
                ClientCallbacks.CODE_POP+"5"+ClientCallbacks.CODE_QUESTIONERFINISHED+this.startTime
        );
    }
    public void alertDoneRound(){
        if(!usableQnA){return;}
        messageAllPlayers(
                ClientCallbacks.CODE_PUSH+qna[0]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[1]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[2]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[3]+"\r\n"+
                ClientCallbacks.CODE_PUSH+qna[4]+"\r\n"+
                ClientCallbacks.CODE_PUSH+answerCounts[0]+"\r\n"+
                ClientCallbacks.CODE_PUSH+answerCounts[1]+"\r\n"+
                ClientCallbacks.CODE_PUSH+answerCounts[2]+"\r\n"+
                ClientCallbacks.CODE_PUSH+answerCounts[3]+"\r\n"+
                ClientCallbacks.CODE_POP+"9"+ClientCallbacks.CODE_ANSWERSGIVEN+correct
        );
        voidQandA();
    }

    public void informPlayerJoin(ConnectedClient player){
        messageAllPlayersExcept(player,
                ClientCallbacks.CODE_PUSH+player.getUsername()+"\r\n"+
                ClientCallbacks.CODE_PUSH+player.getImageIndex()+"\r\n"+
                ClientCallbacks.CODE_POP+"2"+
                ClientCallbacks.CODE_USERJOINED+player.getUniqueID()
        );
        String message = "";
        if(player != host){
            message +=
                    ClientCallbacks.CODE_PUSH+player.getUsername()+"\r\n"+
                    ClientCallbacks.CODE_PUSH+player.getImageIndex()+"\r\n"+
                    ClientCallbacks.CODE_POP+"2"+
                    ClientCallbacks.CODE_HOSTJOINED+player.getUniqueID()+"\r\n";
        }
        for(int i = 0;i < connectedPlayers-1;i++){
            if(player != players[i]){
                message +=
                        ClientCallbacks.CODE_PUSH+player.getUsername()+"\r\n"+
                        ClientCallbacks.CODE_PUSH+player.getImageIndex()+"\r\n"+
                        ClientCallbacks.CODE_POP+"2"+
                        ClientCallbacks.CODE_USERJOINED+player.getUniqueID()+"\r\n";
            }
        }
        player.sendMessage(message);
    }
    public void informPlayerLeave(ConnectedClient player){
        messageAllPlayers(ClientCallbacks.CODE_USERLEFT+player.getUniqueID());
        player.sendMessage(ClientCallbacks.CODE_GAMEDISCONNECT);
    }

    public void messageAllPlayersExcept(ConnectedClient ignore, String message){
        if(ignore != host){
            host.sendMessage(message);
        }
        for(int i = 0;i < connectedPlayers-1;i++){
            if(ignore != players[i]){
                players[i].sendMessage(message);
            }
        }
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
