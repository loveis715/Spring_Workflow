package com.vmware.workflow.core.activity.segment;

import java.io.InvalidClassException;
import java.util.HashMap;
import java.util.Map;

import com.vmware.workflow.core.activity.BusinessActivity;
import com.vmware.workflow.core.activity.WhileActivity;
import com.vmware.workflow.core.base.Activity;
import com.vmware.workflow.core.workflow.WorkflowFactoryBean;

class SpringIntegrationContext {
    Map<Activity, SpringIntegrationSegment<?>> segmentMap = new HashMap<Activity, SpringIntegrationSegment<?>>();

    SpringIntegrationSegment<?> getSegment(Activity activity) throws Throwable {
        SpringIntegrationSegment<?> segment = segmentMap.get(activity);
        if (segment == null) {
            segment = createSegment(activity);
            segmentMap.put(activity, segment);
        }
        return segment;
    }

    SpringIntegrationSegment<?> createSegment(Activity activity) throws Throwable {
        // TODO: Use reflection to create segment instances. In that case, we can
        // just register a class and remove these if elses
        if (activity.getClass().equals(WorkflowFactoryBean.class)) {
            return new WorkflowSegment((WorkflowFactoryBean)activity);
        } else if (activity.getClass().equals(BusinessActivity.class)) {
            return new BusinessActivitySegment((BusinessActivity)activity);
        } else if (activity.getClass().equals(WhileActivity.class)) {
            return new WhileActivitySegment((WhileActivity)activity);
        } else {
            throw new InvalidClassException("Unsupported activity type"); // TODO: Semantically incorrect
        }
    }
}