package zare.alireza.Roles;

import javax.management.timer.TimerNotification;
import java.util.Scanner;

/**
 * The type Physician.
 */
public class Physician extends Citizen{

    private boolean canSaveHimself = true;

    /**
     * Instantiates a new Physician.
     */
    public Physician() {
        super();
        information =
                "You Can Save One Person During night and he will not kill by anyone";
    }
    @Override
    public int action(String list, Scanner scanner) {
        int result;
        System.out.println(list);
        System.out.println("(0) - yourself");
        System.out.println("well, WHO?");

        while (true){
            String index = scanner.next();
            if (index.equals("0")){
                if (canSaveHimself) {
                    canSaveHimself = false;
                    return 0;
                }
                else System.out.println("you saved your self once!");
            }
            if (list.contains("(" + index + ")")){
                result = Integer.parseInt(index);
                break;
            }
        }
        return result;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE PHYSICIAN: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
