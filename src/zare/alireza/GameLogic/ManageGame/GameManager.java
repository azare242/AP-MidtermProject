package zare.alireza.GameLogic.ManageGame;

import zare.alireza.ServerSide.Server;

public class GameManager {

    private Game game;
    private Server server;

    public GameManager(Server server,Game game){
        this.game = game;
        this.server = server;
    }

    public void introNight(){
        server.sendMassageToPlayers("Intro Night Started");
        server.sendMassageToPlayers("Intro Night Ended");
    }

}
