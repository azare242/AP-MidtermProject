package zare.alireza.Roles;

public class SimpleMafia extends Mafia{

    public SimpleMafia(){
        super();
        information =
                "You are just a normal member of mafia"+
                "\nTry To Win!";
    }
    @Override
    public void action() {
        //TODO: IDK :|
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE SIMPLE MAFIA: \n"+ information);
    }
}
