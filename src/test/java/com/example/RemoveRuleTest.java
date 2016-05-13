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

public class RemoveRuleTest {

    private Mail mail;
    private Engine engine;

    @BeforeClass
    public static void before() {
        instance().addRecepient("ACME", "Yair");
        instance().addRecepient("ACME", "Yanhui");
        instance().addRecepient("PURPLE", "Dmitriy");
    }

    @Test
    public void foo() throws IOException, DroolsParserException {
        mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
        engine = new Engine();
        WorkingMemory session = engine.createRulesSessionFromResource("/remove_acme_org_recepients.drl");
        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size() < 3, is(true));

        engine.removeRuleFromSession(session, "com.sample", "Remove all ACME");
        mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
        engine = new Engine();
        session.fireAllRules();
        assertThat(mail.getRecepients().size() == 3, is(true));

    }



}
