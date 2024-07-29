package com.joelly.config.loader;

import lombok.Data;

public class ApplicationConfigContext {


    private static String localIp;

    public static String getLocalIp() {
        return localIp;
    }

    public static void setLocalIp(String localIp) {
        ApplicationConfigContext.localIp = localIp;
    }
}
