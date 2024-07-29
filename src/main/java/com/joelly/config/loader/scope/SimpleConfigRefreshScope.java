package com.joelly.config.loader.scope;

import com.joelly.config.utils.FieldUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimpleConfigRefreshScope implements Scope {

    public static final String SCOPE_NAME = "hxConfigRefreshScope";

    private ConcurrentHashMap<String, Object> beanCacheMap = new ConcurrentHashMap<>();

    private Set<String> hadProcessBeanSet = ConcurrentHashMap.newKeySet();

    private ConcurrentHashMap<String, Set<String>> cfgKeyBeanMap = new ConcurrentHashMap<>();

    private static final SimpleConfigRefreshScope HX_CONFIG_REFRESH_SCOPE_INSTANCE = new SimpleConfigRefreshScope();


    private SimpleConfigRefreshScope() {
    }

    public static SimpleConfigRefreshScope getInstance() {
        return HX_CONFIG_REFRESH_SCOPE_INSTANCE;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object bean = HX_CONFIG_REFRESH_SCOPE_INSTANCE.beanCacheMap.get(name);
        if (bean == null) {
            bean = objectFactory.getObject();
            beanCacheMap.put(name, bean);
            refreshCfgKeyBeanMap(name, bean);
        }
        return bean;
    }

    @Override
    public Object remove(String name) {
        return beanCacheMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    private void refreshCfgKeyBeanMap(String name, Object bean) {
        if (hadProcessBeanSet.contains(name)) {
            return;
        }
        hadProcessBeanSet.add(name);
        Set<String> fieldValues = FieldUtils.getAllValueFieldKey(bean);
        if (CollectionUtils.isEmpty(fieldValues)) {
            return;
        }
        for (String key : fieldValues) {
            Set<String> keyBeanSet = cfgKeyBeanMap.get(key);
            if (keyBeanSet == null) {
                keyBeanSet = ConcurrentHashMap.newKeySet();
                cfgKeyBeanMap.put(key, keyBeanSet);
            }
            keyBeanSet.add(name);
        }
    }


    public static void cleanAll() {
        HX_CONFIG_REFRESH_SCOPE_INSTANCE.beanCacheMap.clear();
    }

    public static Set<String> queryAllUseClass(String key) {
        return HX_CONFIG_REFRESH_SCOPE_INSTANCE.cfgKeyBeanMap.get(key);
    }

    public static void clean(String key) {
        Set<String> beanNames = HX_CONFIG_REFRESH_SCOPE_INSTANCE.cfgKeyBeanMap.get(key);
        if (CollectionUtils.isEmpty(beanNames)) {
            return;
        }
        beanNames.stream().forEach(name -> HX_CONFIG_REFRESH_SCOPE_INSTANCE.beanCacheMap.remove(name));
    }
}
