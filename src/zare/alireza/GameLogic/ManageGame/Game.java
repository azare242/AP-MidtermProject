package zare.alireza.GameLogic.ManageGame;


import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private HashMap<String, PlayerOnServer> playerThreadHashMap;
    private ArrayList<String> userNames;
    private ArrayList<PlayerOnServer> threads;
    private int mafias;
    private int citizens;


    public Game(int capacity,HashMap<String, PlayerOnServer> playerThreadHashMap, ArrayList<String> userNames, ArrayList<PlayerOnServer> threads) {
        this.playerThreadHashMap = playerThreadHashMap;
        this.userNames = userNames;
        this.threads = threads;

        setTeamCounts(capacity);
    }
    private void setTeamCounts(int capacity){
        if (capacity == 10) {
            mafias = 3;
            citizens = 7;
        }
        else if (capacity == 9){
            mafias = 3;
            citizens = 6;
        }
        else if (capacity == 8){
            mafias = 2;
            citizens = 6;
        }
        else if (capacity == 7){
            mafias = 2;
            citizens = 5;
        }
        else if (capacity == 6){
            mafias = 2;
            citizens = 4;
        }
        else if (capacity == 5){
            mafias = 1;
            citizens = 4;
        }
    }
    public PlayerOnServer getPlayerThread(String roleName) {
        return playerThreadHashMap.get(roleName);
    }

    public boolean mafiaWins(){
        return mafias == citizens && mafias != 0;
    }

    public boolean citizensWins(){
        return mafias == 0 && citizens != 0;
    }

    public void startDiscussion(){
        for (PlayerOnServer player : threads){
            if (player.isOnGame() && player.alive())
            player.chat();
        }
    }

    public void kill(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                player.kill();
                if (player.isMafia() && !player.alive()){
                    mafias--;
                }
                else if (!player.isMafia() && !player.alive())
                    citizens--;
            }
        }
    }
    public void save(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                player.saved();
            }
        }
    }
    public boolean checkProKill(String userName){

        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                return player.isMafia();
            }
        }
        return false;
    }
    public void silent(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                player.silent();
            }
        }
    }
    public String getUserName(int index){
        return userNames.get(index);
    }
    public void execute(String userName){
        for (PlayerOnServer player : threads){
            if (player.getUserName().equals(userName)){
                player.execute();
                if (player.isMafia()){
                    mafias--;
                }
                else citizens--;
            }
        }
    }
    private String status(String winnerTeam,boolean team){
        if (winnerTeam.equalsIgnoreCase("Mafias") && team) return "WINNER";
        else if (winnerTeam.equalsIgnoreCase("Citizens") && !team) return "WINNER";
        else return "LOSER";
    }
    public String scoreBoard(){
        String sb = "THE SCORE BOARD: \n";
        String winnerTeam = mafiaWins() ? "Mafias" : "Citizens";
        sb += "WINNER TEAM IS : " + winnerTeam + '\n';
        for (PlayerOnServer player : threads){
            sb += player.getUserName() + " -> " + status(winnerTeam, player.isMafia()) + '\n';
        }
        return sb;
    }
}
