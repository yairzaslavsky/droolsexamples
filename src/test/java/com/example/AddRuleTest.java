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
import org.junit.BeforeClass;
import org.junit.Test;

public class AddRuleTest {

    private Mail mail;
    private Engine engine;

    @BeforeClass
    public static void before() {
        instance().addRecepient("ACME", "Yair");
        instance().addRecepient("ACME", "Yanhui");
        instance().addRecepient("PURPLE", "Dmitriy");
    }

    @Test
    public void shoulRemoveAllUsersAfterTwoRulesAreAdded() throws IOException, DroolsParserException {
        createMailWithRecepients();

        engine = new Engine();
        WorkingMemory session = engine.createRulesSessionFromResource("/remove_acme_org_recepients.drl");
        session = engine.addRuleToSessionFromResource(session, "/remove_purple_org_recepients_if_mailtype_is_rfi.drl");
        mail.setMailType(MailType.RFI);
        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size() , is(0));
    }

    private void createMailWithRecepients() {
        mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
    }


}
