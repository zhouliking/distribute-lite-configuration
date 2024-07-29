package com.joelly.config.controller;

import com.joelly.config.controller.base.BaseController;
import com.joelly.config.controller.respose.R;
import com.joelly.config.service.RefreshConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@ConditionalOnProperty(name = "joelly.config.enable", havingValue = "true")
public class ConfigRefreshController extends BaseController {

    @Autowired
    private RefreshConfigService refreshConfigService;

    @PostMapping("/refreshOne")
    public R<Boolean> refreshOne(@RequestParam("configKey") String configKey) {
        log.info("refresh refreshOne, configKey: {}", configKey);
        refreshConfigService.refreshSingeConfig(configKey);
        return success(true);
    }



}
