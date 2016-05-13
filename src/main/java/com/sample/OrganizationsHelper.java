package com.sample;

public class OrganizationsHelper {

    public static boolean isMemberOfOrg (String orgName, String userName) {
        return Organizations.instance().isMemberOfOrg(orgName, userName);
    }
}
