package zare.alireza.Roles;

public class DoctorLector extends Mafia{

    public DoctorLector(){
        super();
        information =
                "Doctor Lector can chooses a mafia every night to save him from Professional Shot."+
                "\nAnd He Can Saves Himself Once.";
    }
    @Override
    public void action() {

    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE Doctor Lector: \n"+ information);
    }
}
