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
import org.drools.rule.Package;
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

        //Activate first rule, removal of 2 acme users
        engine = new Engine();
        WorkingMemory session = engine.createRulesSessionFromResource("/remove_acme_org_recepients.drl");
        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        assertThat(mail.getRecepients().size() < 3, is(true));

        //Activate first rule, removal of no user (mail type is not RFI)

        createMailWithRecepients();
        session = engine.addRuleToSessionFromResource(session, "/remove_purple_org_recepients_if_mailtype_is_rfi.drl", "com.sample", "Remove all PURPLE if mailtype is RFI");
        session.fireAllRules();
        assertThat(mail.getRecepients().size() ,is(3));

        createMailWithRecepients();
        //Activate all rules , no recepients should exist
        Package pkg = session.getRuleBase().getPackages()[0];
        System.out.println("rules number " + pkg.getRules().length);
        mail.setMailType(MailType.RFI);
        engine.attachObjectToSession(session, mail);
        session.fireAllRules();
        System.out.println(mail.getRecepients().get(0));
        assertThat(mail.getRecepients().size() ,is(0));



    }

    private void createMailWithRecepients() {
        mail = new Mail();
        mail.addRecepient("Yair");
        mail.addRecepient("Yanhui");
        mail.addRecepient("Dmitriy");
    }


}
