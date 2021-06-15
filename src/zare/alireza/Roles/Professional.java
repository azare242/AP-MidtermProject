package zare.alireza.Roles;

import java.util.Scanner;

/**
 * The type Professional.
 */
public class Professional extends Citizen{

    /**
     * Instantiates a new Professional.
     */
    public Professional(){
        super();
        information = "You Can Shot at night , but be careful ; if you shot citizens you will die";
    }
    @Override
    public int action(String list, Scanner scanner) {
        int result;
        System.out.println(list);
        System.out.println("(0) - Not To Shot");
        System.out.println("well, WHO?");
        while (true){
            String index = scanner.next();
            if (list.contains("(" + index + ")") || index.equals("0")){
                result = Integer.parseInt(index);
                break;
            }
        }
        return result;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE PROFESSIONAL: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
