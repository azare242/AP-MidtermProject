package zare.alireza.Roles;

import java.util.Scanner;

/**
 * The type Mafia.
 */
public abstract class Mafia implements Role{
    /**
     * The Information.
     */
    protected String information;
    /**
     * The Investigation.
     */
    protected char investigation;

    /**
     * Instantiates a new Mafia.
     */
    public Mafia(){
        super();
        investigation = 'B';
    }

    public abstract int action(String list, Scanner scanner);
    public abstract void printInformation();
    public abstract char getInvestigation();
}
