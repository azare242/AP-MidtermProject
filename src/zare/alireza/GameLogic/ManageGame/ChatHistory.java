package zare.alireza.GameLogic.ManageGame;

/**
 * The type Chat history.
 */
public class ChatHistory {

    private String toSend;

    /**
     * Instantiates a new Chat history.
     */
    public ChatHistory(){
        toSend = "";
    }

    /**
     * Add massage.
     *
     * @param massage the massage
     */
    public synchronized void addMassage(String massage){
        toSend += massage + '\n';
    }

    /**
     * Get string.
     *
     * @return the string
     */
    public String get() {
        return toSend;
    }
}
