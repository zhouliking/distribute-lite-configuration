# simple-distribute-config-starter

简单轻量级分布式配置中心，适用于：分布式应用、单体应用、微服务应用等spring web项目，直接引入web工程即可使用。提供工程启动前（sping 容器启动前）加载配置，实时配置管理生效等功能

## Getting started
> 使用步骤，如下：
- Step1: 添加maven依赖
```
<dependency>
    <groupId>com.joelly.config.config</groupId>
    <artifactId>distribute-lite-configuration-starter</artifactId>
    <version>1.0.0</version>
</dependency
```

- Step2: 添加yaml配置
```
spring:
  application:
    name: xxx-app

joelly:
  config:
    enable: true
    refreshRateSecond: 60 // 循环兜底刷新配置时间，单位秒，保证最终一致性
    db:
      url: jdbc:mysql://xx.xx.xx.xx:3311/config_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&max_query_size=100000000
      username: root
      password: 123456

# 若工程中无数据库配置，则如下配置
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://xx.xx.xx.xx:3311/config_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&max_query_size=100000000
    username: root
    password: 123456

```

- Step3: 添加mysql表
```mysql
CREATE TABLE `simple_config_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application` varchar(63) NOT NULL COMMENT '应用名',
  `env` varchar(63) NOT NULL COMMENT '环境',
  `cfg_key` varchar(127) NOT NULL COMMENT '配置key',
  `cfg_value` varchar(1023) NOT NULL COMMENT '配置值',
  `version` int NOT NULL DEFAULT 1 COMMENT '版本信息',
  `created_by` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(128) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_env_key` (`application`, `env`, `cfg_key`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='配置表'



CREATE TABLE `simple_config_property_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application` varchar(63) NOT NULL COMMENT '应用名',
  `env` varchar(63) NOT NULL COMMENT '环境',
  `cfg_key` varchar(127) NOT NULL COMMENT '配置key',
  `cfg_value` varchar(1023) NOT NULL COMMENT '配置值',
  `version` int NOT NULL DEFAULT 1 COMMENT '版本信息',
  `created_by` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(128) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_env_key_version` (`application`, `env`, `cfg_key`, `version`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='配置表历史记录表'


CREATE TABLE `simple_app_deploy_machine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application` varchar(63) NOT NULL COMMENT '应用名',
  `env` varchar(63) NOT NULL COMMENT '环境',
  `ip` varchar(127) NOT NULL COMMENT '机器ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_env_ip` (`application`, `env`, `ip`)  
```

- Step4: 使用
```
@Component
@SimpleConfigRefresh
@Getter
public class TestConfig {

    @Value("${test.concatJavaStraceSwitch:true}")
    private boolean concatJavaStraceSwitch;

}

```

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin http://xx.xx.xx.xx:9090/xxx/xxx-config-starter.git
git branch -M master
git push -uf origin master
```

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***
## Suggestions for a good README
Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
