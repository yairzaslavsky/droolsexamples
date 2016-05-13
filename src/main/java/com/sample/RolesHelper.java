package com.sample;

public class RolesHelper {

    public static boolean isOfRole (String userName, String role) {
        return Roles.instance().isOfRole(userName, role);
    }
}
