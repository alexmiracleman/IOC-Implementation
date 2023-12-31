package org.alex.context;

import java.util.List;

public interface ApplicationContext {
    <T> T getBean(Class<T> clazz);

    <T> T getBean(String id, Class<T> clazz);

    Object getBean(String id);

    List<String> getBeanNames();
}
