public class Utility {
    private static Game[] games = new Game[2];
    private static String[] gameCodes = new String[2];
    private static int gameCount = 0;
    private static void checkGameListLength(){
        if(gameCount < games.length){return;}
        Game[] newGames = new Game[games.length*2];
        String[] newGameCodes = new String[games.length*2];
        for(int i = 0;i < games.length;i++){
            newGames[i] = games[i];
            newGameCodes[i] = gameCodes[i];
        }
        games = newGames;
        gameCodes = newGameCodes;
    }
    public static void addGame(Game newGame){
        checkGameListLength();
        games[gameCount] = newGame;
        gameCodes[gameCount++] = newGame.getCode();
    }
    public static Game getGame(String code){
        for(int i = 0;i < gameCount;i++){
            if(code.equals(gameCodes[i])){
                return games[i];
            }
        }
        return null;
    }
    public static void deleteGame(String code){
        for(int i = 0;i < gameCount;i++){
            if(code.equals(gameCodes[i])){
                Game toRemove = games[i];
                for(int j = i;j < gameCount-1;j++){
                    gameCodes[j] = gameCodes[j+1];
                    games[j] = games[j+1];
                }
                toRemove.remove();
                gameCount--;
                return;
            }
        }
//        deleteGame(getGame(code));
    }
    public static void deleteGame(Game game){
        deleteGame(game.getCode());
    }
    public static boolean codeIsTaken(String code){
        for(int i = 0;i < gameCount;i++){
            if(code.equals(gameCodes[i])){
                return true;
            }
        }
        return false;
    }
}
