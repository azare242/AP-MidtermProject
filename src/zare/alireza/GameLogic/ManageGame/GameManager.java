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
        for (int i = 0 ; i < server.getCapacity() ; i++){
            votes.put(game.getUserName(i),0);
        }
        votes.put("NoOne",0);
    }
    private void sendMassageToMayor(){
        PlayerOnServer playerOnServer = game.getPlayerThread("Mayor");
        String massage = "hey mayor wakeup , you must know " + game.getPlayerThread("Physician").getUserName() + " is physician";
        playerOnServer.receiveMassage(massage);
    }
    private void sendMassageMafias(){
        PlayerOnServer sm = game.getPlayerThread("SimpleMafia");
        PlayerOnServer dl = game.getPlayerThread("DoctorLector");
        PlayerOnServer gf = game.getPlayerThread("GodFather");
        String massage = "hey god father wakeup , you must know " + game.getPlayerThread("DoctorLector").getUserName() + " is doctor lector and " + game.getPlayerThread("SimpleMafia").getUserName()
                + " is simple mafia";
        gf.receiveMassage(massage);
        sm.receiveMassage("Your TeamMates are : " + gf.getUserName() + " and " + dl.getUserName());
        dl.receiveMassage("Your TeamMates are : " + gf.getUserName() + " and " + sm.getUserName());
    }
    public void introNight(){
        server.sendMassageToPlayers("Intro Night Started");
        sendMassageMafias();
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
        if (!simpleMafia.alive()) {
            return "NoBody";
        }
        String opinion = simpleMafia.opinion();
        return simpleMafia.getUserName() + " says " + opinion;
    }
    private String getDoctorLectorOpinion(){
        PlayerOnServer doctorLector = game.getPlayerThread("DoctorLector");
        if (!doctorLector.alive()) {
            return "NoBody";
        }
        String opinion = doctorLector.opinion();
        return doctorLector.getUserName() + " says " + opinion;
    }
    private String godFatherAction(){
        PlayerOnServer godFather = game.getPlayerThread("GodFather");
        if (!godFather.alive()) {
            return "NoBody";
        }
        String action = godFather.action();
        return action;
    }
    private String doctorLectorAction(){
        server.sendMassageToPlayers(NightMassages.doctorLectorWakeUp);
        PlayerOnServer doctorLector = game.getPlayerThread("DoctorLector");
        if (!doctorLector.alive()){
            server.sendMassageToPlayers(NightMassages.doctorLectorSleep);
            return "NoBody";
        }
        String action = doctorLector.action();
        server.sendMassageToPlayers(NightMassages.doctorLectorSleep);
        return action;
    }
    private String physicianAction(){
        server.sendMassageToPlayers(NightMassages.physicianWakeUp);
        PlayerOnServer physician = game.getPlayerThread("Physician");
        if (!physician.alive()) {
            server.sendMassageToPlayers(NightMassages.physicianSleep);
            return "NoBody";
        }
        String action = physician.action();
        server.sendMassageToPlayers(NightMassages.physicianSleep);
        return action;
    }
    private void detectorAction(){
        server.sendMassageToPlayers(NightMassages.detectorWakeUp);
        PlayerOnServer detector = game.getPlayerThread("Detector");
        if (!detector.alive()){
            server.sendMassageToPlayers(NightMassages.detectorSleep);
            return;
        }
        String action = detector.action();
        investigation(action,detector);
        server.sendMassageToPlayers(NightMassages.detectorSleep);
    }
    private void investigation(String userName, PlayerOnServer detector){
        if (userName.equalsIgnoreCase("NoBody")) return;
        String massage = server.investigation(userName);
        detector.receiveMassage(massage);
    }

    private boolean proKill(String userName){
        return game.checkProKill(userName);
    }
    private String professionalAction(){
        server.sendMassageToPlayers(NightMassages.proWakeUp);
        PlayerOnServer professional = game.getPlayerThread("Professional");
        if (!professional.alive()) {
            server.sendMassageToPlayers(NightMassages.proSleep);
            return "NoBody";
        }
        String action = professional.action();
        server.sendMassageToPlayers(NightMassages.proSleep);
        if (proKill(action)){
            action = action;
        }
        else action = professional.getUserName();

        return action;
    }
    private String psychologistAction() {
        server.sendMassageToPlayers(NightMassages.psychologistWakeUp);
        PlayerOnServer psychologist = game.getPlayerThread("Psychologist");
        if (!psychologist.alive()) {
            server.sendMassageToPlayers(NightMassages.psychologistSleep);
            return "NoBody";
        }
        String action = psychologist.action();
        server.sendMassageToPlayers(NightMassages.psychologistSleep);
        return action;
    }
    private String ironSideAction(){
        server.sendMassageToPlayers(NightMassages.ironSideWakeUp);
        PlayerOnServer ironSide = game.getPlayerThread("IronSide");
        if (!ironSide.alive()){
            server.sendMassageToPlayers(NightMassages.ironSideSleep);
            return "No";
        }
        String action = ironSide.action();
        server.sendMassageToPlayers(NightMassages.ironSideSleep);
        return action;
    }
    private void ironSideActionHandle(String action){
        if (action.equalsIgnoreCase("YES")) {
            String outRoles = server.getOutRoles();
            server.sendMassageToPlayers("This Roles are Dead:\n" + outRoles);
        }
        else return;
    }
    private String mafiaTarget(){

        server.sendMassageToPlayers(NightMassages.mafiaWakeUp);
        server.sendMassageToPlayers(NightMassages.godFatherTarget);
        PlayerOnServer sm = game.getPlayerThread("SimpleMafia");
        PlayerOnServer dl = game.getPlayerThread("DoctorLector");
        PlayerOnServer gf = game.getPlayerThread("GodFather");

        String simpleMafiaOpinion = "",doctorLectorOpinion = "";
        if (sm.alive()){
            simpleMafiaOpinion = getSimpleMafiaOpinion();
        }

        if (!gf.alive()){
            if (dl.alive()){
                if (sm.alive()) {
                    dl.receiveMassage(sm.getUserName() + " says kill " + simpleMafiaOpinion);
                }

                return getDoctorLectorOpinion();
            }
            if (!dl.alive()){
                return simpleMafiaOpinion;
            }
        } else {
            if (sm.alive()){
                simpleMafiaOpinion = getSimpleMafiaOpinion();
            }

            if (dl.alive()){
                doctorLectorOpinion = getDoctorLectorOpinion();
            }
            sendMassagesToMafias(simpleMafiaOpinion,doctorLectorOpinion);
            return godFatherAction();
        }
        return "NoBody";
    }
    private void sendMassagesToMafias(String... massages){
        PlayerOnServer sm = game.getPlayerThread("SimpleMafia");
        PlayerOnServer dl = game.getPlayerThread("DoctorLector");
        PlayerOnServer gf = game.getPlayerThread("GodFather");
        if (sm.alive()){
            sm.receiveMassage(massages[1]);
        }
        if (dl.alive()){
            dl.receiveMassage(massages[0]);
        }
        if (gf.alive()){
            dl.receiveMassage(massages[0] + massages[1]);
        }
    }
    public void night(){
        String mafiaAction = mafiaTarget();
        server.sendMassageToPlayers(NightMassages.mafiaSleep);
        String doctorLectorAction = doctorLectorAction();
        String physicianAction = physicianAction();
        detectorAction();
        String psychoAction = psychologistAction();
        String professionalAction = professionalAction();
        String ironSideAction = ironSideAction();
        server.sendMassageToPlayers("EVERY ONE WAKE UP!");
        handleNightEvents(mafiaAction,doctorLectorAction,physicianAction,professionalAction);
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
            server.playerDeadMenu(actions[0]);
        }

        if (!server.isPlayerAlive(actions[3])){
            events += "We Lost " + actions[3];
            server.playerDeadMenu(actions[0]);
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
        server.playerDeadMenu(userName);
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
        for (int i = 0 ; i < server.getCapacity() ; i++){
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
        if (userName.equalsIgnoreCase("NoBody")) return;
        game.silent(userName);
    }
    public void checkVotes() {
        if (!checkTheVotesAreRegular() || noOneHasMaxVotes()) {
            server.setVote("No One Executed");
            clearVotes();
        } else {
            for (int i = 0; i < server.getCapacity(); ++i) {
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
