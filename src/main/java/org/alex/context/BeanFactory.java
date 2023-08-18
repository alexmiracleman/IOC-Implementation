package org.alex.context;

import lombok.extern.slf4j.Slf4j;
import org.alex.entity.Bean;
import org.alex.entity.BeanDefinition;
import org.alex.exception.ContainerException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanFactory {
    public List<Bean> makeBeansFromBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        List<Bean> beans = getRawBeans(beanDefinitions);
        injectValueDependencies(beans, beanDefinitions);
        injectRefDependencies(beans, beanDefinitions);
        return beans;
    }

    protected List<Bean> getRawBeans(List<BeanDefinition> beanDefinitions) {
        List<Bean> beans = new ArrayList<>();
        for (BeanDefinition definition : beanDefinitions) {
            String beanId = definition.getId();
            Object object;
            try {
                object = Class.forName(definition.getBeanClassName()).getConstructor().newInstance();
            } catch (Exception e) {
                log.error("Failed to create bean instance for the bean: {}", beanId, e);
                throw new ContainerException("Error creating bean: " + beanId, e);
            }
            Bean bean = new Bean(beanId, object);
            beans.add(bean);
        }
        return beans;
    }

    protected void injectValueDependencies(List<Bean> beans, List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Object beanInstance = bean.getValue();
                    Map<String, String> dependencies = beanDefinition.getDependencies();
                    if (dependencies.isEmpty()) {
                        break;
                    }
                    for (Map.Entry<String, String> dependencyEntry : dependencies.entrySet()) {
                        String propertyName = dependencyEntry.getKey();
                        String propertyValue = dependencyEntry.getValue();
                        setPropertyValue(beanInstance, propertyName, propertyValue);
                    }
                }
            }
        }
    }

    protected void injectRefDependencies(List<Bean> beans, List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Object beanInstance = bean.getValue();
                    Map<String, String> refDependencies = beanDefinition.getRefDependencies();
                    if (refDependencies.isEmpty()) {
                        break;
                    }
                    for (Map.Entry<String, String> refDependencyEntry : refDependencies.entrySet()) {
                        String propertyName = refDependencyEntry.getKey();
                        String refBeanId = refDependencyEntry.getValue();
                        for (Bean refBean : beans) {
                            if (refBeanId.equals(refBean.getId())) {
                                Object refBeanInstance = refBean.getValue();
                                setPropertyValue(beanInstance, propertyName, refBeanInstance);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void setPropertyValue(Object beanInstance, String propertyName, Object propertyValue) {
        try {
            Class<?> beanClass = beanInstance.getClass();
            Field field = beanClass.getDeclaredField(propertyName);
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            Object convertedValue = setValue(propertyValue, fieldType);
            field.set(beanInstance, convertedValue);
        } catch (Exception e) {
            log.error("Failed to set property value for the property: {} on the bean instance: {}",
                    propertyName, beanInstance, e);
            throw new ContainerException("Failed to set property value.", e);
        }
    }

    protected Object setValue(Object propertyValue, Class<?> fieldType) {
        Object value = null;
        if (fieldType.isAssignableFrom(propertyValue.getClass())) {
            value = propertyValue;
        } else if (fieldType == int.class) {
            value = Integer.parseInt(propertyValue.toString());
        } else if (fieldType == long.class) {
            value = Long.parseLong(propertyValue.toString());
        } else if (fieldType == double.class) {
            value = Double.parseDouble(propertyValue.toString());
        } else if (fieldType == float.class) {
            value = Float.parseFloat(propertyValue.toString());
        } else if (fieldType == short.class) {
            value = Short.parseShort(propertyValue.toString());
        } else if (fieldType == byte.class) {
            value = Byte.parseByte(propertyValue.toString());
        } else if (fieldType == boolean.class) {
            value = Boolean.parseBoolean(propertyValue.toString());
        } else if (fieldType == char.class) {
            if (propertyValue.toString().length() > 0) {
                value = propertyValue.toString().charAt(0);
            }
        } else {
            log.error("Failed to set property value: {}; field type: {}", propertyValue, fieldType);
            throw new ContainerException("Unsupported data type.");
        }
        return value;
    }
}
