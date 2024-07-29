package com.joelly.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joelly.config.dao.objs.AppDeployMachine;
import org.apache.ibatis.annotations.Insert;

public interface AppDeployMachineDao extends BaseMapper<AppDeployMachine> {

    @Insert({"<script>",
            "INSERT INTO hx_app_deploy_machine (application, env, ip)",
            "VALUES (#{application}, #{env}, #{ip})",
            "ON DUPLICATE KEY UPDATE update_time = now()",
            "</script>"})
    int insertOrUpdate(AppDeployMachine appDeployMachine);
}
