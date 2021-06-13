package zare.alireza.Roles;

import java.util.Scanner;

public class DoctorLector extends Mafia{

    public DoctorLector(){
        super();
        information =
                "Doctor Lector can chooses a mafia every night to save him from Professional Shot."+
                "\nAnd He Can Saves Himself Once.";
    }
    @Override
    public int action(String list, Scanner scanner) {
        int result;
        System.out.println(list);
        System.out.println("well, WHO?");

        while (true){
            String index = scanner.next();
            if (list.contains("(" + index + ")")){
                result = Integer.parseInt(index);
                break;
            }
        }
        return result;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE Doctor Lector: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
