package com.vmware.workflow.core.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

// TODO: What are these overrides used for?
public class WorkflowNamespaceHandler implements NamespaceHandler {

    // TODO: Why we need this delegate? Seems something interesting
    private final NamespaceHandlerDelegate delegate = new NamespaceHandlerDelegate();

    // This function will be called when we want to parse workflow configuration elements
    @Override
    public void init() {
        registerBeanDefinitionParser(NamespaceConstants.WORKFLOW, new WorkflowParser());
    }

    @Override
    public final BeanDefinition parse(Element element, ParserContext parserContext) {
        return this.delegate.parse(element, parserContext);
    }

    @Override
    public final BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
        return this.delegate.decorate(source, definition, parserContext);
    }

    protected final void registerBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator decorator) {
        this.delegate.doRegisterBeanDefinitionDecorator(elementName, decorator);
    }

    protected final void registerBeanDefinitionDecoratorForAttribute(String attributeName, BeanDefinitionDecorator decorator) {
        this.delegate.doRegisterBeanDefinitionDecoratorForAttribute(attributeName, decorator);
    }

    protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
        this.delegate.doRegisterBeanDefinitionParser(elementName, parser);
    }

    private class NamespaceHandlerDelegate extends NamespaceHandlerSupport {

        @Override
        public void init() {
            // TODO: This kind of schema
            WorkflowNamespaceHandler.this.init();
        }

        private void doRegisterBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator decorator) {
            super.registerBeanDefinitionDecorator(elementName, decorator);
        }

        private void doRegisterBeanDefinitionDecoratorForAttribute(String attributeName, BeanDefinitionDecorator decorator) {
            super.registerBeanDefinitionDecoratorForAttribute(attributeName, decorator);
        }

        private void doRegisterBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
            super.registerBeanDefinitionParser(elementName, parser);
        }
    }
}