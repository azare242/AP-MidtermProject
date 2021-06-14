package zare.alireza.Roles;

import java.util.Scanner;

public class Professional extends Citizen{

    public Professional(){
        super();
        information = "You Can Shot at night , but be careful ; if you shot citizens you will die";
    }
    @Override
    public int action(String list, Scanner scanner) {
        int result;
        System.out.println(list);
        System.out.println("well, WHO?");
        System.out.println("0 for nobody");
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
