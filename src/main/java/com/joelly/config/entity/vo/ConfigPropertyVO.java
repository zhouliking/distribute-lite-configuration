package com.joelly.config.entity.vo;

import lombok.Data;

@Data
public class ConfigPropertyVO {

    /**
     * 配置键，用于唯一标识配置项。
     */
    private String cfgKey;

    /**
     * 配置值，与cfgKey对应的具体配置内容。
     */
    private String cfgValue;

    /**
     * 版本号，用于跟踪配置项的变更历史。
     */
    private Integer version;

    /**
     * 操作者
     */
    private String operator;
}
