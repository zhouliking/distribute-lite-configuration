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
@TableName("simple_app_deploy_machine")
public class AppDeployMachine extends Model<AppDeployMachine> {
    //ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 应用名
    private String application;

    // 环境（如开发、测试、生产）
    private String env;

    // 机器IP地址
    private String ip;

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

