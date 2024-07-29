package com.joelly.config.configs.bean;

import lombok.Data;

@Data
public class AreaInfoCacheConfig {

    /**
     * 默认缓存1天
     */
    private int cacheExpireMinutes = 60 * 24;

    /**
     * 最大缓存数量, 目前 1-4级区域约为45000个
     */
    private int maxCacheSize = 5000;

}
