package zare.alireza.ServerSide;

import zare.alireza.GameLogic.ManageGame.GameManager;
import zare.alireza.GameLogic.ManageGame.Game;
import zare.alireza.Roles.*;
import zare.alireza.ServerSide.PlayerThreads.PlayerOnServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Role> rolesForGiveToPlayers;
    private HashMap<String, PlayerOnServer> rolesForGame;
    private ArrayList<String> userNames;
    private ArrayList<PlayerOnServer> threads;
    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
        }
        rolesForGiveToPlayers = RolesList.get();
        rolesForGame = new HashMap<>();
        userNames = new ArrayList<>();
        threads = new ArrayList<>();
    }
    private boolean userNameIsValid(String userName){
        for (String un : userNames){
            if (un.equalsIgnoreCase(userName)) return false;
        }
        return true;
    }
    private synchronized String getUserNameFromClient(DataOutputStream dataOutputStream,DataInputStream dataInputStream){
        String userName;
        while (true){
            try {
                userName = dataInputStream.readUTF();
                if (userNameIsValid(userName)) {
                    dataOutputStream.writeUTF("DONE");
                    return userName;
                }
                else dataOutputStream.writeUTF("UserName Is Already Taken,Try Again");
            } catch (IOException e) {
                System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
            }
        }
    }
    private Role giveRoleToPlayer(){
        Role role = rolesForGiveToPlayers.get(0);
        rolesForGiveToPlayers.remove(0);
        return role;
    }
    private void mapRoleToPlayerThread(Role role, PlayerOnServer pt){
        rolesForGame.put(role.getClass().getSimpleName(),pt);
    }

    private void sendRoleInformationToClient(Role role,OutputStream outputStream){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(role);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void serverStart(){

        int clientsConnectedCounter = 0;
        while (clientsConnectedCounter != 4){

            try {
                Socket socket = serverSocket.accept();

                System.out.println(clientsConnectedCounter);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String newUserName = getUserNameFromClient(dataOutputStream,dataInputStream);
                userNames.add(newUserName);
                Role role = giveRoleToPlayer();
                PlayerOnServer newClientThread = new PlayerOnServer(role,newUserName,this,dataInputStream,dataOutputStream);
                mapRoleToPlayerThread(role,newClientThread);
                sendRoleInformationToClient(role,socket.getOutputStream());
                threads.add(newClientThread);
                //newClientThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            clientsConnectedCounter += 1;
            if (clientsConnectedCounter == 3){
                GameManager gameManager = new GameManager(this,new Game(rolesForGame,userNames,threads));
                setGameManagerForThreads(gameManager);
                gameManager.introNight();
            }
        }


    }
    private void setGameManagerForThreads(GameManager gameManager){
        for (PlayerOnServer ps : threads) {
            ps.setGameManager(gameManager);
        }
    }
    private void startClientThreads(){
        for (PlayerOnServer ps : threads) {
            ps.start();
        }
    }
    public void sendMassageToPlayers(String massage){
        for (PlayerOnServer ps : threads){
            ps.receiveMassage(massage);
        }
    }
}
