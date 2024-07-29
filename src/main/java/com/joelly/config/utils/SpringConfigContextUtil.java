package com.joelly.config.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringConfigContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    public static <T> T getBeanWithCatch(Class<T> clazz) {
        try {
            return (T) context.getBean(clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
