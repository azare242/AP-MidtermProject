package zare.alireza.Roles;

import java.util.Scanner;

/**
 * The type Citizen.
 */
public abstract class Citizen implements Role{
    /**
     * The Information.
     */
    protected String information;
    /**
     * The Investigation.
     */
    protected char investigation;

    /**
     * Instantiates a new Citizen.
     */
    public Citizen(){
        super();
        investigation = 'W';
    }
    public abstract int action(String list, Scanner scanner);
    public abstract void printInformation();
    public abstract char getInvestigation();
}
