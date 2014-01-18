package com.vmware.workflow.core.workflow;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.vmware.workflow.core.base.Activity;
import com.vmware.workflow.core.base.ActivityContainer;

// FIXME: Workflow should also be an Activity
// TODO: Research why we use a factory bean instead of normal factory?
// TODO: Research base interfaces for beans, e.g. BeanNameAware,
// NamedComponent, BeanFactoryAware, InitializingBean, SmartLifecycle,
// TrackableComponent, FactoryBean<Object>, MethodInterceptor, BeanClassLoaderAware, SmartFactoryBean
public class WorkflowFactoryBean extends ActivityContainer implements MethodInterceptor, InitializingBean, FactoryBean<Object>, BeanClassLoaderAware {

    private volatile Class<?> workflowInterface;

    private volatile Object workflowProxy;
    private volatile GatewayProxyFactoryBean gatewayFactory;
    private List<Activity> activities;

    private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private volatile boolean initialized;

    private final Object initializationMonitor = new Object();

    public Class<?> getWorkflowInterface() {
        return this.workflowInterface;
    }

    public void setWorkflowInterface(Class<?> workflowInterface) {
        this.workflowInterface = workflowInterface;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public final void afterPropertiesSet() {
        try {
            this.onInit();
        }
        catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new BeanInitializationException("failed to initialize", e);
        }
    }

    // TODO: Seems this is used to implement the feature for lazy loading?
    // That is, we will create the bean instance when we really need it?
    protected final void onInit() {
        synchronized (this.initializationMonitor) {
            if (this.initialized) {
                return;
            }
            Class<?> proxyInterface = this.determineWorkflowInterface();
            this.workflowProxy = new ProxyFactory(proxyInterface, this).getProxy(this.beanClassLoader);
            this.gatewayFactory = constructNestedGateway();
            this.initialized = true;
        }
    }

    private GatewayProxyFactoryBean constructNestedGateway() {
        connect(activities);

        GatewayProxyFactoryBean gatewayFactory = new GatewayProxyFactoryBean();
        gatewayFactory.setDefaultRequestChannel(requestChannel);
        gatewayFactory.setDefaultReplyChannel(replyChannel);
        gatewayFactory.setServiceInterface(workflowInterface);
        return gatewayFactory;
    }

    private Class<?> determineWorkflowInterface() {
        if (this.workflowInterface == null) {
            this.workflowInterface = DefaultWorkflowInterface.class;
        }
        return this.workflowInterface;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public Object getObject() throws Exception {
        if (this.workflowProxy == null) {
            this.onInit();
            Assert.notNull(this.workflowProxy, "failed to initialize proxy");
        }
        return this.workflowProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.workflowInterface != null ? this.workflowInterface : null);
    }

    @Override
    public boolean isSingleton() {
        // FIXME: Need investigate
        return true;
    }

    // FIXME: We have only support invoking the first method defined in workflow
    // interface.
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (this.gatewayFactory == null) {
            this.onInit();
        }
        return this.gatewayFactory.invoke(invocation);
    }
}
