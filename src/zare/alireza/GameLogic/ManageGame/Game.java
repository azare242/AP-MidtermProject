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


    public Game(HashMap<String, PlayerOnServer> playerThreadHashMap, ArrayList<String> userNames, ArrayList<PlayerOnServer> threads) {
        this.playerThreadHashMap = playerThreadHashMap;
        this.userNames = userNames;
        this.threads = threads;

        mafias = 3;
        citizens = 7;
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
}
