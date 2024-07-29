package com.joelly.config.service;

import com.joelly.config.dao.AppDeployMachineDao;
import com.joelly.config.dao.objs.AppDeployMachine;
import com.joelly.config.loader.ApplicationConfigContext;
import com.joelly.config.loader.EnvironmentPropertyContext;
import com.joelly.config.utils.GsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class DeployMachineManagerService {

    @Autowired
    private AppDeployMachineDao appDeployMachineDao;

    @PostConstruct
    public void init() {
        // 获取本地机器的所有网络接口
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        // 获取IP地址字符串
        String ipAddress = ip.getHostAddress();
        ApplicationConfigContext.setLocalIp(ipAddress);
        AppDeployMachine appDeployMachine = new AppDeployMachine();
        appDeployMachine.setApplication(EnvironmentPropertyContext.getApplicationName());
        appDeployMachine.setEnv(EnvironmentPropertyContext.getSpringProfilesActive());
        appDeployMachine.setIp(ipAddress);

        int dbResult =  appDeployMachineDao.insertOrUpdate(appDeployMachine);
        log.info("ConfigManagerService dbResult: {}, appDeployMachine: {}",
                dbResult, GsonProvider.obj2Str(appDeployMachine));
    }
}
