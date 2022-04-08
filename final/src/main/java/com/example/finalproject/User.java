package com.example.finalproject;

public class User {
    private String uid;
    private String username;
    private int imageIndex;
    private int score;
    private boolean localUser;
    public User(String uid, String username, int imageIndex){
        this.uid = uid;
        if(uid.equals("_")){
            this.localUser = true;
        }
        this.username = username;
        this.imageIndex = imageIndex;
        User.addUser(this);
    }

    public String getUID() {
        return uid;
    }
    public String getUsername() {
        return username;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public int getScore() {
        return score;
    }
    public boolean isLocalUser() {
        return this.localUser;
    }
    public void setUID(String uid){
        User.removeUser(this.uid);
        this.uid = uid;
        User.addUser(this);
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setImageIndex(int imageIndex){
        this.imageIndex = imageIndex;
    }
    public void setScore(int score){
        this.score = score;
    }

    public void delete() {
        User.removeUser(this.uid);
    }

    private static User[] allUsers = new User[1];
    private static int userCount = 0;
    private static void checkArraySize(){
        if(userCount < allUsers.length){return;}
        User[] newAllUsers = new User[allUsers.length*2];
        for(int i = 0;i < userCount;i++){
            newAllUsers[i] = allUsers[i];
        }
        allUsers = newAllUsers;
    }
    private static void addUser(User user){
        checkArraySize();
        allUsers[userCount++] = user;
    }
    public static User getUser(String uid){
        for(int i = 0;i < userCount;i++){
            if(uid.equals(allUsers[i].getUID())){
                return allUsers[i];
            }
        }
        return null;
    }
    public static User getLocalUser(){
        for(int i = 0;i < userCount;i++){
            if(allUsers[i].isLocalUser()){
                return allUsers[i];
            }
        }
        return new User("_", "", -1);
    }
    public static User[] getAllUsers(){
        User[] returnUsers = new User[userCount];
        for(int i = 0;i < userCount;i++){
            returnUsers[i] = allUsers[i];
        }
        return returnUsers;
    }
    public static User removeUser(String uid){
        for(int i = 0;i < userCount;i++){
            if(uid.equals(allUsers[i].getUID())){
                User toReturn = allUsers[i];
                for(int j = i;j < userCount-1;j++){
                    allUsers[j] = allUsers[j+1];
                }
//                allUsers[] = null;
                userCount--;
                return toReturn;
            }
        }
        return null;
    }
}
