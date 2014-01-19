package com.vmware.workflow.poc.samples.workflow;

public class TestService {
    private int counter = 5;

    public String sayHello(String name) {
        return "Hello " + name;
    }

    public boolean count() {
        counter--;
        return counter > 0;
    }
}
