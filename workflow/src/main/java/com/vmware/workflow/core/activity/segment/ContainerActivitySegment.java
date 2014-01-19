package com.vmware.workflow.core.activity.segment;

import java.util.Iterator;
import java.util.List;

import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.DirectChannel;

import com.vmware.workflow.core.base.Activity;
import com.vmware.workflow.core.base.ActivityContainer;

public class ContainerActivitySegment<T extends ActivityContainer> extends SpringIntegrationSegment<T> {
    MessageChannel childrenInputChannel;
    MessageChannel childrenOutputChannel;

    ContainerActivitySegment(T activity) {
        super(activity);
    }

    MessageChannel getChildrenInputChannel() {
        return this.childrenInputChannel;
    }

    void setChildrenInputChannel(MessageChannel childrenInputChannel) {
        this.childrenInputChannel = childrenInputChannel;
    }

    MessageChannel getChildrenOutputChannel() {
        return this.childrenOutputChannel;
    }

    void setChildrenOutputChannel(MessageChannel childrenOutputChannel) {
        this.childrenOutputChannel = childrenOutputChannel;
    }

    @Override
    void expandAction(SpringIntegrationContext context) throws Throwable {
        MessageChannel inputChannel = this.childrenInputChannel;
        MessageChannel outputChannel = null;

        List<Activity> activities = activity.getActivities();
        Iterator<Activity> itActivity = activities.iterator();
        while (itActivity.hasNext()) {
            Activity activity = itActivity.next();
            if (itActivity.hasNext()) {
                outputChannel = new DirectChannel();
            } else {
                outputChannel = childrenOutputChannel;
            }

            SpringIntegrationSegment<?> segment = context.getSegment(activity);
            segment.setInputChannel(inputChannel);
            segment.setOutputChannel(outputChannel);
            segment.expandAction(context);

            inputChannel = outputChannel;
        }
    }
}