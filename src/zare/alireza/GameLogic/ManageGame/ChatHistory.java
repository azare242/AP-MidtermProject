package zare.alireza.GameLogic.ManageGame;

public class ChatHistory {

    private String toSend;

    public ChatHistory(){
        toSend = "";
    }

    public synchronized void addMassage(String massage){
        toSend += massage + '\n';
    }

    public String get() {
        return toSend;
    }
}
