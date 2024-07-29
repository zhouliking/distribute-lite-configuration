package com.joelly.config.dao;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joelly.config.dao.objs.ConfigProperty;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 配置记录表
 *
 * @author makejava
 * @since 2024-06-04 11:08:48
 */
//@Mapper
public interface ConfigPropertyDao extends BaseMapper<ConfigProperty> {

    @Select("SELECT * FROM hx_config_property WHERE application = #{application} and env = #{env} and cfg_key = #{cfgKey}")
    ConfigProperty selectByAppEnvKey(@Param("application") String application, @Param("env") String env, @Param("cfgKey")String cfgKey);


    @Select("SELECT * FROM hx_config_property WHERE application = #{application} and env = #{env} and update_time > #{updateTime}")
    List<ConfigProperty> selectByAppEnvUpdateTime(@Param("application") String application, @Param("env") String env, @Param("updateTime") Date updateTime);

    @Update({
            "<script>",
            "UPDATE hx_config_property",
            "SET cfg_value = #{cfgValue}, updated_by = #{updatedBy}, version = version + 1",
            "WHERE application = #{application}",
            "  AND env = #{env}",
            "  AND cfg_key = #{cfgKey}",
            "  AND version = #{version}",
            "</script>"
    })
    int updateByAppEnvKey(ConfigProperty configProperty);

}

