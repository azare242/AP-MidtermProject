package zare.alireza.GameLogic.ManageGame;

import zare.alireza.ServerSide.Server;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.util.Collections;
import java.util.HashMap;

/**
 * The type Game manager.
 */
public class GameManager {

    private Game game;
    private Server server;
    private ChatHistory chatHistory;
    private HashMap<String,Integer> votes;
    private boolean gameStarted = false;

    /**
     * Instantiates a new Game manager.
     *
     * @param server the server
     * @param game   the game
     */
    public GameManager(Server server,Game game){
        this.game = game;
        this.server = server;
        chatHistory = new ChatHistory();
        initVotesHashMap();
    }

    private void sleep(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        if (sm == null && dl == null){
            gf.receiveMassage("Wake Up GodFather, You Are Alone");
        }
        else if (dl != null && sm == null){
            gf.receiveMassage("Wake up GodFather," + dl.getUserName() + " is doctor Lector");
            dl.receiveMassage("Your TeamMate is " + gf.getUserName());
        }
        else if (sm != null && dl == null){
            gf.receiveMassage("Wake Up GodFather," + sm.getUserName() + " is simple mafia");
            sm.receiveMassage("Your TeamMate is " + gf.getUserName());
        }
        else if (sm != null && dl != null){
            gf.receiveMassage("Wake up GodFather," + dl.getUserName() + " is doctor Lector and " + sm.getUserName() + " is simple mafia");
            sm.receiveMassage("Your TeamMates is " + gf.getUserName() + " and " + dl.getUserName());
            dl.receiveMassage("Your TeamMates is " + gf.getUserName() + " and " + sm.getUserName());
        }
    }

    /**
     * Intro night.
     */
    public void introNight(){
        server.sendMassageToPlayers("Intro Night Started");
        sendMassageMafias();
        sendMassageToMayor();
        server.sendMassageToPlayers("Intro Night Ended");
    }

    /**
     * Add massage to history.
     *
     * @param massage the massage
     */
    public synchronized void addMassageToHistory(String massage){
        chatHistory.addMassage(massage);
    }

    /**
     * Get chat history string.
     *
     * @return the string
     */
    public String getChatHistory(){
        return chatHistory.get();
    }

    /**
     * Discussion.
     */
    public void discussion(){
        server.sendDayList();
        server.sendMassageToPlayers("It's time to discussion");
        game.startDiscussion();
    }

    private String getSimpleMafiaOpinion(){
        PlayerOnServer simpleMafia = game.getPlayerThread("SimpleMafia");
        if (simpleMafia == null){
            return "NoBody";
        }
        if (!simpleMafia.alive()) {
            return "NoBody";
        }
        String opinion = simpleMafia.opinion();
        return opinion;
    }
    private String getDoctorLectorOpinion(){
        PlayerOnServer doctorLector = game.getPlayerThread("DoctorLector");
        if (doctorLector == null){
            return "NoBody";
        }
        if (!doctorLector.alive()) {
            return "NoBody";
        }
        String opinion = doctorLector.opinion();
        return opinion;
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
        PlayerOnServer doctorLector = game.getPlayerThread("DoctorLector");
        if (doctorLector == null){
            return "NoBody";
        }
        server.sendMassageToPlayers(NightMassages.doctorLectorWakeUp);
        if (!doctorLector.alive()){
            sleep();
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
            sleep();
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
            sleep();
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
        PlayerOnServer professional = game.getPlayerThread("Professional");
        if (professional == null){
            return "NoBody";
        }
        server.sendMassageToPlayers(NightMassages.proWakeUp);
        if (!professional.alive()){
            sleep();
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
            sleep();
            server.sendMassageToPlayers(NightMassages.psychologistSleep);
            return "NoBody";
        }
        String action = psychologist.action();
        server.sendMassageToPlayers(NightMassages.psychologistSleep);
        return action;
    }
    private String ironSideAction(){
        PlayerOnServer ironSide = game.getPlayerThread("IronSide");
        if (ironSide == null){
            return "NoBody";
        }
        server.sendMassageToPlayers(NightMassages.ironSideWakeUp);
        if (!ironSide.alive()){
            sleep();
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
        sleep();
        server.sendMassageToPlayers(NightMassages.godFatherTarget);
        PlayerOnServer sm = game.getPlayerThread("SimpleMafia");
        PlayerOnServer dl = game.getPlayerThread("DoctorLector");
        PlayerOnServer gf = game.getPlayerThread("GodFather");

        if (dl == null && sm == null){
            return godFatherAction();
        }
        else if (dl == null && sm != null){
            String simpleMafiaOpinion = getSimpleMafiaOpinion();
            if (gf.alive()){
                gf.receiveMassage(sm.getUserName() + " says we have to kill " + simpleMafiaOpinion);
                return godFatherAction();
            }
            else {
                return simpleMafiaOpinion;
            }
        }
        else if (dl != null && sm == null){
            String doctorLectorOpinion = getDoctorLectorOpinion();
            if (gf.alive()){
                gf.receiveMassage(dl.getUserName() + " says we have to kill " + doctorLectorOpinion);
                return godFatherAction();
            }
            else {
                return doctorLectorOpinion;
            }
        }
        else if (dl != null && sm != null){
            String simpleMafiaOpinion = getSimpleMafiaOpinion();
            String doctorLectorOpinion = getDoctorLectorOpinion();
            if (gf.alive()){
                gf.receiveMassage(sm.getUserName() + " says we have to kill " + simpleMafiaOpinion + " and " + dl.getUserName() + " says we have to kill " + doctorLectorOpinion);
                return godFatherAction();
            }
            else {
                if (dl.alive()){
                    return doctorLectorOpinion;
                }
                else return simpleMafiaOpinion;
            }
        }
        return "NoOne";
    }

    /**
     * Night.
     */
    public void night(){
        if (!gameStarted){
            introNight();
            gameStarted = true;
            return;
        }
        server.sendMassageToPlayers(NightMassages.eveyOneSleep);
        sleep();
        String mafiaAction = mafiaTarget();
        server.sendMassageToPlayers(NightMassages.mafiaSleep);
        sleep();
        String doctorLectorAction = doctorLectorAction();
        sleep();
        String physicianAction = physicianAction();
        sleep();
        detectorAction();
        sleep();
        String psychoAction = psychologistAction();
        sleep();
        String professionalAction = professionalAction();
        sleep();
        String ironSideAction = ironSideAction();
        sleep();
        server.sendMassageToPlayers(NightMassages.everyOneWakeUp);
        sleep();
        handleNightEvents(mafiaAction,doctorLectorAction,physicianAction,professionalAction);
        makeSilent(psychoAction);
        sleep();
        ironSideActionHandle(ironSideAction);
    }
    private void killsOrSavesHandle(String[] actions){
        kill(actions[0]);

        if (actions[3].equals(actions[2])){
            actions[2] = "NoBody";
        }

        kill(actions[3]);

        save(actions[1]);
        save(actions[2]);

    }
    private void handleNightEvents(String... actions){
        String events = "";
        killsOrSavesHandle(actions);

        if (!server.isPlayerAlive(actions[0]) && !actions[0].equals("NoBody")){
            events += "We Lost " + actions[0] +'\n';
        }

        if (!server.isPlayerAlive(actions[3]) && !actions[3].equals("NoBody")){
            events += "We Lost " + actions[3];
        }

        if (events.length() == 0){
            events += "Last Night is a good night";
        }
        server.sendMassageToPlayers(events);
        playerDeadMenus(actions[0],actions[3]);
    }
    private void playerDeadMenus(String... actions){
        if (!server.isPlayerAlive(actions[0]) && !actions[0].equals("NoBody")){
            server.playerDeadMenu(actions[0]);
        }
        if (!server.isPlayerAlive(actions[1]) && !actions[1].equals("NoBody")){
            server.playerDeadMenu(actions[1]);
        }
    }
    private void kill(String userName){
        game.kill(userName);
    }

    /**
     * Start voting.
     */
    public void startVoting(){
        server.startVoting();
    }
    private void executePlayer(String userName){
        game.execute(userName);

    }

    /**
     * Set vote.
     *
     * @param userName the user name
     */
    public void setVote(String userName){
        Integer count = votes.get(userName);
        count += 1;
        votes.replace(userName,count);
    }
    private Integer maxVote(){
        return Collections.max(votes.values());
    }

    /**
     * Check the votes are regular boolean.
     *
     * @return the boolean
     */
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
        server.sendMassageToPlayers(userName + " is silenced by psychologist");
    }

    /**
     * Check votes.
     *
     * @param votesList the votes list
     */
    public void checkVotes(String votesList) {
        server.sendMassageToPlayers("We Are Waiting for Mayor Agreement...");
        PlayerOnServer mayor = game.getPlayerThread("Mayor");
        String MAYORAction = "";
        if (!mayor.alive()) MAYORAction = "0";
        else MAYORAction = mayor.action();
        if (MAYORAction.equals("1")) {
            server.sendMassageToPlayers("No One Executed");
            server.sendMassageToPlayers("VOTES : \n" + votesList);
            clearVotes();
            startGame();
        }
        if (!checkTheVotesAreRegular() || noOneHasMaxVotes()) {
            server.sendMassageToPlayers("No One Executed");
            server.sendMassageToPlayers("VOTES : " + votesList);
            clearVotes();
            startGame();
        } else {
            for (int i = 0; i < server.getCapacity(); ++i) {
                String userName = game.getUserName(i);
                if (votes.get(userName).equals(maxVote())) {
                    execute(userName,votesList);

                }
            }
        }

    }
    private void save(String userName) {
        game.save(userName);
    }

    private void execute(String userName,String votesList) {
        executePlayer(userName);
        server.sendMassageToPlayers(userName + " has been executed , ask his role from iron side :D");
        server.sendMassageToPlayers("VOTES : " + votesList);
        clearVotes();
        server.playerDeadMenu(userName);
        startGame();
    }


    /**
     * Start game.
     */
    public void startGame() {
        if (game.mafiaWins() || game.citizensWins()){
            scoreBoard();
        }
        night();
        if (game.mafiaWins() || game.citizensWins()){
            scoreBoard();
        }
        discussion();
        startVoting();
    }
    private void scoreBoard(){

        String scoreBoard = game.scoreBoard();

        server.sendMassageToPlayers(scoreBoard);
        server.sendMassageToPlayers("end");
        server.endGame();
    }
}
