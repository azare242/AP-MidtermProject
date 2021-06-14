package zare.alireza.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RolesList {



    private static ArrayList<Role> initialize(int capacity) {
        ArrayList<Role> result = new ArrayList<>();

        if (capacity >= 10) {
            result.add(new GodFather());
            result.add(new DoctorLector());
            int simpleMafias = capacity / 3 - 2;
            for (int i = 0; i < simpleMafias; ++i) {
                result.add(new SimpleMafia());
            }
            result.add(new Mayor());
            result.add(new Physician());
            result.add(new Psychologist());
            result.add(new Professional());
            result.add(new IronSide());
            result.add(new Detector());

            int simpleCitizens = capacity - (capacity / 3) - 6;
            for (int i = 0; i < simpleCitizens; i++) {
                result.add(new SimpleCitizen());
            }
        }
        return result;
    }

    public static ArrayList<Role> get(int capacity) {
        return initialize(capacity);
    }
}
