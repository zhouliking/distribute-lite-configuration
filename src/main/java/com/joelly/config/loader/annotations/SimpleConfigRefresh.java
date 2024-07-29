package com.joelly.config.loader.annotations;

import com.joelly.config.loader.scope.SimpleConfigRefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(SimpleConfigRefreshScope.SCOPE_NAME)
public @interface SimpleConfigRefresh {
    /**
     * 定义代理模式，默认为 TARGET_CLASS
     * @return ScopedProxyMode
     */
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;
}
