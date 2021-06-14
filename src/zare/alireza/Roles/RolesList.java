package zare.alireza.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RolesList {



    private static ArrayList<Role> initialize(int capacity) {
        ArrayList<Role> result = new ArrayList<>();

        if (capacity == 10) {
            result.add(new GodFather());
            result.add(new DoctorLector());
            result.add(new SimpleMafia());
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Professional());
            result.add(new IronSide());
            result.add(new Detector());
            result.add(new SimpleCitizen());
        }
        else if (capacity == 9){
            result.add(new GodFather());
            result.add(new DoctorLector());
            result.add(new SimpleMafia());
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Professional());
            result.add(new IronSide());
            result.add(new Detector());
        }
        else if (capacity == 8){
            result.add(new GodFather());
            result.add(new DoctorLector());
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Professional());
            result.add(new IronSide());
            result.add(new Detector());
        }
        else if (capacity == 7){
            result.add(new GodFather());
            result.add(new DoctorLector());
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Professional());
            result.add(new Detector());
        }
        else if (capacity == 6){
            result.add(new GodFather());
            result.add(new DoctorLector());
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Detector());
        }
        else if (capacity == 5){
            result.add(new GodFather());
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Detector());
        }

        Collections.shuffle(result);
        return result;
    }


    public static ArrayList<Role> get(int capacity) {
        return initialize(capacity);
    }
}
