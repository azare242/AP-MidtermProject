package zare.alireza.GameLogic.ManageGame;

import zare.alireza.ServerSide.Server;

public class GameManager {

    private Game game;
    private Server server;
    private int readyPlayers;

    public GameManager(Game game){
        this.game = game;
        readyPlayers = 0;
    }

    public void APlayerSaysReady(){
        readyPlayers++;
        if (readyPlayers == 10) {

        }
    }


}
