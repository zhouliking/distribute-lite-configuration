package com.joelly.config.entity;

import lombok.Data;

import java.util.Map;

/**
 * 国家行政区域(Area)
 *
 * @since 2024-06-04 11:01:52
 */
@SuppressWarnings("serial")
@Data
public class ConfigInfo {
    //ID
    private Long id;

    //扩展信息
    private Map<String, Object> extInfo;
}

