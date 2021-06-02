package zare.alireza.Roles;

public abstract class Mafia implements Role{
    protected String information;
    protected char investigation;

    public Mafia(){
        super();
        investigation = 'B';
    }

    @Override
    public abstract void action();
    @Override
    public abstract void printInformation();
}
