package zare.alireza.GameLogic.ManageGame;

import zare.alireza.GameLogic.phases.*;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private HashMap<String, PlayerOnServer> playerThreadHashMap;
    private ArrayList<String> userNames;
    private ArrayList<PlayerOnServer> threads;
    private int mafias;
    private int citizens;
    private Phase[] phases;

    public Game(HashMap<String, PlayerOnServer> playerThreadHashMap, ArrayList<String> userNames, ArrayList<PlayerOnServer> threads) {
        this.playerThreadHashMap = playerThreadHashMap;
        this.userNames = userNames;
        this.threads = threads;
        phases = new Phase[]{
                new IntroNight(),new NormalNight(),new Discussion(),new ElectionAndExecution()
        };
        mafias = 3;
        citizens = 7;
    }

    public PlayerOnServer getPlayerThread(String roleName) {
        return playerThreadHashMap.get(roleName);
    }

    public Phase introNight(){
        return phases[0];
    }
    public Phase NormalNight(){
        return phases[1];
    }
    public Phase discussion(){
        return phases[2];
    }
    public Phase electionAndExecution(){
        return phases[3];
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
}
