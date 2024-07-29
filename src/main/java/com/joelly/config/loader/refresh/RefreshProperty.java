package com.joelly.config.loader.refresh;

import org.springframework.core.env.PropertySource;

import java.util.Map;

public interface RefreshProperty {

    /**
     * 刷新配置
     * @param thisPropertySource
     */
    void refresh(PropertySource<Map<String, Object>> thisPropertySource);
}
