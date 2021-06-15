package zare.alireza.Roles;

import java.util.Scanner;

/**
 * The type Psychologist.
 */
public class Psychologist extends Citizen{

    /**
     * Instantiates a new Psychologist.
     */
    public Psychologist(){
        super();
        information = "you can silent someone at night, he can't speak at day";
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
        System.out.println("YOU ARE PSYCHOLOGIST: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
