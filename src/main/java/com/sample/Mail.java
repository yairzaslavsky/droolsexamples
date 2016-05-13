package com.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mail {

    private List<String> recepients = new ArrayList<String>();
    private MailType mailType = MailType.GENERAL;

    public List<String> getRecepients() {
        return recepients;
    }

    public void addRecepient(String recepient) {
        this.recepients.add(recepient);
    }

    public boolean containsRecepient(String recepient) {
        return recepients.contains(recepient);
    }

    public void removeRecepient(String recepient) {
        recepients.remove(recepient);
    }

    public boolean isRecepientAndMemberOfOrg(String orgName, String recepient) {
        return containsRecepient(recepient) && Organizations.instance().isMemberOfOrg(orgName, recepient);
    }

    public boolean containsRecepientsFromOrg(String orgName) {
        for (String recepient : recepients) {
             if (Organizations.instance().isMemberOfOrg(orgName, recepient)) {
                 return true;
             }
        }

        return false;
    }


    public void removeRecpeientsForOrg(String orgName) {
        System.out.println("removing!");
        Iterator<String> iterator = recepients.iterator();
        while (iterator.hasNext()) {
            String recepient = iterator.next();
            if (Organizations.instance().isMemberOfOrg(orgName, recepient)) {
                iterator.remove();
            }
        }
    }

    public MailType getMailType() {
        return mailType;
    }

    public void setMailType(MailType mailType) {
        this.mailType = mailType;
    }
}
