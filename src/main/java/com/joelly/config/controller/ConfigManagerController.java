package com.joelly.config.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joelly.config.controller.base.BaseController;
import com.joelly.config.controller.respose.R;
import com.joelly.config.dao.objs.ConfigProperty;
import com.joelly.config.entity.vo.ConfigPropertyVO;
import com.joelly.config.service.ConfigManagerService;
import com.joelly.config.utils.GsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
@ConditionalOnProperty(name = "joelly.config.enable", havingValue = "true")
public class ConfigManagerController extends BaseController {

    @Autowired
    private ConfigManagerService configManagerService;

    @GetMapping("/propertyPage")
    public R<IPage<ConfigProperty>> propertyPage(Page<ConfigProperty> page,
                                                 @RequestParam(value = "cfgKey", required = false) String cfgKey) {
        log.info("/config/propertyPage, cfgKey: {}", cfgKey);
        return success(configManagerService.pageQuery(page, cfgKey));
    }

    @PostMapping("/addConfig")
    public R<Boolean> propertyPage(@RequestBody ConfigPropertyVO propertyVO) {
        log.info("/config/addConfig, propertyVO: {}", GsonProvider.obj2Str(propertyVO));
        return success(configManagerService.addConfig(propertyVO));
    }

    @PostMapping("/updateConfig")
    public R<Boolean> updateConfig(@RequestBody ConfigPropertyVO propertyVO) {
        log.info("/config/updateConfig, propertyVO: {}", GsonProvider.obj2Str(propertyVO));
        return success(configManagerService.updateConfig(propertyVO));
    }

    @PostMapping("/deleteConfig")
    public R<Boolean> updateConfig(@RequestParam(value = "id", required = true) Long id,
                                   @RequestParam(value = "operator", required = false) String operator) {
        log.info("/config/deleteConfig, id: {}", id);
        return success(configManagerService.deleteConfig(id, operator));
    }

    @GetMapping("/configUseClass")
    public R<Set<String>> configUseClass(@RequestParam(value = "cfgKey", required = true) String cfgKey) {
        log.info("/config/configUseClass, cfgKey: {}", cfgKey);
        return success(configManagerService.configUseClass(cfgKey));
    }

}
