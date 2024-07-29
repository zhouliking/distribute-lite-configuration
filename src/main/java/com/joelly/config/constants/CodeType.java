package com.joelly.config.constants;

/**
 * 行政编码说明
 */
public class CodeType {

    /**
     * 正常编码
     */
    public static final Integer NORMAL = 0;

    /**
     * 编码缺失，表明这是一个虚拟区域
     */
    public static final Integer NO_CODE = 1;

    /**
     * 与父级不匹配，可能经过行政调整
     */
    public static final Integer NOT_MATCH = 2;
}
