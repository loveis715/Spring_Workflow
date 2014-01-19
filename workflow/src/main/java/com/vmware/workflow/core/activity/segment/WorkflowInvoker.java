package com.vmware.workflow.core.activity.segment;

import org.aopalliance.intercept.MethodInvocation;

import com.vmware.workflow.core.workflow.WorkflowFactoryBean;

public class WorkflowInvoker {
    private final WorkflowFactoryBean workflowBean;

    public WorkflowInvoker(WorkflowFactoryBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    // TODO: Revisit to decide whether we need to include workflow segment in context
    public Object invoke(MethodInvocation invocation) throws Throwable {
        SpringIntegrationContext context = new SpringIntegrationContext();
        WorkflowSegment workflowSegment = (WorkflowSegment)context.getSegment(workflowBean);
        workflowSegment.expandAction(context);
        return workflowSegment.invoke(invocation, context);
    }
}