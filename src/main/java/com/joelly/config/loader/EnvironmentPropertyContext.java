package com.joelly.config.loader;

import com.joelly.config.exception.SimpleConfigException;
import com.joelly.config.loader.refresh.RefreshProperty;
import com.joelly.config.utils.SpringConfigContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import java.util.Map;

import static com.joelly.config.constants.CommonConstant.CUSTOM_PROPERTIES_NAME;

@Slf4j
public class EnvironmentPropertyContext {

    public static ConfigurableEnvironment configurableEnvironment;

    private static RefreshProperty refreshProperty;

    public static String getCustomProperty(String configKey) {
        return configurableEnvironment.getProperty(configKey);
    }

    public static void setCustomProperty(String configKey, String cfgValue) {
        PropertySource<?> propertySource = configurableEnvironment.getPropertySources()
                .get(CUSTOM_PROPERTIES_NAME);
        PropertySource<Map<String, Object>> thisPropertySource = (PropertySource<Map<String, Object>>) propertySource;
        thisPropertySource.getSource().put(configKey, cfgValue);
        refresh(thisPropertySource);
    }

    private static synchronized void refresh(PropertySource<Map<String, Object>> propertySource) {
        if (refreshProperty == null) {
            refreshProperty = SpringConfigContextUtil.getBeanWithCatch(RefreshProperty.class);
        }
        if (refreshProperty == null) {
            log.info("refreshProperty is null, not refresh!");
            return;
        }
        refreshProperty.refresh(propertySource);
    }


    public static String getSpringProfilesActive() {
        return getProperty("spring.profiles.active", "defaultEnv");
    }
    public static String getServerPort() {
        return getProperty("server.port", "8080");
    }

    public static String getApplicationName() {
        return getProperty("spring.application.name", null);
    }

    public static String getDbDriverClassName() {
        return getProperty("joelly.config.db.driver-class-name", "com.mysql.cj.jdbc.Driver");
    }

    public static String getDbUrl() {
        return getProperty("joelly.config.db.url", null);
    }

    public static String getDbUsername() {
        return getProperty("joelly.config.db.username", null);
    }

    public static String getDbPassword() {
        return getProperty("joelly.config.db.password", "");
    }

    public static String getStartInitSql() {
        return getProperty("joelly.config.db.startInitSql", "select * from simple_config_property where application=%s and env=%s");
    }

    public static void setConfigurableEnvironment(ConfigurableEnvironment environment) {
        configurableEnvironment = environment;
    }

    private static String getProperty(String key, String defaultValue) {
        String value = configurableEnvironment.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new SimpleConfigException(String.format("please config '%s' in yaml or property! ", key));
    }

}
