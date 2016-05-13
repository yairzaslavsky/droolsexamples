
package com.example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import com.aconex.rules.Engine;
import com.sample.Mail;
import com.sample.Organizations;
import com.sample.Roles;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class RemovalOfRecepientsOfOrgIfNotMembersOfRoleTest {

    private Mail mail;
    private Engine engine;
    private WorkingMemory session;

    @BeforeClass
    public static void beforeClass() {
        Organizations.instance().addRecepient("ACME", "Yair");
        Organizations.instance().addRecepient("ACME", "Yanhui");
        Organizations.instance().addRecepient("PURPLE", "Dmitriy");

    }

    @Before
    public void setup() throws IOException, DroolsParserException {
        mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
        engine = new Engine();
        session = engine.createRulesSessionFromResource("/remove_acme_org_recepients_if_not_admins.drl");
    }

    @Test
    public void shouldRemoveNonAdminRecepientsOfAcme() throws IOException, DroolsParserException {
        Roles.instance().addRoleForUser("Yair", "Admin");

        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size(), is(2));
        assertThat(mail.getRecepients().contains("Dmitriy"), is(true));
        assertThat(mail.getRecepients().contains("Yair"), is(true));


    }

    @Test
    public void shouldRemoveRecepientsOfAcme() throws IOException, DroolsParserException {

        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size(), is(1));
    }





}