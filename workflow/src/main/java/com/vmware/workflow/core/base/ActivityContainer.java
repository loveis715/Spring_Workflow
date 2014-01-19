package com.vmware.workflow.core.base;

import java.util.List;

public class ActivityContainer extends Activity {
    public List<Activity> activities;

    public List<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}