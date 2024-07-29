package com.joelly.config.utils;

import com.joelly.config.exception.SimpleConfigException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class AssertUtils {

    public static <T> void assertNull(T t, String msg) {
        if (!Objects.isNull(t)) {
            throw new SimpleConfigException(msg);
        }
    }
    public static <T> void assertNotNull(T t, String msg) {
        if (Objects.isNull(t)) {
            throw new SimpleConfigException(msg);
        }
    }

    public static void assertTrue(Boolean value, String msg) {
        if (!Boolean.TRUE.equals(value)) {
            throw new SimpleConfigException(msg);
        }
    }

    public static void assertNotBlank(String value, String msg) {
        if (StringUtils.isBlank(value)) {
            throw new SimpleConfigException(msg);
        }
    }
}
