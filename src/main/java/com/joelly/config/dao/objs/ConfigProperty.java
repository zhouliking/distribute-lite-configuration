package com.joelly.config.dao.objs;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 国家行政区域(Area)表实体类
 *
 * @author makejava
 * @since 2024-06-04 11:01:52
 */
@SuppressWarnings("serial")
@Data
@TableName("simple_config_property")
public class ConfigProperty extends Model<ConfigProperty> {
    //ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用程序名称。
     */
    private String application;

    /**
     * 环境标识，如dev、test、prod等。
     */
    private String env;

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
     * 创建者标识，记录该配置项由谁创建。
     */
    private String createdBy;

    /**
     * 最后更新者标识，记录最后一次修改该配置项的用户。
     */
    private String updatedBy;

    /**
     * 创建时间，记录该配置项何时被创建。
     */
    private Date createTime;

    /**
     * 更新时间，记录该配置项最后一次被修改的时间。
     */
    private Date updateTime;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

