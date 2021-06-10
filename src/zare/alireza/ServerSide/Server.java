package zare.alireza.ServerSide;

import zare.alireza.GameLogic.ManageGame.GameManager;
import zare.alireza.GameLogic.ManageGame.Game;
import zare.alireza.Roles.*;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Role> rolesForGiveToPlayers;
    private HashMap<String, PlayerOnServer> rolesForGame;
    private ArrayList<String> userNames;
    private ArrayList<PlayerOnServer> threads;
    private int playersReady = 0;
    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
        }
        rolesForGiveToPlayers = RolesList.get();
        rolesForGame = new HashMap<>();
        userNames = new ArrayList<>();
        threads = new ArrayList<>();
    }
    public synchronized void addUserName(String userName){
        userNames.add(userName);
    }

    public synchronized boolean userNameIsValid(String userName){
        for (String un : userNames){
            if (un.equalsIgnoreCase(userName)) return false;
        }
        return true;
    }
    public synchronized Role giveRoleToPlayer(){
        Role role = rolesForGiveToPlayers.get(0);
        rolesForGiveToPlayers.remove(0);
        return role;
    }
    public synchronized void increaseReadyPlayers(){
        playersReady++;
        System.out.println("Players Ready : " + playersReady);
    }
    public synchronized void mapRoleToPlayerThread(Role role, PlayerOnServer pt){
        rolesForGame.put(role.getClass().getSimpleName(),pt);
    }
    public synchronized boolean allPlayersReady(){
        return playersReady == 10;
    }
    public synchronized void startGame(){
        GameManager gameManager = new GameManager(this,new Game(rolesForGame,userNames,threads));
        gameManager.introNight();
    }

    public void serverStart(){

        int clientsConnectedCounter = 0;
        while (true){

            try {
                Socket socket = serverSocket.accept();
                if (clientsConnectedCounter == 10){
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("game_is_full");
                }

                else {
                    clientsConnectedCounter++;
                    System.out.println("New Player Joins");
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    PlayerOnServer playerOnServer = new PlayerOnServer(this,dataOutputStream,dataInputStream);
                    threads.add(playerOnServer);
                    playerOnServer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    private void startClientThreads(){
        for (PlayerOnServer ps : threads) {
            ps.start();
        }
    }
    public void sendMassageToPlayers(String massage){
        for (PlayerOnServer ps : threads){
            ps.receiveMassage(massage);
        }
    }
}
