//package com.joelly.config.config.loader;
//
//import com.joelly.config.config.loader.annotations.HxConfigRefresh;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//
//@Component
//@Slf4j
//public class ValueAnnotationPostProcessor implements BeanPostProcessor {
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        Class<?> beanClass = bean.getClass();
//
//        boolean hasHxConfigRefresh = beanClass.isAnnotationPresent(HxConfigRefresh.class);
//
//        Field[] fields = bean.getClass().getDeclaredFields();
//
//        if (fields == null || fields.length <= 0) {
//            return bean;
//        }
//
//        if ("configTest".equals(beanName)) {
//            log.info("postProcessBeforeInitialization {}", beanName);
//        }
//        for (Field field : fields) {
//            if (field.isAnnotationPresent(org.springframework.beans.factory.annotation.Value.class)) {
//                // 可以在这里处理或记录这个字段的信息
//                log.info("Bean: " + beanName + " has a @Value field: " + field.getName());
//                // 如果需要，还可以获取注解的值等
//                // String value = field.getAnnotation(org.springframework.beans.factory.annotation.Value.class).value();
//            }
//        }
//
//
//
//
//
//        return bean;
//    }
//}