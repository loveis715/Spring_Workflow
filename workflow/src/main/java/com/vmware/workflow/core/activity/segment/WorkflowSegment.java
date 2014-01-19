package com.vmware.workflow.core.activity.segment;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import com.vmware.workflow.core.workflow.WorkflowFactoryBean;

class WorkflowSegment extends ContainerActivitySegment<WorkflowFactoryBean>{
    private GatewayProxyFactoryBean gatewayFactory;

    WorkflowSegment(WorkflowFactoryBean workflow) {
        super(workflow);
    }

    Object invoke(MethodInvocation invocation, SpringIntegrationContext context) throws Throwable {
        if (gatewayFactory == null) {
            expandAction(context);
        }
        return gatewayFactory.invoke(invocation);
    }

    @Override
    void expandAction(SpringIntegrationContext context) throws Throwable {
        Class<?> workflowInterface = activity.getWorkflowInterface();
        inputChannel = new DirectChannel();
        childrenInputChannel = inputChannel;
        outputChannel = new QueueChannel();
        childrenOutputChannel = outputChannel;

        gatewayFactory = new GatewayProxyFactoryBean();
        gatewayFactory.setDefaultRequestChannel(inputChannel);
        gatewayFactory.setDefaultReplyChannel(outputChannel);
        gatewayFactory.setServiceInterface(workflowInterface);

        super.expandAction(context);
    }
}