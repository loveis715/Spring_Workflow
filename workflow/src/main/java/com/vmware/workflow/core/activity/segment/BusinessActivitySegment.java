package com.vmware.workflow.core.activity.segment;

import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.ServiceActivatingHandler;

import com.vmware.workflow.core.activity.BusinessActivity;

class BusinessActivitySegment extends SpringIntegrationSegment<BusinessActivity> {
    BusinessActivitySegment(BusinessActivity activity) {
        super(activity);
    }

    @Override
    void expandAction(SpringIntegrationContext context) {
        Object targetObject = activity.getTargetObject();
        String targetMethodName = activity.getTargetMethodName();
        ServiceActivatingHandler handler = new ServiceActivatingHandler(targetObject, targetMethodName);
        handler.setOutputChannel(outputChannel);

        EventDrivenConsumer consumer = new EventDrivenConsumer((SubscribableChannel) inputChannel, handler);
        consumer.start();
    }
}