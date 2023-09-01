package org.alex.context;

import lombok.extern.slf4j.Slf4j;
import org.alex.entity.Bean;
import org.alex.entity.BeanDefinition;
import org.alex.exception.ContainerException;
import org.alex.reader.BeanDefinitionReader;
import org.alex.reader.dom.DOMBeanDefinitionReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClassPathApplicationContext implements ApplicationContext {
    private List<Bean> beans;

    public ClassPathApplicationContext(String... paths) {
        this(new DOMBeanDefinitionReader(paths));
    }

    public ClassPathApplicationContext(BeanDefinitionReader reader) {
        List<BeanDefinition> beanDefinitions = reader.readBeanDefinitions();
        this.beans = new BeanFactory().makeBeans(beanDefinitions);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        parameterCheck(clazz);
        for (Bean bean : beans) {
            if (clazz.isInstance(bean.getValue())) {
                return clazz.cast(bean.getValue());
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) {
        parameterCheck(id);
        for (Bean bean : beans) {
            if (bean.getId().equals(id) && clazz.isInstance(bean.getValue())) {
                return clazz.cast(bean.getValue());
            }
        }
        return null;
    }

    @Override
    public Object getBean(String id) {
        parameterCheck(id);
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return bean.getValue();
            }
        }
        return null;
    }

    @Override
    public List<String> getBeanNames() {
        List<String> beanNames = new ArrayList<>();
        for (Bean bean : beans) {
            beanNames.add(bean.getId());
        }
        return beanNames;
    }

    void parameterCheck(Class<?> clazz) {
        if (clazz == null) {
            log.error("Bean class is null");
            throw new ContainerException("Bean class is null");
        }
    }

    void parameterCheck(String id) {
        if (id == null || id.isEmpty()) {
            log.error("Bean id: {} is null or empty", id);
            throw new ContainerException("Bean id is null or empty");
        }
    }
}