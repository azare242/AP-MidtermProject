import zare.alireza.ServerSide.Server;

import java.util.Scanner;

public class ServerRun {

    private static boolean portIsValid(String port){
        if (port.length() != 4) return false;

        for (int i = 0 ; i < 4 ; ++i){
            if (Character.isAlphabetic(port.charAt(i))) return false;
        }
        return true;
    }
    private static int playersCount = 10;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("1/Start New Game\n2/Set Players Count\n3/Exit");
            String chose = scanner.next();
            switch (chose){
                case "1" -> {
                    System.out.println("Enter Port To Creat a Game: ");
                    while (true) {
                        String port = scanner.next();
                        if (portIsValid(port)) {
                            new Server(Integer.parseInt(port)).serverStart();
                            return;
                        } else System.out.println("Invalid port");
                    }
                }
                case "2" -> {
                    System.out.println("Enter Player Count: [minimum is 6]: ");

                    while (true) {
                        int count = scanner.nextInt();
                        if (count >= 6) {
                            playersCount = count;
                            break;
                        } else System.out.println("Invalid input try again");

                    }
                }
                case "3" -> {
                    return;
                }
            }
        }


    }
}
