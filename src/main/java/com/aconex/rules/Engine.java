package com.aconex.rules;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import org.drools.rule.Rule;

public class Engine {


    public WorkingMemory createRulesSession(InputStream rulesDataStream) throws IOException, DroolsParserException {

        InputStreamReader reader = new InputStreamReader(rulesDataStream);
        PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl(reader);
        RuleBase ruleBase = addRulesToWorkingMemory(builder);
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        return workingMemory;
    }

    public WorkingMemory createRulesSessionFromResource(String resourceName) throws IOException, DroolsParserException {
        return createRulesSession(this.getClass().getResourceAsStream(resourceName));

    }

    public <T> void attachObjectToSession(WorkingMemory session, T object) {
        session.insert(object);
    }

    public WorkingMemory removeRuleFromSession(WorkingMemory session, String pkgName, String ruleName) {
        session.getRuleBase().removeRule(pkgName, ruleName);
        return session.getRuleBase().newStatefulSession();
    }

    public WorkingMemory addRuleToSessionFromResource(WorkingMemory session, String resourceName, String pkgName, String ruleName) throws IOException, DroolsParserException {
        return addRuleToSession(session, pkgName, ruleName, this.getClass().getResourceAsStream(resourceName));
    }

    public WorkingMemory addRuleToSession(WorkingMemory session, String pkgName, String ruleName, InputStream rulesDataStream) throws IOException, DroolsParserException {

        PackageBuilder builder = new PackageBuilder();
        InputStreamReader reader = new InputStreamReader(rulesDataStream);
        builder.addPackageFromDrl(reader);
        Rule rule = builder.getPackage().getRule(ruleName);
        session.getRuleBase().getPackage(pkgName).addRule(rule);
        return session.getRuleBase().newStatefulSession();
    }


    private Reader getRuleFileAsReader(String ruleFile) {
        InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);

        return new InputStreamReader(resourceAsStream);
    }

    private RuleBase addRulesToWorkingMemory(PackageBuilder packageBuilder) {
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        org.drools.rule.Package rulesPackage = packageBuilder.getPackage();
        ruleBase.addPackage(rulesPackage);

        return ruleBase;
    }


    private void assertNoRuleErrors(PackageBuilder packageBuilder) {
        PackageBuilderErrors errors = packageBuilder.getErrors();

        if (errors.getErrors().length > 0) {
            StringBuilder errorMessages = new StringBuilder();
            errorMessages.append("Found errors in package builder\n");
            for (int i = 0; i < errors.getErrors().length; i++) {
                DroolsError errorMessage = errors.getErrors()[i];
                errorMessages.append(errorMessage);
                errorMessages.append("\n");
            }
            errorMessages.append("Could not parse knowledge");

            throw new IllegalArgumentException(errorMessages.toString());
        }
    }

}
