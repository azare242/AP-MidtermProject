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
    private boolean isReady = false;
    private DataInputStream receiver;
    private DataOutputStream sender;
    private Server server;


   public PlayerOnServer(Server server,DataOutputStream dataOutputStream,DataInputStream dataInputStream){
       this.server = server;

       receiver = dataInputStream;
       sender = dataOutputStream;
   }


    public void killOrExecute(){
        this.isAlive = false;
    }
    public void saved() {
       this.isAlive = true;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public void run(){
        if (!isReady){

            try {
                sender.writeUTF("username");
                while (true){
                    String userName = receiver.readUTF();
                    if (server.userNameIsValid(userName)){
                        server.addUserName(userName);
                        sender.writeUTF("done");
                        this.userName  = userName;
                        break;
                    }
                    else sender.writeUTF("invalid");
                }
                sender.writeUTF("r_u_ready?");
                String response = receiver.readUTF();
                if (response.equals("im_ready")){
                    server.increaseReadyPlayers();
                    sender.writeUTF("give_your_role");
                    sendRole();
                    if (server.allPlayersReady()){
                        server.startGame();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            isReady = true;
            return;
        }
    }

    private void sendRole(){
       Role role = server.giveRoleToPlayer();
       try {
           sender.writeUTF(role.getClass().getSimpleName());
           server.mapRoleToPlayerThread(role,this);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public void receiveMassage(String massage){
        try {
            sender.writeUTF(massage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
