package zare.alireza.ServerSide.PlayerThreads;

import zare.alireza.GameLogic.ManageGame.GameManager;
import zare.alireza.ServerSide.Server;
import zare.alireza.Roles.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public  class PlayerOnServer extends Thread{

    private Role role;
    private String userName;
    private boolean isAlive = true;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private GameManager gameManager;
    private Server server;


    public PlayerOnServer(Role role, String userName,Server server,DataInputStream dataInputStream,DataOutputStream dataOutputStream) {
        this.role = role;
        this.userName = userName;
        this.server = server;
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void killOrExecute(){
        this.isAlive = true;
    }



    public String getUserName() {
        return userName;
    }

    @Override
    public void run(){
        try {
            dataOutputStream.writeUTF("READY?");
            dataOutputStream.writeUTF("READY?");
            dataOutputStream.writeUTF("READY?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void receiveMassage(String massage){
        try {
            dataOutputStream.writeUTF(massage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
