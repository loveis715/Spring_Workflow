package com.vmware.workflow.core.activity;

import com.vmware.workflow.core.base.ActivityContainer;

public class WhileActivity extends ActivityContainer {

    private Object conditionBean;
    private String conditionMethod;

    public Object getConditionBean() {
        return this.conditionBean;
    }

    public void setConditionBean(Object conditionBean) {
        this.conditionBean = conditionBean;
    }

    public String getConditionMethod() {
        return this.conditionMethod;
    }

    public void setConditionMethod(String conditionMethod) {
        this.conditionMethod = conditionMethod;
    }
}