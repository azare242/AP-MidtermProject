package zare.alireza.Roles;

public class Detector extends Citizen{

    public Detector(){
        super();
        information = "hello detector,you must ask god for investigations , if it's 'W' you give answer \uD83D\uDD93 else if it's 'B' you give answer \uD83D\uDD92 ";
    }
    @Override
    public void action() {

    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE DETECTOR: \n"+ information);
    }
}
