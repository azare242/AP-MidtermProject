package zare.alireza.Roles;

import java.util.Scanner;

/**
 * The type God father.
 */
public class GodFather extends Mafia{

    /**
     * Instantiates a new God father.
     */
    public GodFather(){
        super();
        information =
                        "The leader of a mafia group, they are responsible for sending in the night kill information to the moderator." +
                        "\nThey can communicate with their group outside of the thread to help decide who to kill each night before sending a private message with the name of their target and who is to carry out the kill to the moderator." +
                        "\nSometimes The Godfather will be allowed to recruit new members but doing so usually forgoes the night kill." +
                        "\nIf investigated by a Cop, The Godfather comes up town-aligned, though this can change either when one member of his crew is killed or all the other members are killed, depending on the moderator’s preference."+
                        "\nIf The Godfather is killed, the mafia lose their night kill for the next night while a new Godfather is chosen.";
        investigation = 'W';

    }
    public void printInformation(){
        System.out.println("YOU ARE GOD FATHER: \n" + information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }

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

}
