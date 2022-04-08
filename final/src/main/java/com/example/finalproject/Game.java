package com.example.finalproject;

public class Game {
    private GameSettings gameSettings;
    private int round = 0;
    private String code;
    public Game(GameSettings gameSettings){
        this.gameSettings = gameSettings;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public int getRound(){
        return this.round;
    }
    public void incrementRound(){
        this.round++;
    }
    public boolean isFinalRound(){
        int rounds = getiSetting("rounds");
        return rounds <= this.round;
    }
    public GameSettings getGameSettings(){
        return this.gameSettings;
    }
    public String getSSetting(String key){
        return this.gameSettings.getSSetting(key);
    }
    public int getiSetting(String key){
        return this.gameSettings.getiSetting(key);
    }
    public float getfSetting(String key){
        return this.gameSettings.getfSetting(key);
    }
    public String getSettingType(String key){
        return this.gameSettings.getSettingType(key);
    }
    public Object getUNKSetting(String key){
        return this.gameSettings.getUNKSetting(key);
    }
}
