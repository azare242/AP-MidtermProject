package zare.alireza.Roles;

import java.util.Scanner;

public abstract class Citizen implements Role{
    protected String information;
    protected char investigation;

    public Citizen(){
        super();
        investigation = 'W';
    }
    public abstract int action(String list, Scanner scanner);
    public abstract void printInformation();
    public abstract char getInvestigation();
}
