package zare.alireza.Roles;

import java.io.Serializable;
import java.util.Scanner;

public interface Role extends Serializable {
    public int action(String list, Scanner scanner);
    public void printInformation();
    public char getInvestigation();
}
