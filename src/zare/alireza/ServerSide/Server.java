package zare.alireza.ServerSide;

import zare.alireza.Roles.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Role> rolesForGiveToPlayers;
    private HashMap<String, Role> rolesForGame;
    private ArrayList<String> userNames;
    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
        }
        rolesForGiveToPlayers = RolesList.get();
        rolesForGame = new HashMap<>();
        userNames = new ArrayList<>();
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
    public void serverStart(){

        int clientsConnectedCounter = 1;
        while (clientsConnectedCounter <= 10){
            try {
                Socket socket = serverSocket.accept();
                String newUserName = getUserNameFromClient(socket.getInputStream(),socket.getOutputStream());
                userNames.add(newUserName);

                //TODO:ClientThreads Start
                clientsConnectedCounter++;
            } catch (IOException e) {
                System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
            }
        }
        //TODO: GAME START
    }
}
