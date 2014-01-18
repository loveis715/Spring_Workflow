package com.vmware.workflow.core.activities;

import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.ServiceActivatingHandler;

import com.vmware.workflow.core.base.Activity;

public class BusinessActivity extends Activity {

    private Object targetObject;

    private String targetMethodName;

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethodName(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }

    @Override
    protected void constructEndpoint(MessageChannel inputChannel, MessageChannel outputChannel) throws Exception {
        ServiceActivatingHandler handler = new ServiceActivatingHandler(targetObject, targetMethodName);
        handler.setOutputChannel(outputChannel);

        EventDrivenConsumer consumer = new EventDrivenConsumer((SubscribableChannel) inputChannel, handler);
        consumer.start();
    }
}