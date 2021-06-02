package zare.alireza.Roles;

public abstract class Citizen implements Role{
    protected String information;
    protected char investigation;

    public Citizen(){
        super();
        investigation = 'W';
    }
    public abstract void action();
    public abstract void printInformation();
}
