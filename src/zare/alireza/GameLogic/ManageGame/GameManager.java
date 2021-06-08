package zare.alireza.GameLogic.ManageGame;

import zare.alireza.ServerSide.Server;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;
public class GameManager {

    private Game game;
    private Server server;

    public GameManager(Server server,Game game){
        this.game = game;
        this.server = server;
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

}
