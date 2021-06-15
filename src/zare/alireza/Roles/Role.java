package zare.alireza.Roles;

import java.io.Serializable;
import java.util.Scanner;

/**
 * The interface Role.
 */
public interface Role extends Serializable {
    /**
     * Action int.
     *
     * @param list    the list
     * @param scanner the scanner
     * @return the int
     */
    public int action(String list, Scanner scanner);

    /**
     * Print information.
     */
    public void printInformation();

    /**
     * Gets investigation.
     *
     * @return the investigation
     */
    public char getInvestigation();
}
