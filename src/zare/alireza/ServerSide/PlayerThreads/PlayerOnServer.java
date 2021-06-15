package zare.alireza.ServerSide.PlayerThreads;


import zare.alireza.ServerSide.Server;
import zare.alireza.Roles.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * The type Player on server.
 */
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

    /**
     * Instantiates a new Player on server.
     *
     * @param server           the server
     * @param dataOutputStream the data output stream
     * @param dataInputStream  the data input stream
     * @param socket           the socket
     */
    public PlayerOnServer(Server server,DataOutputStream dataOutputStream,DataInputStream dataInputStream,Socket socket){
       this.server = server;

       receiver = dataInputStream;
       sender = dataOutputStream;
       this.socket = socket;
   }

    /**
     * Leave game.
     */
    public void leaveGame(){
        try {

            isAlive = false;
            onGame = false;
            String massageToSend = "\"" + userName + "\" : " + "i quit the match";

            server.aPlayerSendsMassageToOtherPlayers(massageToSend, this);
            sender.close();
            receiver.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * End game.
     */
    public void endGame(){
       try {
           sender.close();
           receiver.close();
           socket.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    /**
     * Dead menu.
     */
    public void deadMenu(){
        try {
            sender.writeUTF("you_dead");
            String answer = receiver.readUTF();
            if (answer.equals("y")){
                return;
            }
            else if (answer.equals("n")){
                onGame = false;
                receiver.close();
                sender.close();
                socket.close();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Silent.
     */
    public void silent(){
       canTalk = false;
   }

    /**
     * Execute.
     */
    public void execute(){
       this.isAlive = false;
   }

    /**
     * Kill.
     */
    public void kill(){
       if (!cankill){
           cankill = true;
           return;
       }
        this.isAlive = false;
    }

    /**
     * Saved.
     */
    public void saved() {
       this.isAlive = true;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
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
                    if (server.userNameIsValid(userName) && !userName.equalsIgnoreCase("NoBody")){
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

    /**
     * Receive massage.
     *
     * @param massage the massage
     */
    public void receiveMassage(String massage){
        try {
            sender.writeUTF(massage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Voting.
     */
    public void voting(){
       new VotingThread(this,server,sender,receiver).start();
    }

    /**
     * Chat.
     */
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
                   leaveGame();
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

    /**
     * Action string.
     *
     * @return the string
     */
    public String action() {

        String action = "";
        try {
            sender.writeUTF("action");


            if (isDoctorLector()){
                server.sendListToDoctorLector(this);
                String chose = receiver.readUTF();
                int index = Integer.parseInt(chose);
                action = server.getPlayerUserName(index);
            }
            else if (isIronSide()){
                sender.writeUTF("IronSide Doesn't need LIST");
                String chose = receiver.readUTF();
                if (chose.equals("0")){
                    return "NO";
                }
                else return "YES";
            }
            else if (isMayor()){
                sender.writeUTF("Mayor Doesn't need LIST");
                String chose = receiver.readUTF();
                return chose;
            }
            else {
                server.getAlivePlayersListToAPlayer(this);
                String chose = receiver.readUTF();
                int index = Integer.parseInt(chose);
                if (index == 0) action = "NoBody";
                else action = server.getPlayerUserName(index);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * Opinion string.
     *
     * @return the string
     */
    public String opinion(){
        String opinion = "";
        try {
            sender.writeUTF("opinion");
            server.getAlivePlayersListToAPlayer(this);
            String chose = receiver.readUTF();
            int index = Integer.parseInt(chose);
            opinion = server.getPlayerUserName(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return opinion;
    }
    private void setRole(Role role){
       this.role = role;
       if (role.getClass().getSimpleName().equals("IronSide")){
           cankill = false;
       }
        System.out.println(userName + " Role is " + role.getClass().getSimpleName());
    }

    /**
     * Is mayor boolean.
     *
     * @return the boolean
     */
    public boolean isMayor(){
        return role.getClass().getSimpleName().equals("Mayor");
    }

    /**
     * Alive boolean.
     *
     * @return the boolean
     */
    public boolean alive(){
        return isAlive;
    }

    /**
     * Is on game boolean.
     *
     * @return the boolean
     */
    public boolean isOnGame(){
        return onGame;
    }

    /**
     * Is iron side boolean.
     *
     * @return the boolean
     */
    public boolean isIronSide(){
        return this.role.getClass().getSimpleName().equals("IronSide");
    }

    /**
     * Investigation boolean.
     *
     * @return the boolean
     */
    public boolean investigation(){
        return role.getInvestigation() == 'B';
    }

    /**
     * Is mafia boolean.
     *
     * @return the boolean
     */
    public boolean isMafia(){
        return role instanceof Mafia;
    }

    /**
     * Is doctor lector boolean.
     *
     * @return the boolean
     */
    public boolean isDoctorLector(){
        return role.getClass().getSimpleName().equals("DoctorLector");
    }

    /**
     * Get role name string.
     *
     * @return the string
     */
    public String getRoleName(){
        return role.getClass().getSimpleName();
    }
}
