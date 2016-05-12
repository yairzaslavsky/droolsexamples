
package com.example;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.sample.Example;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import org.drools.rule.Package;
import org.junit.Test;


public class DroolsTest {

    @Test
    public void shouldNotFireHelloWorldIfAgeIsNotMinimal() throws IOException, DroolsParserException {
        RuleBase ruleBase = initialiseDrools();
        WorkingMemory workingMemory = initializeMessageObjects(ruleBase ,"Hello", 6);
        int expectedNumberOfRulesFired = 0;

        int actualNumberOfRulesFired = workingMemory.fireAllRules();

        assertThat(actualNumberOfRulesFired, is(expectedNumberOfRulesFired));
    }

    @Test
    public void shouldFireHelloWorldIfAgeIsAboveMinimal() throws IOException, DroolsParserException {
        RuleBase ruleBase = initialiseDrools();
        WorkingMemory workingMemory = initializeMessageObjects(ruleBase, "Hello", 19);
        int expectedNumberOfRulesFired = 1;

        int actualNumberOfRulesFired = workingMemory.fireAllRules();

        assertThat(actualNumberOfRulesFired, is(expectedNumberOfRulesFired));
    }


    private RuleBase initialiseDrools() throws IOException, DroolsParserException {
        PackageBuilder packageBuilder = readRuleFiles();
        return addRulesToWorkingMemory(packageBuilder);
    }

    private PackageBuilder readRuleFiles() throws DroolsParserException, IOException {
        PackageBuilder packageBuilder = new PackageBuilder();

        File f = new File("/sample.drl");
        System.out.println(f.exists());

        String ruleFile = "/sample.drl";
        Reader reader = getRuleFileAsReader(ruleFile);
        packageBuilder.addPackageFromDrl(reader);

        assertNoRuleErrors(packageBuilder);

        return packageBuilder;
    }

    private Reader getRuleFileAsReader(String ruleFile) {
        InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);

        return new InputStreamReader(resourceAsStream);
    }

    private RuleBase addRulesToWorkingMemory(PackageBuilder packageBuilder) {
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        Package rulesPackage = packageBuilder.getPackage();
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

    private WorkingMemory initializeMessageObjects(RuleBase ruleBase, String msg, int age) {
        WorkingMemory workingMemory = ruleBase.newStatefulSession();

        createHelloWorld(workingMemory, msg, age);

        return workingMemory;
    }

    private void createHelloWorld(WorkingMemory workingMemory, String msg, int age) {
        Example.Message helloMessage = new Example.Message();
        helloMessage.setMessage(msg);
        helloMessage.setAge(age);
        workingMemory.insert(helloMessage);
    }
}