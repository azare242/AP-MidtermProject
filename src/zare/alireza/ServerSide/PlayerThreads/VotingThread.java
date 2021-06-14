package zare.alireza.ServerSide.PlayerThreads;


import zare.alireza.ServerSide.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VotingThread extends Thread{

    private Server server;
    private DataOutputStream sender;
    private DataInputStream receiver;
    private PlayerOnServer thisPlayer;

    public VotingThread(PlayerOnServer player, Server server, DataOutputStream dataOutputStream, DataInputStream dataInputStream){
        this.server = server;
        sender = dataOutputStream;
        receiver = dataInputStream;
        thisPlayer = player;
    }

    @Override
    public void run(){
        String vote = "";
        try {
            sender.writeUTF("voting");
            server.getAlivePlayersListToAPlayer(thisPlayer);

            String chose = receiver.readUTF();
            if (chose.equalsIgnoreCase("exit")){
                thisPlayer.leaveGame();;
                return;
            }
            int index = Integer.parseInt(chose);
            if (index == 0) server.setVote("NoOne",thisPlayer.getUserName());
            else {
                vote = server.getPlayerUserName(index);
                server.setVote(vote,thisPlayer.getUserName());
            }
            if (server.allPlayersVoted()){
                server.checkVotes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
