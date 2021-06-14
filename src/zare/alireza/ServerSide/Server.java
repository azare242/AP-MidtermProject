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
    private int votedPlayers = 0;
    private int capacity;
    private GameManager gameManager;
    public Server(int port,int capacity){
        this.capacity = capacity;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
        }
        rolesForGiveToPlayers = RolesList.get(capacity);
        rolesForGame = new HashMap<>();
        userNames = new ArrayList<>();
        threads = new ArrayList<>();
    }
    public int getCapacity(){
        return capacity;
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
        return playersReady == capacity;
    }
    public synchronized void startGame(){
        gameManager = new GameManager(this,new Game(capacity,rolesForGame,userNames,threads));
        gameManager.startGame();
    }
    public synchronized void addMassageToHistory(String massage){
        gameManager.addMassageToHistory(massage);
    }

    public void serverStart(){

        int clientsConnectedCounter = 0;
        while (true){

            try {
                Socket socket = serverSocket.accept();
                if (clientsConnectedCounter == capacity){
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("game_is_full");
                }

                else {
                    clientsConnectedCounter++;
                    System.out.println("New Player Joins");
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    PlayerOnServer playerOnServer = new PlayerOnServer(this,dataOutputStream,dataInputStream,socket);
                    threads.add(playerOnServer);
                    playerOnServer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public String getChatHistory(){
        return gameManager.getChatHistory();
    }
    public void aPlayerSendsMassageToOtherPlayers(String massage, PlayerOnServer sender){
        for (PlayerOnServer player : threads){
            if (player != sender && player.isOnGame()){
                player.receiveMassage(massage);
            }
        }
    }
    public void checkPlayersOnGame(){
        ArrayList<PlayerOnServer> checked = new ArrayList<>();
        for (PlayerOnServer player : threads){
            if (!player.isOnGame()){
                checked.add(player);
            }
        }
        for (PlayerOnServer player : checked){
            threads.remove(player);
        }
    }
    public void sendMassageToPlayers(String massage){
        for (PlayerOnServer ps : threads){
            ps.receiveMassage(massage);
        }
    }
    public void getAlivePlayersListToAPlayer(PlayerOnServer p){
        String list = "";
        int i = 1;
        for (PlayerOnServer player : threads){
            if (player != p && player.alive()){
                list += "(" + i + ")" + " - " + player.getUserName() + '\n';
            }
            i++;
        }
        p.receiveMassage(list);
    }
    public void setVote(String userName){
        gameManager.setVote(userName);
        votedPlayers++;
    }
    public boolean allPlayersVoted(){
        return votedPlayers == capacity;
    }
    public void checkVotes(){
        gameManager.checkVotes();
        votedPlayers = 0;
    }
    public String getPlayerUserName(int index){
        return threads.get(index - 1).getUserName();
    }
    public void startVoting(){
        for (PlayerOnServer player : threads){
            if (player.alive() && player.isOnGame())
            player.voting();
        }
    }
    private String shuffledStringOfOutRoles(ArrayList<String> list){
        Collections.shuffle(list);
        String shuffledList = "";
        for (String s : list){
            shuffledList += "\t >>> " + s;
        }
        return shuffledList;
    }
    public String getOutRoles(){
        ArrayList<String> list = new ArrayList<>();
        for (PlayerOnServer player : threads){
            if (!player.alive()){
                list.add(player.getRoleName());
            }
        }
        return shuffledStringOfOutRoles(list);
    }
    public boolean isPlayerAlive(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                return player.alive();
            }
        }
        return false;
    }
    public String investigation(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                if (player.investigation()) return "YES";
                else return "NO";
            }
        }
        return "NO";
    }
    public void sendListToDoctorLector(PlayerOnServer doctorLector){
        String list = "";
        int i = 1;
        for (PlayerOnServer player : threads){
            if (player.isMafia() && player != doctorLector){
                list += "(" + i + ")" + " - " + player.getUserName() + '\n';
            }
            i++;
        }
        doctorLector.receiveMassage(list);
    }
    public void playerDeadMenu(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                player.deadMenu();
            }
        }
    }

}
