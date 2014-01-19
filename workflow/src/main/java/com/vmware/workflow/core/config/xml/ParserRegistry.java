package com.vmware.workflow.core.config.xml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;

public class ParserRegistry {
    private static Map<String, AbstractSingleBeanDefinitionParser> parsers;

    public static AbstractSingleBeanDefinitionParser getParser(String parserName) {
        if (parsers == null) {
            initParsers();
        }

        return parsers.get(parserName);
    }

    private static void initParsers() {
        parsers = new HashMap<String, AbstractSingleBeanDefinitionParser>();
        parsers.put(NamespaceConstants.ACTIVITY, new BusinessActivityParser());
        parsers.put(NamespaceConstants.WHILE, new WhileActivityParser());
    }
}