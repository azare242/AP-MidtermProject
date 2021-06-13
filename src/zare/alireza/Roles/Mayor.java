package zare.alireza.Roles;

import java.util.Scanner;

public class Mayor extends Citizen{
    private boolean canCancelVoting = true;
    public Mayor(){
        super();
        information = "Mayor Can Cancel the election of day";
    }
    @Override
    public int action(String list, Scanner scanner) {
        int result = 0;
        if (canCancelVoting){
            System.out.println("Are You Want Cancel Voting?[1 -> yes , 0 -> no]");
            while (true){
                String chose = scanner.next();
                if (chose.equals("1") || chose.equals("0")) {
                    result = Integer.parseInt(chose);
                    if (chose.equals("1")) {
                        canCancelVoting = false;
                    }
                    break;
                }
            }
        }
        else {
            System.out.println("You Only Can Cancel The Voting Once");
        }
        return result;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE MAYOR: \n"+ information);

    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
