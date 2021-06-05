package zare.alireza.ClientSide;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientProgram {

    private Scanner scanner;

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
            boolean portIsValid = port.length() == 4 && !Character.isAlphabetic(port.charAt(0));
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

    private Socket connectToServer(){
        int port = scanPort();
        try {
            return new Socket("localhost",port);
        } catch (IOException e) {
            System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
        }
        return null;
    }

    private void goToGame(){
        Socket socket  = connectToServer();
        if (socket == null){
            System.out.println("Something Went Wrong");
            return;
        }


        try {
            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);

            String userName;
            while (true){
                System.out.print("Enter UserName: ");
                userName = scanner.next();
                dataOutputStream.writeUTF(userName);

                String massageFromServer = dataInputStream.readUTF();

                if (massageFromServer.equals("DONE")) {
                    System.out.println("Successfully Logged In To Server");
                    break;
                }
                else System.out.println("SERVER: " + massageFromServer);
            }


        } catch (IOException e) {
            System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
        }
    }
}