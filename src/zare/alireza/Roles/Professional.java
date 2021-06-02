package zare.alireza.Roles;

public class Professional extends Citizen{

    public Professional(){
        super();
        information = "You Can Shot at night , but be careful ; if you shot citizens you will die";
    }
    @Override
    public void action() {
        //TODO: IF HE WANTS SHOT
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE PROFESSIONAL: \n"+ information);
    }
}
