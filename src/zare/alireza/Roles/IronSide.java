package zare.alireza.Roles;

import java.util.Scanner;

public class IronSide extends Citizen{

    public IronSide() {
        super();
        information = "you can ask god to tell everyone which roles are out of the game, don't tell anyone, if mafia shot you once it's not affect";
    }

    @Override
    public int action(String list, Scanner scanner) {
        System.out.println("Do You Want Use Your Ability?[yes/no]");
        while (true) {

            String answer = scanner.next();
            if (answer.equalsIgnoreCase("yes")) {
                return 1;
            } else if (answer.equalsIgnoreCase("no")) {
                return 0;
            }
            else System.out.println("invalid input try again");
        }

    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE IRON SIDE: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
