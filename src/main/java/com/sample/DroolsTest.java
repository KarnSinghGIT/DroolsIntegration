package com.sample;

import org.jboss.logging.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
//import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.StatelessKieSession;

import java.io.InputStream;
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;
import java.util.Scanner;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.command.runtime.BatchExecutionCommandImpl; 
import org.drools.core.command.runtime.rule.FireAllRulesCommand; 
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.drools.core.io.impl.UrlResource;
import org.kie.server.api.marshalling.MarshallingFormat; 
import org.kie.server.api.model.KieContainerResource; 
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.definition.ProcessDefinition; 
import org.kie.server.api.model.instance.NodeInstance; 
import org.kie.server.api.model.instance.ProcessInstance; 
import org.kie.server.api.model.instance.TaskSummary; 
import org.kie.server.client.KieServicesClient; 
import org.kie.server.client.KieServicesConfiguration; 
import org.kie.server.client.KieServicesFactory; 
import org.kie.server.client.ProcessServicesClient; 
import org.kie.server.client.QueryServicesClient; 
import org.kie.server.client.RuleServicesClient; 
import org.kie.server.client.UserTaskServicesClient; 
/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	     public static final void main(String[] args) 
    {
        try {
        	// works even without -SNAPSHOT versions
            String url = "http://ec2-52-53-255-228.us-west-1.compute.amazonaws.com:8180/kie-wb/maven2/org/bcd/mortgages/0.0.1/mortgages-0.0.1.jar";
            
            KieServices ks = KieServices.Factory.get();
            KieRepository kr = ks.getRepository();
            UrlResource urlResource = (UrlResource) ks.getResources()
                    .newUrlResource(url);
            urlResource.setUsername("workbench");
            urlResource.setPassword("workbench!");
            urlResource.setBasicAuthentication("enabled");
            InputStream is = urlResource.getInputStream();
            KieModule kModule = kr.addKieModule(ks.getResources()
                    .newInputStreamResource(is));
        //    org.kie.api.builder.ReleaseId releaseId1 =kModule.getReleaseId();
        //    org.kie.api.builder.ReleaseId releaseId = ks.newReleaseId("org.bcd", "mortgages", "0.0.1" );        
            KieContainer kieContainer = ks.newKieContainer(kModule.getReleaseId());

           // kieContainer.newStatelessKieSession();

            // check every 5 seconds if there is a new version at the URL
         //   KieScanner kieScanner = ks.newKieScanner(kieContainer);
          //  kieScanner.start(5000L);
            // alternatively:
            // kieScanner.scanNow();
            KieSession kieSession = kieContainer.newKieSession("DEFAULT");          
       
            
            LoanApplication loanApplication= new LoanApplication();
            loanApplication.setAmount(900);
            loanApplication.setApproved(false);
            System.out.println("Before Execute:" + loanApplication.getApproved());
            kieSession.insert(loanApplication);
            kieSession.fireAllRules();
            System.out.println("After Execute:" + loanApplication.getApproved());
            
            System.out.println("Press enter in order to run the test again....");
            
           
	       } catch (Throwable t) {
            t.printStackTrace();
        }
    }
     static void runRule(KieContainer kieKontainer) {
        StatelessKieSession kSession = kieKontainer.newStatelessKieSession("testSession");
        kSession.setGlobal("out", System.out);
        kSession.execute("testRuleAgain");
    }
 
     public static class LoanApplication extends java.lang.Object implements java.io.Serializable
     {

        static final long serialVersionUID = 1L;

        @org.kie.api.definition.type.Position(0)
        private java.lang.Integer amount;

        @org.kie.api.definition.type.Position(1)
        private java.lang.Boolean approved;

        public LoanApplication()
        {
        }

        public java.lang.Integer getAmount()
        {
           return this.amount;
        }

        public void setAmount(java.lang.Integer amount)
        {
           this.amount = amount;
        }

        public java.lang.Boolean getApproved()
        {
           return this.approved;
        }

        public void setApproved(java.lang.Boolean approved)
        {
           this.approved = approved;
        }

        public LoanApplication(java.lang.Integer amount, java.lang.Boolean approved)
        {
           this.amount = amount;
           this.approved = approved;
        }
     }
     
     public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }
    
}
