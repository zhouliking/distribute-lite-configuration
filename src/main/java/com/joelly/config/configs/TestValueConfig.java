package com.joelly.config.configs;

import com.joelly.config.loader.annotations.SimpleConfigRefresh;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SimpleConfigRefresh
@Getter
@ToString
public class TestValueConfig {

    @Value("${joelly.test.bool:true}")
    private boolean boolSwitch;

    @Value("${joelly.test.string:a}")
    private String stringValue;

}
