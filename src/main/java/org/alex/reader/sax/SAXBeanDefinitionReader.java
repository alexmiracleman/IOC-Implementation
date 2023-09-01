package org.alex.reader.sax;

import lombok.extern.slf4j.Slf4j;
import org.alex.entity.BeanDefinition;
import org.alex.exception.ContainerException;
import org.alex.reader.BeanDefinitionReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SAXBeanDefinitionReader implements BeanDefinitionReader {
    private static final SAXParserFactory FACTORY = SAXParserFactory.newInstance();
    private final SAXParser saxParser;
    private final Handler handler;
    private final String[] paths;

    public SAXBeanDefinitionReader(String... paths) {
        this.paths = paths;
        this.handler = new Handler();

        try {
            saxParser = FACTORY.newSAXParser();
        } catch (Exception e) {
            log.error("Failed to create SAXParser", e);
            throw new ContainerException("Error occurred while creating the SAXParser.");
        }
    }

    private InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        List<String> importPaths = new ArrayList<>();
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String path : paths) {
            try (InputStream inputStream = getResourceAsStream(path)) {
                handler.reset();
                saxParser.parse(inputStream, handler);
                handler.getImportPaths();
                importPaths.addAll(handler.getImportPaths());
                beanDefinitions.addAll(handler.getBeanDefinitions());
            } catch (Exception e) {
                log.error("Failed to read bean definition from the import path: {}", path, e);
                throw new ContainerException("Error occurred while reading bean definitions.");
            }
        }
        for (String importPath : importPaths) {
            if (!importPath.isEmpty()) {
                try (InputStream inputStream = getResourceAsStream(importPath)) {
                    handler.reset();
                    saxParser.parse(inputStream, handler);
                    beanDefinitions.addAll(handler.getBeanDefinitions());
                } catch (Exception e) {
                    log.error("Failed to read bean definition from the path: {}", importPath, e);
                    throw new ContainerException("Error occurred while reading bean definitions.");
                }
            }
        }
        return beanDefinitions;
    }

}
