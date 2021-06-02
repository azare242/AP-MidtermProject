package zare.alireza.Roles;

public class Mayor extends Citizen{

    public Mayor(){
        super();
        information = "Mayor Can Cancel the election of day";
    }
    @Override
    public void action() {

    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE MAYOR: \n"+ information);

    }
}
