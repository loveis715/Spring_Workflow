package com.vmware.workflow.core.activity;

import com.vmware.workflow.core.base.Activity;

public class BusinessActivity extends Activity {

    private Object targetObject;

    private String targetMethodName;

    public Object getTargetObject() {
        return this.targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public String getTargetMethodName() {
        return this.targetMethodName;
    }

    public void setTargetMethodName(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }
}