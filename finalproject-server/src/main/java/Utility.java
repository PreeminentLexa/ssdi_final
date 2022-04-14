public class Utility {
    private static Game[] games = new Game[2];
    private static int gameCount = 0;
    private static void checkGameListLength(){
        if(gameCount < games.length){return;}
        Game[] newGames = new Game[games.length*2];
        for(int i = 0;i < games.length;i++){
            newGames[i] = games[i];
        }
        games = newGames;
    }
    public static void addGame(Game newGame){
        checkGameListLength();
        games[gameCount++] = newGame;

        System.out.println("New game created, Code: "+newGame.getCode());
        for(String setting : newGame.getSettingStrings()){
            System.out.println("\t"+setting);
        }
    }
    public static Game getGame(String code){
        for(int i = 0;i < gameCount;i++){
            if(code.equals(games[i].getCode())){
                return games[i];
            }
        }
        return null;
    }
    public static void deleteGame(String code){
        for(int i = 0;i < gameCount;i++){
            if(code.equals(games[i].getCode())){
                Game toRemove = games[i];
                for(int j = i;j < gameCount-1;j++){
                    games[j] = games[j+1];
                }
                toRemove.remove();
                gameCount--;
                System.out.println("Game closed, Code: "+code);
                return;
            }
        }
        System.out.println("Failed to close "+code);
    }
    public static void deleteGame(Game game){
        deleteGame(game.getCode());
    }
    public static boolean codeIsTaken(String code){
        for(int i = 0;i < gameCount;i++){
            if(games[i].getCodeIsGenerated()){
                if(code.equals(games[i].getCode())){
                    return true;
                }
            }
        }
        return false;
    }



















    private static ConnectedClient[] clients = new ConnectedClient[2];
    private static int clientCount = 0;
    private static void checkClientListLength(){
        if(clientCount < clients.length){return;}
        ConnectedClient[] newClients = new ConnectedClient[clients.length*2];
        for(int i = 0;i < clients.length;i++){
            newClients[i] = clients[i];
        }
        clients = newClients;
    }
    public static void addClient(ConnectedClient client){
        checkClientListLength();
        clients[clientCount++] = client;
    }
    public static ConnectedClient getClient(String uid){
        for(int i = 0;i < clientCount;i++){
            if(uid.equals(clients[i].getUniqueID())){
                return clients[i];
            }
        }
        return null;
    }
    public static void deleteClient(String uid){
        for(int i = 0;i < clientCount;i++){
            if(uid.equals(clients[i].getUniqueID())){
                ConnectedClient toRemove = clients[i];
                for(int j = i;j < clientCount-1;j++){
                    clients[j] = clients[j+1];
                }
                toRemove.remove();
                clientCount--;
                return;
            }
        }
    }
    public static void deleteClient(ConnectedClient client){
        deleteClient(client.getUniqueID());
    }

    public static boolean uniqueUserIDIsTaken(String uid){
        for(int i = 0;i < clientCount;i++){
            if(clients[i].getUIDIsGenerated()){
                if(uid.equals(clients[i].getUniqueID())){
                    return true;
                }
            }
        }
        return false;
    }
}
