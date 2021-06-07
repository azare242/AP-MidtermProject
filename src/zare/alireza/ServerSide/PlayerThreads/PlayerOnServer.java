package zare.alireza.ServerSide.PlayerThreads;

import TEST.GameManager;
import zare.alireza.Roles.*;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public  class PlayerOnServer extends Thread{

    private Socket socket;
    private Role role;
    private String userName;
    private boolean isAlive;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private GameManager gameManager;
    public PlayerOnServer(Socket socket, Role role, String userName) {
        this.socket = socket;
        this.role = role;
        this.userName = userName;


        try {
            dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            dataInputStream = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            while (true){
                String massage = dataInputStream.readUTF();

                switch (massage){
                    case "im_ready" ->  {
                        gameManager.APlayerSaysReady();
                    }
                    case "game_is_ready" -> {

                    }
                }
            }
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
