package zare.alireza.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RolesList {

    private ArrayList<Role> roles;

    private static RolesList INSTANCE = null;

    private RolesList() {
        roles = initialize();
        Collections.shuffle(roles);
    }

    private ArrayList<Role> initialize() {
        Role[] roles = new Role[]{
                new SimpleMafia(), new Mayor(),
                new Physician(),new SimpleCitizen()
                ,new GodFather(),new IronSide(),
                new Professional(),new Psychologist(),
                new Detector(),new DoctorLector()
        };
        return new ArrayList<>(Arrays.asList(roles));
    }

    public static ArrayList<Role> get(){
        if (INSTANCE == null) {
            INSTANCE = new RolesList();
        }
        return INSTANCE.roles;
    }
}
