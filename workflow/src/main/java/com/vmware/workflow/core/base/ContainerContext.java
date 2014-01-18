package com.vmware.workflow.core.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.MessageChannel;

// This is an internal class to be used during construction workflow. During
// that process, it is garenteed to be one single thread.
class ContainerContext {
    Map<Activity, MessageChannel> inputChannels = new HashMap<Activity, MessageChannel>();
    Map<Activity, MessageChannel> outputChannels = new HashMap<Activity, MessageChannel>();

    MessageChannel getInputChannel(Activity activity) {
        return inputChannels.get(activity);
    }

    void setInputChannel(Activity activity, MessageChannel channel) {
        inputChannels.put(activity, channel);
    }

    MessageChannel getOutputChannel(Activity activity) {
        return outputChannels.get(activity);
    }

    void setOutputChannel(Activity activity, MessageChannel channel) {
        outputChannels.put(activity, channel);
    }
}
