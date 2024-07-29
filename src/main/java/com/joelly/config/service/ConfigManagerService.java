package com.joelly.config.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joelly.config.constants.CommonConstant;
import com.joelly.config.convert.ConfigConvert;
import com.joelly.config.dao.AppDeployMachineDao;
import com.joelly.config.dao.ConfigPropertyDao;
import com.joelly.config.dao.objs.AppDeployMachine;
import com.joelly.config.dao.objs.ConfigProperty;
import com.joelly.config.entity.vo.ConfigPropertyVO;
import com.joelly.config.loader.ApplicationConfigContext;
import com.joelly.config.loader.EnvironmentPropertyContext;
import com.joelly.config.loader.scope.SimpleConfigRefreshScope;
import com.joelly.config.service.exts.WebOpsUserService;
import com.joelly.config.utils.AssertUtils;
import com.joelly.config.utils.GsonProvider;
import com.joelly.config.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import static com.joelly.config.constants.CommonConstant.REFRESH_SINGLE_CONFIG_URL;

@Slf4j
public class ConfigManagerService implements ApplicationContextAware {


    @Value("${server.servlet.context-path:}")
    private String servletContextPath;

    @Value("${spring.mvc.servlet.path:}")
    private String springMvcServletPath;

    @Autowired
    private ConfigPropertyDao configPropertyDao;

    @Autowired
    private AppDeployMachineDao appDeployMachineDao;

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfigManagerService.context = applicationContext;
    }

    public IPage<ConfigProperty> pageQuery(Page<ConfigProperty> page, String cfgKey) {
        LambdaQueryWrapper<ConfigProperty> queryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(cfgKey)) {
            queryWrapper.eq(ConfigProperty::getCfgKey, cfgKey);
        }
        queryWrapper.orderBy(true, false, ConfigProperty::getUpdateTime);
        return configPropertyDao.selectPage(page, queryWrapper);
    }

    public boolean addConfig(ConfigPropertyVO propertyVO) {
        AssertUtils.assertNotBlank(propertyVO.getCfgKey(), "配置值不可为空");
        String env = EnvironmentPropertyContext.getSpringProfilesActive();
        String application = EnvironmentPropertyContext.getApplicationName();
        ConfigProperty dbConfig = configPropertyDao.selectByAppEnvKey(application, env, propertyVO.getCfgKey());
        AssertUtils.assertNull(dbConfig,
                String.format("新增配置的key已经在本应用（%s）本环境(%s)中存在，请直接编辑", application, env));

        String currentUser = getCurrentUser(propertyVO.getOperator());
        propertyVO.setOperator(currentUser);
        ConfigProperty configProperty = ConfigConvert.convertInsertFromVO(application, env, propertyVO);
        int insertResult = configPropertyDao.insert(configProperty);
        log.info("addConfig end, insertResult:{}, vo:{}, configProperty:{}",
                insertResult, propertyVO, GsonProvider.obj2Str(configProperty));
        // 刷新配置
        refreshConfig2EveryMachine(configProperty.getCfgKey());
        return insertResult > 0;
    }

    public boolean updateConfig(ConfigPropertyVO propertyVO) {
        AssertUtils.assertNotBlank(propertyVO.getCfgKey(), "修改的配置值不可为空");
        String env = EnvironmentPropertyContext.getSpringProfilesActive();
        String application = EnvironmentPropertyContext.getApplicationName();
        ConfigProperty dbConfig = configPropertyDao.selectByAppEnvKey(application, env, propertyVO.getCfgKey());
        AssertUtils.assertNotNull(dbConfig, String.format("配置的key已经在本应用（%s）本环境(%s)中不存在", application, env));

        String currentUser = getCurrentUser(propertyVO.getOperator());
        propertyVO.setOperator(currentUser);
        ConfigProperty configProperty = ConfigConvert.convertUpdateFromVO(application, env, propertyVO);
        int updateResult = configPropertyDao.updateByAppEnvKey(configProperty);
        AssertUtils.assertTrue(updateResult > 0, "该配置已被改变，请刷新页面后重试");
        // 刷新配置
        refreshConfig2EveryMachine(configProperty.getCfgKey());
        return true;
    }

    public boolean deleteConfig(Long id, String operator) {
        ConfigProperty configProperty = configPropertyDao.selectById(id);
        AssertUtils.assertNotNull(configProperty, String.format("该删除的配置（id=%s）不存在", id));
        int deleteNum = configPropertyDao.deleteById(id);
        String user = getCurrentUser(operator);
        log.info("deleteConfig, id = {}, deleteNum: {}, user: {}, configProperty: {}",
                id, deleteNum, user, GsonProvider.obj2Str(configProperty));
        return deleteNum > 0;
    }

    public Set<String> configUseClass(String cfgKey) {
        return SimpleConfigRefreshScope.getInstance().queryAllUseClass(cfgKey);
    }

    private String getCurrentUser(String webParamUser) {
        try {
            WebOpsUserService userService = context.getBean(WebOpsUserService.class);
            if (userService != null) {
                return userService.getCurrentUserId();
            }
        } catch (Exception e) {
            log.warn("get UserService error, webParamUser:{}", webParamUser, e);
        }
        if (StringUtils.isNotBlank(webParamUser)) {
            return webParamUser;
        }
        return CommonConstant.DEFAULT_USER;
    }

    public void refreshConfig2EveryMachine(String configKey) {
        QueryWrapper<AppDeployMachine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("env", EnvironmentPropertyContext.getSpringProfilesActive())
                .eq("application", EnvironmentPropertyContext.getApplicationName());
        List<AppDeployMachine> lists = appDeployMachineDao.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(lists)) {
            log.error("refreshConfig2EveryMachine end, AppDeployMachine is empty");
            return;
        }
        String serverPort = EnvironmentPropertyContext.getServerPort();
        HttpHeaders headers = copyHttpHeaders();
        for (AppDeployMachine appDeployMachine : lists) {
            try {
                String ip = appDeployMachine.getIp();
            if (ip.equals(ApplicationConfigContext.getLocalIp())) {
                ip = "127.0.0.1";
//                refreshConfigService.refreshSingeConfig(configKey);
//                return;
            }
                String url = String.format(REFRESH_SINGLE_CONFIG_URL, ip, serverPort, getUrlPrefix(), configKey);

                String httpResult = HttpUtil.postForObject(url, null, headers);
                log.info("refreshConfig2EveryMachine end, machine:{}, result:{}", appDeployMachine, httpResult);
            } catch (Exception e) {
                log.error("refreshConfig2EveryMachine error, machine:{}", appDeployMachine, e);
            }
        }
    }

    private HttpHeaders copyHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes == null) {
            return headers;
        }
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        if (httpServletRequest == null) {
            return headers;
        }
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            headers.set(headerName, headerValue);
        }
        return headers;
    }

    private String getUrlPrefix() {
        if (StringUtils.isNotBlank(servletContextPath) && StringUtils.isNotBlank(springMvcServletPath)) {
            return servletContextPath.trim() + springMvcServletPath.trim();
        } else if (StringUtils.isNotBlank(servletContextPath)) {
            return servletContextPath.trim();
        } else if (StringUtils.isNotBlank(springMvcServletPath)) {
            return springMvcServletPath.trim();
        }
        return Strings.EMPTY;

    }



}
