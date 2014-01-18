package com.vmware.workflow.poc.samples;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vmware.workflow.poc.samples.workflow.WorkflowInterface;

public class Sample {
    private static Logger logger = Logger.getLogger(Sample.class);

    public static void main(String[] args) {
        invokeSpringIntegration();
        invokeWorkflow();
        System.exit(0); // See http://bugs.sun.com/view_bug.do?bug_id=6476706
    }

    private static void invokeSpringIntegration() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/helloWorldDemo.xml", Sample.class);
        /*MessageChannel inputChannel = context.getBean("inputChannel", MessageChannel.class);
        PollableChannel outputChannel = context.getBean("outputChannel", PollableChannel.class);
        inputChannel.send(new GenericMessage<String>("World"));
        logger.info("==> HelloWorldDemo: " + outputChannel.receive(0).getPayload());*/
        //WorkflowInterface workflow = context.getBean("gateway", WorkflowInterface.class);
        //workflow.Invoke("Test");
    }

    private static void invokeWorkflow() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/workflow/Sample.xml", Sample.class);
        WorkflowInterface workflowInterface = context.getBean(WorkflowInterface.class);
        workflowInterface.Invoke("World!");
    }
}