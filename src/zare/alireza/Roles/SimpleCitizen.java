package zare.alireza.Roles;

public class SimpleCitizen extends Citizen{

    public SimpleCitizen(){
        super();
        information = "let's help to find mafias and take them out";
    }

    @Override
    public void action() {
        //TODO: IDK :|
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE SIMPLE CITIZEN: \n"+ information);
    }
}
