package org.alex.reader.dom;

import org.alex.entity.BeanDefinition;
import org.alex.reader.BeanDefinitionReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DOMBeanDefinitionReader implements BeanDefinitionReader {
    private String[] paths;
    private BeanDefinition beanDefinition;

    public DOMBeanDefinitionReader(String... paths) {
        this.paths = paths;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String path : paths) {
            NodeList beanList = parseBeansFromXML(path);
            for (int i = 0; i < beanList.getLength(); i++) {
                Element beanElement = (Element) beanList.item(i);
                String id = beanElement.getAttribute("id");
                String className = beanElement.getAttribute("class");
                beanDefinition = new BeanDefinition();
                beanDefinition.setId(id);
                beanDefinition.setBeanClassName(className);
                beanDefinition.setDependencies(new HashMap<>());
                beanDefinition.setRefDependencies(new HashMap<>());
                fillDependency(beanElement, beanDefinition);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;
    }

    public InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    public NodeList parseBeansFromXML(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            ByteArrayInputStream content = new ByteArrayInputStream(inputStream.readAllBytes());
            content.reset();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(content);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("bean");
            return nodeList;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillDependency(Element beanElement, BeanDefinition beanDefinition) {
        NodeList propertyNodes = beanElement.getElementsByTagName("property");
        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Element propertyElement = (Element) propertyNodes.item(i);
            String propertyName = propertyElement.getAttribute("name");
            String propertyValue = propertyElement.getAttribute("value");
            String propertyRef = propertyElement.getAttribute("ref");
            if (!propertyRef.isEmpty()) {
                beanDefinition.getRefDependencies().put(propertyName, propertyRef);
            } else {
                beanDefinition.getDependencies().put(propertyName, propertyValue);
            }
        }
    }
}