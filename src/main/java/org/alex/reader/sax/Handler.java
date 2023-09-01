package org.alex.reader.sax;

import lombok.extern.slf4j.Slf4j;
import org.alex.entity.BeanDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class Handler extends DefaultHandler {
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }
    public List<String> getImportPaths() {
        return importPaths;
    }

    private final List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private BeanDefinition beanDefinition;
    private List<String> importPaths = new ArrayList<>();


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("import")) {
            String path = attributes.getValue("resource");
            importPaths.add(path);
        }
        if (qName.equalsIgnoreCase("bean")) {
            String id = attributes.getValue("id");
            String className = attributes.getValue("class");
            beanDefinition = new BeanDefinition();
            beanDefinition.setId(id);
            beanDefinition.setBeanClassName(className);
            beanDefinition.setDependencies(new HashMap<>());
            beanDefinition.setRefDependencies(new HashMap<>());
        } else if (qName.equalsIgnoreCase("property")) {
            String propertyName = attributes.getValue("name");
            String propertyValue = attributes.getValue("value");
            String propertyRef = attributes.getValue("ref");
            if (propertyRef == null) {
                beanDefinition.getDependencies().put(propertyName, propertyValue);
            } else {
                beanDefinition.getRefDependencies().put(propertyName, propertyRef);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("bean")) {
            beanDefinitions.add(beanDefinition);
            beanDefinition = null;
        }

    }

    public void reset() {
        beanDefinitions.clear();
        beanDefinition = null;
    }
}
