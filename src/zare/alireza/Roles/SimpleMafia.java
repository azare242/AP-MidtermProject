package zare.alireza.Roles;

import java.util.Scanner;

/**
 * The type Simple mafia.
 */
public class SimpleMafia extends Mafia{

    /**
     * Instantiates a new Simple mafia.
     */
    public SimpleMafia(){
        super();
        information =
                "You are just a normal member of mafia"+
                "\nTry To Win!";
    }
    @Override
    public int action(String list, Scanner scanner) {
        return 0;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE SIMPLE MAFIA: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
