package com.joelly.config.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class GsonProvider {
    private static final Gson GSON = new Gson();

    private GsonProvider() {
        // 私有构造函数防止实例化
    }

    private static Gson getInstance() {
        return GSON;
    }


    /**
     * 字符串转换
     *
     * @param jsonString
     * @param type : new TypeToken<T>(){}
     * @return
     * @param <T>
     */
    public static <T> T str2Obj(String jsonString, TypeToken<T> type) {
        T map = getInstance().fromJson(jsonString, type.getType());
        return map;
    }

    public static <T> T str2Obj(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return (T) gson.fromJson(jsonString, clazz);
    }

    public static String obj2Str(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return getInstance().toJson(obj);
    }
}
