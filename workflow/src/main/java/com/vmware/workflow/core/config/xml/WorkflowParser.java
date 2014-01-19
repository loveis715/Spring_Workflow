package com.vmware.workflow.core.config.xml;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

// TODO: Investigate the full process of bean creation, via AbstractSimpleBeanDefinitionParser
// FIXME: Investigate all base classes for parsing beans. Time is limited now.
public class WorkflowParser extends ContainerActivityParser {

    @Override
    protected String getBeanClassName(Element element) {
        return NamespaceConstants.BASE_PACKAGE + ".workflow.WorkflowFactoryBean";
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String attributeValue = element.getAttribute("workflow-interface");
        if (StringUtils.hasText(attributeValue)) {
            builder.addPropertyValue("workflowInterface", new TypedStringValue(attributeValue));
        }
        super.doParse(element, parserContext, builder);
    }
}