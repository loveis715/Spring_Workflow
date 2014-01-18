package com.vmware.workflow.core.base;

import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.DirectChannel;

public abstract class Activity {
    private ContainerContext containerContext;

    ContainerContext getContainerContext() {
        return this.containerContext;
    }

    void setContainerContext(ContainerContext containerContext) {
        this.containerContext = containerContext;
    }

    public void init() {
        MessageChannel inputChannel = containerContext.getInputChannel(this);
        if (inputChannel == null) {
            inputChannel = new DirectChannel();
            containerContext.setInputChannel(this, inputChannel);
        }

        MessageChannel outputChannel = containerContext.getOutputChannel(this);
        if (outputChannel == null) {
            outputChannel = new DirectChannel();
            containerContext.setOutputChannel(this, outputChannel);
        }

        try {
            // Initialize service activator for business logic
            constructEndpoint(inputChannel, outputChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void constructEndpoint(MessageChannel inputChannel, MessageChannel outputChannel) throws Exception;
}