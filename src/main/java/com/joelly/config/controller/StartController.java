package com.joelly.config.controller;

import com.joelly.config.configs.TestValueConfig;
import com.joelly.config.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController extends BaseController {

    @Value("${joelly.config.enable: false}")
    private Boolean configEnable;

    @Autowired
    private TestValueConfig testValueConfig;

    @GetMapping("/hello")
    public String hello() {
        return "OK";
    }


    @GetMapping("/configEnable")
    public Boolean configEnable() {
        return configEnable;
    }

    @GetMapping("/testHxConfig")
    public String testHxConfig() {
        return "testHxConfig: " + testValueConfig.toString();
    }

}