//package com.joelly.config.config.loader;
//
//import org.springframework.boot.context.config.*;
//import org.springframework.core.env.MapPropertySource;
//import org.springframework.core.env.PropertySource;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class CustomConfigDataLoader implements ConfigDataLoader {
//
//
//    @Override
//    public boolean isLoadable(ConfigDataLoaderContext context, ConfigDataResource resource) {
//        return ConfigDataLoader.super.isLoadable(context, resource);
//    }
//
//    @Override
//    public ConfigData load(ConfigDataLoaderContext context, ConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {
//        // 创建一个空的配置数据对象
//        List<PropertySource<?>> propertySources = new ArrayList<>();
//
//        // 假设我们从硬编码的配置开始
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("my.custom.config", "value22");
//        properties.put("my.custom.number", 123);
//
//        // 创建一个PropertySource并添加到ConfigData
//        PropertySource<?> propertySource = new MapPropertySource("customProperties", properties);
//        propertySources.add(propertySource);
//
//        System.out.println("CustomConfigDataLoader.load ....................");
//        // 返回配置数据对象
//        return new ConfigData(propertySources);
//    }
//}