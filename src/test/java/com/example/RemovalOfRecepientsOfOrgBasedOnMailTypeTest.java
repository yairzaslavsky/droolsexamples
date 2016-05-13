
package com.example;

import static com.sample.Organizations.instance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import com.aconex.rules.Engine;
import com.sample.Mail;
import com.sample.MailType;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class RemovalOfRecepientsOfOrgBasedOnMailTypeTest {

    private Mail mail;
    private Engine engine;
    private WorkingMemory session;

    @BeforeClass
    public static void beforeClass() {
        instance().addRecepient("ACME", "Yair");
        instance().addRecepient("ACME", "Yanhui");
        instance().addRecepient("PURPLE", "Dmitriy");

    }

    @Before
    public void setup() throws IOException, DroolsParserException {
        mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
        engine = new Engine();
        session = engine.createRulesSessionFromResource("/remove_acme_org_recepients_if_mailtype_is_rfi.drl");
    }

    @Test
    public void shouldRemoveRecepientsOfAcme() throws IOException, DroolsParserException {
        mail.setMailType(MailType.RFI);


        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size(), is(1));
        assertThat(mail.getRecepients().get(0), is("Dmitriy"));
    }

    @Test
    public void shouldNotRemoveRecepientsOfAcme() throws IOException, DroolsParserException {

        mail.setMailType(MailType.GENERAL);
        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size(), is(3));
    }





}