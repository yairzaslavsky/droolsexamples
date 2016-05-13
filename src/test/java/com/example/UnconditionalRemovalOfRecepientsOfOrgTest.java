
package com.example;

import static com.sample.Organizations.instance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import com.aconex.rules.Engine;
import com.sample.Mail;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.junit.BeforeClass;
import org.junit.Test;


public class UnconditionalRemovalOfRecepientsOfOrgTest {


    @BeforeClass
    public static void before() {
        instance().addRecepient("ACME", "Yair");
        instance().addRecepient("ACME", "Yanhui");
        instance().addRecepient("PURPLE", "Dmitriy");

    }

    @Test
    public void shouldRemoveRecepientsOfAcme() throws IOException, DroolsParserException {
        Engine engine = new Engine();
        WorkingMemory session = engine.createRulesSessionFromResource("/remove_acme_org_recepients.drl");
        Mail mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size(), is(1));
        assertThat(mail.getRecepients().get(0), is("Dmitriy"));
    }

}