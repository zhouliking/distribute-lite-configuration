package com.joelly.config.configs;

import com.joelly.config.constants.CommonConstant;
import com.joelly.config.loader.refresh.RefreshProperty;
import com.joelly.config.loader.refresh.impl.JasyptRefreshProperty;
import com.joelly.config.service.ConfigManagerService;
import com.joelly.config.service.DeployMachineManagerService;
import com.joelly.config.service.RefreshConfigService;
import com.joelly.config.service.exts.WebOpsUserService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动配置与注入
 */
@Configuration
@ComponentScan(basePackages = {"com.joelly.config"})
@ConditionalOnProperty(name = "joelly.config.enable", havingValue = "true")
@ConfigurationProperties
@Slf4j
@MapperScan("com.joelly.config.dao")
public class SimpleConfigAutoConfiguration {

    @Bean
    public ConfigManagerService configManagerService() {
        return new ConfigManagerService();
    }

    @Bean
    public DeployMachineManagerService deployMachineManagerService() {
        return new DeployMachineManagerService();
    }

    @Bean
    public RefreshConfigService refreshConfigService() {
        return new RefreshConfigService();
    }
    @Bean
    @ConditionalOnMissingBean
    public RefreshProperty refreshProperty() {
        return new JasyptRefreshProperty();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebOpsUserService hxConfigUserService() {
        return new WebOpsUserService() {
            @Override
            public String getCurrentUserId() {
                return CommonConstant.DEFAULT_USER;
            }
        };
    }
}
