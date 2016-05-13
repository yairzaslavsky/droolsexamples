package com.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Roles {

    private static final Roles instance = new Roles();

    private HashMap<String, List<String>> roles = new HashMap<>();

    public static Roles instance() {
        return instance;
    }

    public void addRoleForUser(String userName, String roleName) {
        List<String> roleNames = roles.get(userName);
        if (roleNames  == null) {
            roleNames = new ArrayList<>();
        }
        roles.put(userName, roleNames);
        roleNames.add(roleName);
    }

    public boolean isOfRole(String userName, String roleName) {
        List<String> roleNames = roles.get(userName);
        if (roleNames == null) {
            return false;
        }

        return roleNames.contains(roleName);

    }

}
