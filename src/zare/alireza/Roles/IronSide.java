package zare.alireza.Roles;

public class IronSide extends Citizen{

    public IronSide() {
        super();
        information = "you can ask god to tell everyone which roles are out of the game, don't tell anyone, if mafia shot you once it's not affect";
    }

    @Override
    public void action() {
        //TODO:ASK GOD FOR ROLES THAT OUT OF GAME
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE IRON SIDE: \n"+ information);
    }
}
