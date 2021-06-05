package zare.alireza.ClientSide;

import zare.alireza.Roles.*;
import java.io.Serializable;

public class User implements Serializable {
    private String Username;
    private transient Role role;
    private transient boolean isAlive;
    public User(String username) {
        Username = username;
        isAlive = true;
        role = null;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return Username;
    }

    public void killOrExecution(){
        isAlive = false;
    }
}
