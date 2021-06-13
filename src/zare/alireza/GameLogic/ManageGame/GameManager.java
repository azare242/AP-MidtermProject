package zare.alireza.GameLogic.ManageGame;

import zare.alireza.ServerSide.Server;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.util.Collections;
import java.util.HashMap;

public class GameManager {

    private Game game;
    private Server server;
    private ChatHistory chatHistory;
    private HashMap<String,Integer> votes;
    public GameManager(Server server,Game game){
        this.game = game;
        this.server = server;
        chatHistory = new ChatHistory();
        initVotesHashMap();
    }

    private void initVotesHashMap(){
        votes = new HashMap<>();
        for (int i = 0 ; i < 10 ; i++){
            votes.put(game.getUserName(i),0);
        }
        votes.put("NoOne",0);
    }
    private void sendMassageToMayor(){
        PlayerOnServer playerOnServer = game.getPlayerThread("Mayor");
        String massage = "hey mayor wakeup , you must know " + game.getPlayerThread("Physician").getUserName() + " is physician";
        playerOnServer.receiveMassage(massage);
    }
    private void sendMassageToGodFather(){
        PlayerOnServer playerOnServer = game.getPlayerThread("GodFather");
        String massage = "hey god father wakeup , you must know " + game.getPlayerThread("DoctorLector").getUserName() + " is doctor lector and " + game.getPlayerThread("SimpleMafia").getUserName()
                + " is simple mafia";
        playerOnServer.receiveMassage(massage);
    }
    public void introNight(){
        server.sendMassageToPlayers("Intro Night Started");
        sendMassageToGodFather();
        sendMassageToMayor();
        server.sendMassageToPlayers("Intro Night Ended");
    }
    public synchronized void addMassageToHistory(String massage){
        chatHistory.addMassage(massage);
    }
    public String getChatHistory(){
        return chatHistory.get();
    }
    public void discussion(){
        server.sendMassageToPlayers("It's time to discussion");
        game.startDiscussion();
    }
    public void startVoting(){
        server.startVoting();
    }
    private void executePlayer(String userName){
        game.execute(userName);
    }
    public void setVote(String userName){
        Integer count = votes.get(userName);
        count += 1;
        votes.replace(userName,count);
    }
    private Integer maxVote(){
        return Collections.max(votes.values());
    }
    public boolean checkTheVotesAreRegular(){
        int playersWithMaxVote = 0;
        for (int i = 0 ; i < 10 ; i++){
            String userName = game.getUserName(i);
            if (votes.get(userName).equals(maxVote())){
                playersWithMaxVote++;
            }
        }
        return playersWithMaxVote == 1;
    }
    private void clearVotes(){
        for (String name : votes.keySet()){
            votes.replace(name,0);
        }
    }
    private boolean noOneHasMaxVotes(){
        Integer noOneVotes = votes.get("NoOne");
        return noOneVotes.equals(maxVote()) || noOneVotes >= 5;
    }
    public void checkVotes() {
        if (!checkTheVotesAreRegular() || noOneHasMaxVotes()) {
            server.setVote("No One Executed");
            clearVotes();
        } else {
            for (int i = 0; i < 10; ++i) {
                String userName = game.getUserName(i);
                if (votes.get(userName).equals(maxVote())) {
                    execute(userName);
                }
            }
        }

    }
    private void execute(String userName){
        //TODO: ASK FROM MAYOR

        executePlayer(userName);
        server.sendMassageToPlayers(userName + " has been executed , ask his role from iron side :D");
        clearVotes();
    }
}
