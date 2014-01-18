package com.vmware.workflow.core.base;

import java.util.Iterator;
import java.util.List;

import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;

public class ActivityContainer {
    protected ContainerContext containerContext;

    protected MessageChannel requestChannel;
    protected MessageChannel replyChannel;

    // Override this method to perform your own logic
    protected void connect(List<Activity> activities) {
        if (containerContext == null) {
            containerContext = new ContainerContext();
        }

        requestChannel = new DirectChannel();
        replyChannel = new QueueChannel();
        MessageChannel previousChannel = requestChannel;
        Iterator<Activity> itActivity = activities.iterator();
        while (itActivity.hasNext()) {
            Activity activity = itActivity.next();

            containerContext.setInputChannel(activity, previousChannel);
            if (!itActivity.hasNext()) {
                containerContext.setOutputChannel(activity, replyChannel);
            }
            activity.setContainerContext(containerContext);
            activity.init();
            previousChannel = containerContext.getOutputChannel(activity);
        }
    }
}