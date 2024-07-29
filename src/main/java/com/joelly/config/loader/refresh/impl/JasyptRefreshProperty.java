package com.joelly.config.loader.refresh.impl;

import com.joelly.config.loader.refresh.RefreshProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class JasyptRefreshProperty implements RefreshProperty {
    private static final String SPECIAL_CONFIG_JASY = "EncryptableMapPropertySourceWrapper";
    @Override
    public void refresh(PropertySource<Map<String, Object>> thisPropertySource) {
        if (thisPropertySource == null) {
            log.info("JasyptRefreshProperty.refresh, thisPropertySource is null");
            return;
        }
        if (!SPECIAL_CONFIG_JASY.equalsIgnoreCase(thisPropertySource.getClass().getSimpleName())) {
            return;
        }
        Class<?> propertySourceClazz = thisPropertySource.getClass();
        try {
            Method method = propertySourceClazz.getMethod("refresh");
            method.invoke(thisPropertySource);
        } catch (NoSuchMethodException e) {
            log.info("refresh method not exit, clazz: {}", SPECIAL_CONFIG_JASY, e);
        } catch (InvocationTargetException e) {
            log.info("refresh method invoke fail, clazz: {}", SPECIAL_CONFIG_JASY, e);
        } catch (IllegalAccessException e) {
            log.info("refresh method invoke Illegal, clazz: {}", SPECIAL_CONFIG_JASY, e);
        }
    }
}
