package zare.alireza.Roles;

public class Psychologist extends Citizen{

    public Psychologist(){
        super();
        information = "you can silent someone at night, he can't speak at day";
    }
    @Override
    public void action() {
        //TODO: SILENT SOMEONE
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE PSYCHOLOGIST: \n"+ information);
    }
}
