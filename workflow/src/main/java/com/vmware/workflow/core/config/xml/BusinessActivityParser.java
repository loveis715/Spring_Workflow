package com.vmware.workflow.core.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class BusinessActivityParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return NamespaceConstants.BASE_PACKAGE + ".activity.BusinessActivity";
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String ref = element.getAttribute("ref");
        String method = element.getAttribute("method");
        builder.addPropertyReference("targetObject", ref);
        builder.addPropertyValue("targetMethodName", method);
    }
}