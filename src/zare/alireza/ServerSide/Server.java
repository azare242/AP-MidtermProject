package zare.alireza.ServerSide;

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
    private String getUserNameFromClient(InputStream inputStream,OutputStream outputStream){
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
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

    private void sendRoleToClient(Role role , OutputStream outputStream){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(role);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void serverStart(){

        int clientsConnectedCounter = 1;
        while (clientsConnectedCounter <= 10){
            try {
                Socket socket = serverSocket.accept();
                String newUserName = getUserNameFromClient(socket.getInputStream(),socket.getOutputStream());
                userNames.add(newUserName);
                Role role = giveRoleToPlayer();
                PlayerOnServer playerOnServer = new PlayerOnServer(socket,role,newUserName);
                mapRoleToPlayerThread(role, playerOnServer);
                threads.add(playerOnServer);
                sendRoleToClient(role,socket.getOutputStream());
                clientsConnectedCounter++;
            } catch (IOException e) {
                System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
            }
        }

    }
    private void getReady(){

    }
}
