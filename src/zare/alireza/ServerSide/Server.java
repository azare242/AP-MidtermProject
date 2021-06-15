package zare.alireza.ServerSide;

import zare.alireza.GameLogic.ManageGame.GameManager;
import zare.alireza.GameLogic.ManageGame.Game;
import zare.alireza.Roles.*;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The type Server.
 */
public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Role> rolesForGiveToPlayers;
    private HashMap<String, PlayerOnServer> rolesForGame;
    private ArrayList<String> userNames;
    private ArrayList<PlayerOnServer> threads;
    private int playersReady = 0;
    private int votedPlayers = 0;
    private int alivePlayers;
    private int capacity;
    private GameManager gameManager;
    private String votes = "";

    /**
     * Instantiates a new Server.
     *
     * @param port     the port
     * @param capacity the capacity
     */
    public Server(int port,int capacity){
        this.capacity = capacity;
        this.alivePlayers = capacity;
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

    /**
     * Get capacity int.
     *
     * @return the int
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * Add user name.
     *
     * @param userName the user name
     */
    public synchronized void addUserName(String userName){
        userNames.add(userName);
    }

    /**
     * User name is valid boolean.
     *
     * @param userName the user name
     * @return the boolean
     */
    public synchronized boolean userNameIsValid(String userName){
        for (String un : userNames){
            if (un.equalsIgnoreCase(userName)) return false;
        }
        return true;
    }

    /**
     * Give role to player role.
     *
     * @return the role
     */
    public synchronized Role giveRoleToPlayer(){
        Role role = rolesForGiveToPlayers.get(0);
        rolesForGiveToPlayers.remove(0);
        return role;
    }

    /**
     * Increase ready players.
     */
    public synchronized void increaseReadyPlayers(){
        playersReady++;
        System.out.println("Players Ready : " + playersReady);
    }

    /**
     * Map role to player thread.
     *
     * @param role the role
     * @param pt   the pt
     */
    public synchronized void mapRoleToPlayerThread(Role role, PlayerOnServer pt){
        rolesForGame.put(role.getClass().getSimpleName(),pt);
    }

    /**
     * All players ready boolean.
     *
     * @return the boolean
     */
    public synchronized boolean allPlayersReady(){
        return playersReady == capacity;
    }

    /**
     * Start game.
     */
    public synchronized void startGame(){
        gameManager = new GameManager(this,new Game(capacity,rolesForGame,userNames,threads));
        gameManager.startGame();
    }

    /**
     * Add massage to history.
     *
     * @param massage the massage
     */
    public synchronized void addMassageToHistory(String massage){
        gameManager.addMassageToHistory(massage);
    }

    /**
     * Server start.
     */
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

    /**
     * Get chat history string.
     *
     * @return the string
     */
    public String getChatHistory(){
        return gameManager.getChatHistory();
    }

    /**
     * A player sends massage to other players.
     *
     * @param massage the massage
     * @param sender  the sender
     */
    public void aPlayerSendsMassageToOtherPlayers(String massage, PlayerOnServer sender){
        for (PlayerOnServer player : threads){
            if (player != sender && player.isOnGame()){
                player.receiveMassage(massage);
            }
        }
    }

    /**
     * Send massage to players.
     *
     * @param massage the massage
     */
    public void sendMassageToPlayers(String massage){
        for (PlayerOnServer ps : threads){
            if (ps.isOnGame())
                ps.receiveMassage(massage);
        }
    }

    /**
     * Get alive players list to a player.
     *
     * @param p the p
     */
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

    /**
     * Set vote.
     *
     * @param userName the user name
     * @param voter    the voter
     */
    public void setVote(String userName,String voter){
        gameManager.setVote(userName);
        votes += voter + " -> " + userName + '\n';
        votedPlayers++;
    }
    private String status(boolean isAlive){
        if (isAlive) return "(ALIVE)";
        else return "(DEAD)";
    }

    /**
     * Send day list.
     */
    public void sendDayList(){
        String massage = "Players : \n";
        for (PlayerOnServer p : threads){
            massage += p.getUserName() + " :: " + status(p.alive()) + '\n';
        }
        sendMassageToPlayers(massage);
    }

    /**
     * All players voted boolean.
     *
     * @return the boolean
     */
    public boolean allPlayersVoted(){
        return votedPlayers == alivePlayers;
    }

    /**
     * A player killed.
     */
    public synchronized void aPlayerKilled(){
        alivePlayers--;
    }
    /**
     * Check votes.
     */
    public void checkVotes(){
        gameManager.checkVotes(votes);
        votedPlayers = 0;
        votes = "";
    }

    /**
     * Get player user name string.
     *
     * @param index the index
     * @return the string
     */
    public String getPlayerUserName(int index){
        return threads.get(index - 1).getUserName();
    }

    /**
     * Start voting.
     */
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

    /**
     * Get out roles string.
     *
     * @return the string
     */
    public String getOutRoles(){
        ArrayList<String> list = new ArrayList<>();
        for (PlayerOnServer player : threads){
            if (!player.alive()){
                list.add(player.getRoleName());
            }
        }
        return shuffledStringOfOutRoles(list);
    }

    /**
     * Is player alive boolean.
     *
     * @param userName the user name
     * @return the boolean
     */
    public boolean isPlayerAlive(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                return player.alive();
            }
        }
        return false;
    }

    /**
     * Investigation string.
     *
     * @param userName the user name
     * @return the string
     */
    public String investigation(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                if (player.investigation()) return "YES";
                else return "NO";
            }
        }
        return "NO";
    }

    /**
     * Send list to doctor lector.
     *
     * @param doctorLector the doctor lector
     */
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

    /**
     * Player dead menu.
     *
     * @param userName the user name
     */
    public void playerDeadMenu(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                player.deadMenu();
            }
        }
    }

    /**
     * End game.
     */
    public void endGame(){
        for (PlayerOnServer p : threads){
            if (p.isOnGame()){
                p.endGame();
            }
            threads.clear();
            rolesForGame.clear();
            userNames.clear();
            threads = null;
            rolesForGame = null;
            userNames = null;
            System.exit(0);
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
