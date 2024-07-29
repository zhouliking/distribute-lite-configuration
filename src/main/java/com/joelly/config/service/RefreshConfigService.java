package com.joelly.config.service;

import com.joelly.config.dao.ConfigPropertyDao;
import com.joelly.config.dao.objs.ConfigProperty;
import com.joelly.config.loader.EnvironmentPropertyContext;
import com.joelly.config.loader.scope.SimpleConfigRefreshScope;
import com.joelly.config.utils.AssertUtils;
import com.joelly.config.utils.GsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class RefreshConfigService {

    /**
     * 定时刷新时间（秒，默认300秒）
     */
    @Value("${joelly.config.refreshRateSecond: 300}")
    private int refreshRateSecond;


    @Autowired
    private ConfigPropertyDao configPropertyDao;
    private Timer timer = new Timer();

    @PostConstruct
    public void init() {
        log.info("RefreshConfigService.init, refreshRateSecond: {}", refreshRateSecond);
        if (refreshRateSecond <= 1) {
            log.warn("RefreshConfigService.init, refreshRateSecond <= 1, not start refresh, refreshRateSecond: {}",
                    refreshRateSecond);
            return;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshTimeRange(refreshRateSecond);
            }
        }, refreshRateSecond * 1000, refreshRateSecond * 1000);

    }

    public void refreshSingeConfig(String configKey) {
        String env = EnvironmentPropertyContext.getSpringProfilesActive();
        String application = EnvironmentPropertyContext.getApplicationName();
        ConfigProperty dbConfig = configPropertyDao.selectByAppEnvKey(application, env, configKey);
        AssertUtils.assertNotNull(dbConfig, String.format("配置的key(%s)在本应用（%s）本环境(%s)中不存在", configKey, application, env));
        log.info("refreshSingeConfig, configKey: {}, dbConfig:{}", configKey, GsonProvider.obj2Str(dbConfig));
        refreshSingle(configKey, dbConfig.getCfgValue());
    }


    public void refreshTimeRange(int gepSecond) {
        String env = EnvironmentPropertyContext.getSpringProfilesActive();
        String application = EnvironmentPropertyContext.getApplicationName();

        Date pastDate = new Date(System.currentTimeMillis() - gepSecond * 1000 * 2);
        List<ConfigProperty> configPropertyList = configPropertyDao.selectByAppEnvUpdateTime(application, env, pastDate);
        if (CollectionUtils.isEmpty(configPropertyList)) {
            log.info("refreshTimeRange, no configProperty need refresh, application:{}, env: {}, pastDate: {}", application, env, pastDate);
            return;
        }
        log.info("refreshTimeRange, pre to refresh, application:{}, env: {}, pastDate: {}, size: {}",
                application, env, pastDate, CollectionUtils.size(configPropertyList));
        // 刷新
        for (ConfigProperty configProperty : configPropertyList) {
            String key = configProperty.getCfgKey();
            String value = configProperty.getCfgValue();
            log.info("refreshTimeRange single one, key: {}, value: {}, configProperty:{}", key, value, GsonProvider.obj2Str(configProperty));
            refreshSingle(key, value);
        }
    }


    private void refreshSingle(String configKey, String newValue) {
        log.info("pre to refresh singe config, configKey: {}, oldValue：{}, newValue: {}",
                configKey, EnvironmentPropertyContext.getCustomProperty(configKey), newValue);
        EnvironmentPropertyContext.setCustomProperty(configKey, newValue);
        log.info("pre to refresh singe config, configKey: {}, customPropertyValue : {}",
                configKey, EnvironmentPropertyContext.getCustomProperty(configKey));
        SimpleConfigRefreshScope.clean(configKey);
    }

}
