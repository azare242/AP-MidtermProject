package zare.alireza.Roles;

public class Physician extends Citizen{

    public Physician() {
        super();
        information =
                "You Can Save One Person During night and he will not kill by anyone";
    }
    @Override
    public void action() {
        //TODO: SAVE SOMEONE
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE PHYSICIAN: \n"+ information);
    }
}
