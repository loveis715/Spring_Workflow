package com.vmware.workflow.core.activity.segment;

import org.springframework.integration.MessageChannel;

import com.vmware.workflow.core.base.Activity;

abstract class SpringIntegrationSegment<T extends Activity> {
    protected MessageChannel inputChannel;
    protected MessageChannel outputChannel;
    protected final T activity;

    SpringIntegrationSegment(T activity) {
        this.activity = activity;
    }

    MessageChannel getInputChannel() {
        return this.inputChannel;
    }

    void setInputChannel(MessageChannel inputChannel) {
        this.inputChannel = inputChannel;
    }

    MessageChannel getOutputChannel() {
        return this.outputChannel;
    }

    void setOutputChannel(MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    abstract void expandAction(SpringIntegrationContext context) throws Throwable;
}