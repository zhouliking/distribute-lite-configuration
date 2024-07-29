package com.joelly.config.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.joelly.config.constants.CommonConstant.CUSTOM_PROPERTIES_NAME;


public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final int ORDER = -2147482638;


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> properties = new ConcurrentHashMap<>();
        MapPropertySource propertySource = new MapPropertySource(CUSTOM_PROPERTIES_NAME, properties);
        EnvironmentPropertyContext.setConfigurableEnvironment(environment);
        Boolean configEnable = environment.getProperty("joelly.config.enable", Boolean.class);
        if (!Boolean.TRUE.equals(configEnable)) {
            System.out.println("postProcessEnvironment, config not enable, currentConfig " + configEnable);
            return;
        }
        String applicationName = EnvironmentPropertyContext.getApplicationName();
        String env = EnvironmentPropertyContext.getSpringProfilesActive();
        System.out.println("applicationName: " + applicationName);
        System.out.println("env: " + env);

        String url = EnvironmentPropertyContext.getDbUrl();
        String user = EnvironmentPropertyContext.getDbUsername();
        String password = EnvironmentPropertyContext.getDbPassword();
        String startInitSql = EnvironmentPropertyContext.getStartInitSql();
        String driverName = EnvironmentPropertyContext.getDbDriverClassName();
        System.out.println("driverName: " + driverName);
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        startInitSql = String.format(startInitSql, addQuotation(applicationName), addQuotation(env));
        System.out.println("config db url: " + url);
        System.out.println("startInitSql: " + startInitSql);
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(startInitSql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String key = resultSet.getString("cfg_key");
                String value = resultSet.getString("cfg_value");
                System.out.println("id: " + id + ", key: " + key + ", value: " + value);
                properties.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MutablePropertySources propertySources = environment.getPropertySources();
        //propertySources.addLast(propertySource); // addLast 配置优先级低，yml /xml 中的配置会覆盖
        propertySources.addFirst(propertySource);
    }

    private String addQuotation (String value) {
        return String.format("'%s'", value);
    }




    @Override
    public int getOrder() {
        return ORDER;
    }
}
