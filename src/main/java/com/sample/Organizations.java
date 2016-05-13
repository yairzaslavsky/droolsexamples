package com.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Organizations {

    private static final Organizations instance = new Organizations();

    private HashMap<String, List<String>> organizations = new HashMap<>();

    public static Organizations instance() {
        return instance;
    }

    public void addRecepient(String orgName, String recepient) {
        List<String> recepients = organizations.get(orgName);
        if (recepients  == null) {
            recepients = new ArrayList<>();
        }
        organizations.put(orgName, recepients);
        recepients.add(recepient);
    }

    public boolean isMemberOfOrg(String orgName, String recepient) {
        List<String> recepients = organizations.get(orgName);
        if (recepients == null) {
            return false;
        }
        return recepients.contains(recepient);
    }

}
