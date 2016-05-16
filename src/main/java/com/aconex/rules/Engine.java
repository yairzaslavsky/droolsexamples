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
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class Engine {


    public KieSession createRulesSession(InputStream rulesDataStream)  {

        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem fileSystem = kieServices.newKieFileSystem();
        writeInputStreamToFileSystem(rulesDataStream, kieServices, fileSystem);
        KieBuilder builder = kieServices.newKieBuilder(fileSystem);
        builder.buildAll();

        if (builder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Build time Errors: " + builder.getResults().toString());
        }

        KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieSession session = kContainer.newKieSession();
        return session;
    }

    public KieSession createRulesSessionFromResource(String resourceName) throws IOException {
        return createRulesSession(this.getClass().getResourceAsStream(resourceName));

    }

    public <T> void attachObjectToSession(KieSession session, T object) {
        session.insert(object);
    }

    public KieSession removeRuleFromSession(KieSession session, String pkgName, String ruleName) {
        session.getKieBase().removeRule(pkgName, ruleName);
        return session.getKieBase().newKieSession();
    }

    public WorkingMemory addRuleToSessionFromResource(WorkingMemory session, String resourceName)  {
        return addRuleToSession(session, this.getClass().getResourceAsStream(resourceName));
    }

    public WorkingMemory addRuleToSession(KieSession session,  InputStream rulesDataStream)  {

        KieBuilder builder = createBuilder();
        KieFileSystem fileSystem =  KieServices.Factory.get().newKieFileSystem();

        builder.

        PackageBuilder builder = new PackageBuilder();
        InputStreamReader reader = new InputStreamReader(rulesDataStream);
        builder.addPackageFromDrl(reader);
        for (org.drools.rule.Package currentPkg :session.getRuleBase().getPackages()) {
            builder.addPackage(currentPkg);
        }
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        for (org.drools.rule.Package currentPackage: builder.getPackages()) {
            ruleBase.addPackage(currentPackage);
        }
        return ruleBase.newStatefulSession();
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

    private void writeInputStreamToFileSystem(InputStream rulesDataStream, KieServices kieServices, KieFileSystem fileSystem) {
        Resource resource = kieServices.getResources().newInputStreamResource(rulesDataStream).setResourceType(ResourceType.DRL);
        fileSystem.write(resource);
    }


    private KieBuilder createBuilder() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem fileSystem = kieServices.newKieFileSystem();
        return kieServices.newKieBuilder(fileSystem);
    }

}
