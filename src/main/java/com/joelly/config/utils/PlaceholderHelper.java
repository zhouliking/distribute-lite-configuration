package com.joelly.config.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderHelper {
    private static final Pattern pattern = Pattern.compile("(\\$\\{([^}]+)\\})|(#\\{([^}]+)\\})");

    /**
     * 解析配置
     * 如：
     * 输入: ${a}.${b}.${c}" 或 aa="--> ${a}.#{b}.${c}" 或 aa="--> #{a}.#{b}.${c}" 或 aa="--> #{a}.#{b}.#{c}"
     * 输出: [a, b, c]
     * @param input
     * @return
     */
    public static Set<String> parsePlaceholders(String input) {
        Set<String> keys = new HashSet<>();
        if (StringUtils.isBlank(input)) {
            return keys;
        }

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 检查是否匹配了 ${...} 或 #{...}
            if (matcher.group(1) != null || matcher.group(3) != null) {
                String key = matcher.group(2) != null ? matcher.group(2) : matcher.group(4);
                int defaultValueIndex = key.indexOf(':');
                if (defaultValueIndex != -1) {
                    key = key.substring(0, defaultValueIndex);
                }
                keys.add(key);
            }
        }

        return keys;
    }
}
