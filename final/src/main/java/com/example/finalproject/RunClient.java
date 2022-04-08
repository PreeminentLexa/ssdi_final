package com.example.finalproject;


public class RunClient {
    public static void main(String[] args) {
        Connection server = new Connection("localhost:8081");
        server.setUsername("Lexa");
        server.createGame("pass", 5, 1);
        server.joinGame("code", "pass");
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {}
    }
}
