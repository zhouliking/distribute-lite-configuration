package com.joelly.config.convert;

import com.joelly.config.dao.objs.ConfigProperty;
import com.joelly.config.entity.vo.ConfigPropertyVO;
import org.springframework.beans.BeanUtils;

public class ConfigConvert {

    public static ConfigProperty convertInsertFromVO(String application, String env, ConfigPropertyVO vo){
        ConfigProperty configProperty = new ConfigProperty();
        BeanUtils.copyProperties(vo, configProperty);
        configProperty.setApplication(application);
        configProperty.setEnv(env);
        configProperty.setUpdatedBy(vo.getOperator());
        configProperty.setCreatedBy(vo.getOperator());
        return configProperty;
    }

    public static ConfigProperty convertUpdateFromVO(String application, String env, ConfigPropertyVO vo){
        ConfigProperty configProperty = new ConfigProperty();
        BeanUtils.copyProperties(vo, configProperty);
        configProperty.setApplication(application);
        configProperty.setEnv(env);
        configProperty.setUpdatedBy(vo.getOperator());
        return configProperty;
    }
}
