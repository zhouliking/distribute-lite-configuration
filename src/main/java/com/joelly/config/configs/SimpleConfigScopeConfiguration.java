package com.joelly.config.configs;

import com.joelly.config.loader.scope.SimpleConfigRefreshScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Slf4j
public class SimpleConfigScopeConfiguration {

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope(SimpleConfigRefreshScope.SCOPE_NAME, SimpleConfigRefreshScope.getInstance());
        return configurer;
    }

}
