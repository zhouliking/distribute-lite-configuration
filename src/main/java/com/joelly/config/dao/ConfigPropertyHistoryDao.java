package com.joelly.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;;
import com.joelly.config.dao.objs.ConfigProperty;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 配置历史记录表
 *
 * @author makejava
 * @since 2024-06-04 11:08:48
 */
//@Mapper
public interface ConfigPropertyHistoryDao extends BaseMapper<ConfigProperty> {

    @Select("SELECT id FROM hx_area WHERE parent_id = #{parentId}")
    List<Long> selectIdsByParentId(Long parentId);

}

