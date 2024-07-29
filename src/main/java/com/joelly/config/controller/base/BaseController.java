package com.joelly.config.controller.base;

import com.joelly.config.controller.respose.IErrorCode;
import com.joelly.config.controller.respose.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/joelly/config")
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BaseController() {
    }

    protected <T> R<T> success(T data) {
        return R.ok(data);
    }

    protected <T> R<T> failed(String msg) {
        return R.failed(msg);
    }

    protected <T> R<T> failed(IErrorCode errorCode) {
        return R.failed(errorCode);
    }
}
