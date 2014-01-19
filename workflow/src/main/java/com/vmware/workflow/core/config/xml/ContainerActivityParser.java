package com.vmware.workflow.core.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ContainerActivityParser extends AbstractSingleBeanDefinitionParser {

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        ManagedList activityList = new ManagedList();
        NodeList children = element.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                BeanDefinitionHolder holder = this.parseChild((Element) child, parserContext, builder.getBeanDefinition());
                activityList.add(holder);
            }
        }
        builder.addPropertyValue("activities", activityList);
    }

    private BeanDefinitionHolder parseChild(Element element, ParserContext parserContext, BeanDefinition parentDefinition) {
        String localName = element.getLocalName();
        BeanDefinition beanDefinition = null;
        AbstractSingleBeanDefinitionParser activityParser = null;
        if (NamespaceConstants.ACTIVITY.equals(localName)) {
            activityParser = ParserRegistry.getParser(NamespaceConstants.ACTIVITY);
        } else if (NamespaceConstants.WHILE.equals(localName)) {
            activityParser = ParserRegistry.getParser(NamespaceConstants.WHILE);
        }

        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        ParserContext childContext = new ParserContext(delegate.getReaderContext(), delegate, parentDefinition);
        beanDefinition = activityParser.parse(element, childContext);

        if (beanDefinition == null) {
            parserContext.getReaderContext().error("child BeanDefinition must not be null", element);
        }
        else {
            String beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, parserContext.getRegistry(), true);
            return new BeanDefinitionHolder(beanDefinition, beanName);
        }
        return null;
    }
}