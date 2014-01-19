package com.vmware.workflow.core.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class WhileActivityParser extends ContainerActivityParser {

    @Override
    protected String getBeanClassName(Element element) {
        return NamespaceConstants.BASE_PACKAGE + ".activity.WhileActivity";
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String conditionBean = element.getAttribute("condition-bean");
        String conditionMethod = element.getAttribute("condition-method");
        builder.addPropertyReference("conditionBean", conditionBean);
        builder.addPropertyValue("conditionMethod", conditionMethod);
        super.doParse(element, parserContext, builder);
    }
}