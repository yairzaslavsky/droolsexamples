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

    public void removeRuleFromSession(WorkingMemory session, String javaPkgName, String ruleName) {
        session.getRuleBase().removeRule(javaPkgName, ruleName);
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
