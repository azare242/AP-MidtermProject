package zare.alireza.Roles;

import java.util.Scanner;

public abstract class Mafia implements Role{
    protected String information;
    protected char investigation;

    public Mafia(){
        super();
        investigation = 'B';
    }

    public abstract int action(String list, Scanner scanner);
    public abstract void printInformation();
    public abstract char getInvestigation();
}
