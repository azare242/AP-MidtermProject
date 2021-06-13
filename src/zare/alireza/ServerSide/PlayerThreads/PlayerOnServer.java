package zare.alireza.ServerSide.PlayerThreads;


import zare.alireza.ServerSide.Server;
import zare.alireza.Roles.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public  class PlayerOnServer extends Thread{

    private Role role;
    private String userName;
    private boolean isAlive = true;
    private boolean onGame = true;
    private boolean isReady = false;
    private boolean canTalk = true;
    private boolean cankill = true;
    private DataInputStream receiver;
    private DataOutputStream sender;
    private Server server;
    private Socket socket;

   public PlayerOnServer(Server server,DataOutputStream dataOutputStream,DataInputStream dataInputStream,Socket socket){
       this.server = server;

       receiver = dataInputStream;
       sender = dataOutputStream;
       this.socket = socket;
   }


   public void silent(){
       canTalk = false;
   }
   public void execute(){
       this.isAlive = false;
   }
    public void kill(){
       if (!cankill){
           cankill = true;
           return;
       }
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
       setRole(role);
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


    public void voting(){
       new VotingThread(this,server,sender,receiver).start();
    }
    public void chat(){
       try {
           if (!canTalk){
               sender.writeUTF("oops,You are silenced by Psychologist,so you cant talk");
               canTalk = true;
               return;
           }

           sender.writeUTF("chat_time");
           while (true){
               String massage = receiver.readUTF();

               if (massage.equalsIgnoreCase("HISTORY")){
                   sender.writeUTF(server.getChatHistory());
               }
               else if (massage.equalsIgnoreCase("done")){
                   break;
               }
               else if (massage.equalsIgnoreCase("exit")){
                   isAlive = false;
                   onGame = false;
                   String massageToSend =  "\"" + userName + "\" : " + "i quit the match";

                   server.aPlayerSendsMassageToOtherPlayers(massageToSend,this);
                   sender.close();
                   receiver.close();
                   socket.close();
                   break;
               }
               else {
                   String massageToSend =  "\"" + userName + "\" : " + massage;

                   server.addMassageToHistory(massageToSend);
                   server.aPlayerSendsMassageToOtherPlayers(massageToSend,this);
               }
           }
       }catch (IOException e){
           e.printStackTrace();
       }
    }
    private void setRole(Role role){
       this.role = role;
       if (role.getClass().getSimpleName().equals("IronSide")){
           cankill = false;
       }
    }
    public boolean alive(){
       return isAlive;
    }
    public boolean isOnGame(){
       return onGame;
    }
    public boolean isMafia(){
       return role instanceof Mafia;
    }

}
