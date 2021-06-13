package zare.alireza.Roles;

import java.util.Scanner;

public class SimpleCitizen extends Citizen{

    public SimpleCitizen(){
        super();
        information = "let's help to find mafias and take them out";
    }

    @Override
    public int action(String list, Scanner scanner) {
        return 0;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE SIMPLE CITIZEN: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
