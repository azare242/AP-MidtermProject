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

    private String getSimpleMafiaOpinion(){
        PlayerOnServer simpleMafia = game.getPlayerThread("SimpleMafia");
        String opinion = simpleMafia.opinion();
        return simpleMafia.getUserName() + " says " + opinion;
    }
    private String getDoctorLectorOpinion(){
        PlayerOnServer doctorLector = game.getPlayerThread("DoctorLector");
        String opinion = doctorLector.opinion();
        return doctorLector.getUserName() + " says " + opinion;
    }
    private String godFatherAction(){
        PlayerOnServer godFather = game.getPlayerThread("GodFather");
        String action = godFather.action();
        return action;
    }
    private String doctorLectorAction(){
        PlayerOnServer doctorLector = game.getPlayerThread("DoctorLector");
        String action = doctorLector.action();
        return action;
    }
    private String physicianAction(){
        PlayerOnServer physician = game.getPlayerThread("Physician");
        String action = physician.action();
        return action;
    }
    private void detectorAction(){
        PlayerOnServer detector = game.getPlayerThread("Detector");
        String action = detector.action();
        investigation(action,detector);
    }
    private void investigation(String userName, PlayerOnServer detector){
        String massage = server.investigation(userName);
        detector.receiveMassage(massage);
    }

    private boolean proKill(String userName){
        return game.checkProKill(userName);
    }
    private String professionalAction(){
        PlayerOnServer professional = game.getPlayerThread("Professional");
        String action = professional.action();
        if (proKill(action)){
            action = action;
        }
        else action = professional.getUserName();

        return action;
    }
    private String psychologistAction() {
        PlayerOnServer psychologist = game.getPlayerThread("Psychologist");
        String action = psychologist.action();
        return action;
    }
    private String ironSideAction(){
        PlayerOnServer ironSide = game.getPlayerThread("IronSide");
        String action = ironSide.action();
        return action;
    }
    private void sendOpinionsToGodFather(String... opinions){
        String massage = "";
        for (String opinion : opinions){
            massage += opinion + '\n';
        }
        game.getPlayerThread("GodFather").receiveMassage(massage);
    }
    private void ironSideActionHandle(String action){
        if (action.equalsIgnoreCase("YES")) {
            String outRoles = server.getOutRoles();
            server.sendMassageToPlayers("This Roles are Dead:\n" + outRoles);
        }
        else return;
    }
    public void night(){
        String simpleMafiaOpinion = getSimpleMafiaOpinion();
        String doctorLectorOpinion = getDoctorLectorOpinion();
        sendOpinionsToGodFather(simpleMafiaOpinion,doctorLectorOpinion);
        String godFatherAction = godFatherAction();
        String doctorLectorAction = doctorLectorAction();
        String physicianAction = physicianAction();
        detectorAction();
        String psychoAction = psychologistAction();
        String professionalAction = professionalAction();
        String ironSideAction = ironSideAction();
        server.sendMassageToPlayers("EVERY ONE WAKE UP!");
        handleNightEvents(godFatherAction,doctorLectorAction,physicianAction,professionalAction);
        makeSilent(psychoAction);
        ironSideActionHandle(ironSideAction);
    }
    private void killsOrSavesHandle(String[] actions){
        kill(actions[0]);
        kill(actions[3]);
        save(actions[1]);
        save(actions[2]);
        if (actions[1].equals(actions[3])){
            save(actions[3]);
        }
    }
    private void handleNightEvents(String... actions){
        String events = "";
        killsOrSavesHandle(actions);

        if (!server.isPlayerAlive(actions[0])){
            events += "We Lost " + actions[0];
        }

        if (!server.isPlayerAlive(actions[3])){
            events += "We Lost " + actions[3];
        }

        if (events.length() == 0){
            events += "Last Night is a good night";
        }
        server.sendMassageToPlayers(events);
    }
    private void kill(String userName){
        game.kill(userName);
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
    private void makeSilent(String userName){
        game.silent(userName);
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
    private void save(String userName) {
        game.save(userName);
    }

    private void execute(String userName) {
        PlayerOnServer mayor = game.getPlayerThread("Mayor");
        String MAYORAction = mayor.action();
        if (MAYORAction.equals("1")) {
            server.sendMassageToPlayers("No One Executed");
            clearVotes();
        } else {

            executePlayer(userName);
            server.sendMassageToPlayers(userName + " has been executed , ask his role from iron side :D");
            clearVotes();
        }
    }
}
