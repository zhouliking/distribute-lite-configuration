package com.joelly.config.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class FieldUtils {
    public static Set<String> getAllValueFieldKey(Object bean){
        Set<String> sets = new HashSet<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return sets;
        }
        for (Field field : fields) {
            if (!field.isAnnotationPresent(org.springframework.beans.factory.annotation.Value.class)) {
                continue;
            }

            String valueStr = field.getAnnotation(org.springframework.beans.factory.annotation.Value.class).value();
            Set<String> fieldValues = PlaceholderHelper.parsePlaceholders(valueStr);
            // 可以在这里处理或记录这个字段的信息
            log.info("getAllValueFieldKey, bean: {}, field:{}", bean.getClass(), field.getName(), fieldValues);
            sets.addAll(fieldValues);
        }
        return sets;
    }
}
