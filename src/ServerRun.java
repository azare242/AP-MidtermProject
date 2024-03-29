import zare.alireza.ServerSide.Server;

import java.util.Scanner;

/**
 * The type Server run.
 */
public class ServerRun {

    private static boolean portIsValid(String port){
        if (port.length() != 4) return false;

        for (int i = 0 ; i < 4 ; ++i){
            if (Character.isAlphabetic(port.charAt(i))) return false;
        }
        return true;
    }
    private static int playersCount = 10;
    private static void welcome(){

        System.out.println(
                "HELLO , WELCOME TO SERVER APPLICATION FOR MAFIA GAME BY Alireza Zare Zeynabadi\n" +
                        "players count by default is 10\n"
        );
    }
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        welcome();
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
                            System.out.println("a game starter at port " + port + " HAVE FUN");
                            new Server(Integer.parseInt(port),playersCount).serverStart();
                            return;
                        } else System.out.println("Invalid port");
                    }
                }
                case "2" -> {
                    System.out.println("Enter Player Count: [maximum is 10]: ");

                    while (true) {
                        int count = scanner.nextInt();
                        if (count <= 10) {
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
