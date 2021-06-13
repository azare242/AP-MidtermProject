package zare.alireza.ClientSide;

import zare.alireza.Roles.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientProgram {

    private Scanner scanner;
    private Role role;

    public ClientProgram(){
        scanner = new Scanner(System.in);
    }

    private void showMainMenu(){
        System.out.print(
                "‖1.Play\n‖2.Exit\n⮞ "
        );
    }
    public void run(){
        boolean running = true;

        while (running){
            showMainMenu();
            String chose = scanner.next();
            switch (chose){
                case "1" -> {
                    goToGame();
                    return;
                }
                case "2" -> running = false;
                default -> System.out.println("Invalid Input");
            }
        }
    }

    private int scanPort() {
        String port;
        while (true) {
            System.out.println("Enter Game Port: ");
            port = scanner.next();
            boolean portIsValid = portIsValid(port);
            if (portIsValid){
                break;
            }
            else System.out.println("Invalid Input");
        }
        return Integer.parseInt(port);
    }
    private boolean portIsValid(String port){
        if (port.length() != 4) return false;

        for (int i = 0 ; i < 4 ; ++i){
            if (Character.isAlphabetic(port.charAt(i))) return false;
        }
        return true;
    }



    private void goToGame() {
        int port = scanPort();
        Socket socket = null;
        try {
            socket = new Socket("localhost", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket == null) {
            System.out.println("Something Went Wrong");
            return;
        }
        listeningToServer(socket);
    }
    private void waitingForServer(){
        System.out.println(
                "**************\nWaiting For Server...\n**************"
        );
    }
    private void listeningToServer(Socket socket){
        try {
            DataOutputStream sender = new DataOutputStream(socket.getOutputStream());
            DataInputStream receiver = new DataInputStream(socket.getInputStream());
            while (true){
                waitingForServer();
                String serverMassage = receiver.readUTF();
                switch (serverMassage){
                    case "game_is_full" :
                        System.out.println("Game is Full");
                        return;
                    case "username" :
                        sendUsername(receiver,sender);
                        break;
                    case "r_u_ready?" :
                        ready(sender);
                        break;
                    case "give_your_role":
                        giveRole(receiver);
                        break;
                    case "opinion":
                        sendOpinion(sender,receiver);
                        break;
                    case "action":
                        action(receiver,sender);
                        break;
                    case "chat_time":
                        boolean exited = chatting(sender,receiver);
                        if (exited){
                            System.out.println("You Exited The Game NOOB");
                            receiver.close();
                            sender.close();
                            socket.close();
                            return;
                        }
                        break;
                    case "voting":
                        voting(sender,receiver);
                        break;
                    default:
                        System.out.println(serverMassage);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendUsername(DataInputStream dataInputStream,DataOutputStream dataOutputStream){
        System.out.print("Enter Your Username: ");
        while (true){
            String userName = scanner.next();
            try {
                dataOutputStream.writeUTF(userName);

                String response = dataInputStream.readUTF();
                if (response.equals("done")){
                    System.out.println("User Name is agreed");
                    break;
                }
                else if (response.equals("invalid")){
                    System.out.println("User Name is invalid");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void ready(DataOutputStream dataOutputStream){
        System.out.println("Are You Ready?[Enter 1]");
        while (true){
            String response = scanner.next();
            if (response.equals("1")){
                try {
                    dataOutputStream.writeUTF("im_ready");
                    break;
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            else System.out.println("invalid input try again");
        }
    }
    private Role initRole(String className){
        switch (className){
            case "GodFather" : return new GodFather();
            case "DoctorLector" : return new DoctorLector();
            case "SimpleMafia" : return new SimpleMafia();
            case "Detector" : return new Detector();
            case "Physician" : return new Physician();
            case "Psychologist" : return new Psychologist();
            case "Professional" : return new Professional();
            case "Mayor" : return new Mayor();
            case "IronSide" : return new IronSide();
            case "SimpleCitizen" : return new SimpleCitizen();
        }
        return null;
    }
    private void giveRole(DataInputStream dataInputStream){
        try {
            String roleName = dataInputStream.readUTF();
            Role role = initRole(roleName);
            this.role = role;
            role.printInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean chatting(DataOutputStream dataOutputStream,DataInputStream dataInputStream){
        System.out.println("Your Turn to chat: ");
        String garbage = scanner.nextLine();
        while (true){
            try {
                System.out.print("Send Massage: [at end enter \"done\"] : ");
                String massage = scanner.nextLine();
                if (massage.equalsIgnoreCase("HISTORY")){
                    dataOutputStream.writeUTF("HISTORY");
                    String history = dataInputStream.readUTF();
                    System.out.println(history);
                }
                else if (massage.equalsIgnoreCase("exit")){
                    dataOutputStream.writeUTF("exit");
                    return true;
                }
                else if (massage.equalsIgnoreCase("done")){
                    dataOutputStream.writeUTF("done");
                    break;
                }
                else dataOutputStream.writeUTF(massage);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    private boolean indexIsValid(String index , String list){
        return list.contains("(" + index + ")");
    }
    private void sendOpinion(DataOutputStream dataOutputStream,DataInputStream dataInputStream){
        try {
            String list = dataInputStream.readUTF();
            System.out.println(list);
            System.out.println("Chose Your Opinion to tell GodFather: ");
            while (true){
                String index = scanner.next();
                if (indexIsValid(index,list)){
                    dataOutputStream.writeUTF(index);
                    break;
                }
                else System.out.println("invalid input , try again");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void action(DataInputStream dataInputStream,DataOutputStream dataOutputStream){
        try {
            String list = dataInputStream.readUTF();

            int index = role.action(list,scanner);
            dataOutputStream.writeUTF(String.valueOf(index));;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void voting(DataOutputStream dataOutputStream,DataInputStream dataInputStream){

        try {
            System.out.println("Voting Time [for vote to noOne enter 0]");
            String list = dataInputStream.readUTF();
            System.out.println(list);
            while (true){
                String chose = scanner.next();
                if (indexIsValid(chose,list) || chose.equals("0")){
                    dataOutputStream.writeUTF(chose);
                    break;
                }
                else System.out.println("invalid input , try again");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
