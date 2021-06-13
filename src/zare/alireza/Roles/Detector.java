package zare.alireza.Roles;

import java.util.Scanner;

public class Detector extends Citizen{

    public Detector(){
        super();
        information = "hello detector,you must ask god for investigations , if it's 'W' you give answer \uD83D\uDD93 else if it's 'B' you give answer \uD83D\uDD92 ";
    }
    @Override
    public int action(String list, Scanner scanner) {
        int result;
        System.out.println(list);
        System.out.println("well, WHO?");

        while (true){
            String index = scanner.next();
            if (list.contains("(" + index + ")")){
                result = Integer.parseInt(index);
                break;
            }
        }
        return result;
    }

    @Override
    public void printInformation() {
        System.out.println("YOU ARE DETECTOR: \n"+ information);
    }

    @Override
    public char getInvestigation() {
        return investigation;
    }
}
