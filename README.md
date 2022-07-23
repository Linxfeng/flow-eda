# flow-eda

### 项目简介

flow-eda 项目是一种基于事件驱动的流式低代码编程应用程序，它的主要功能是采用可视化编程，以拖拽节点、连接组合节点的形式来完成流程绘制，达到低代码开发和实现业务编程的目的。
更多详细内容请查看[项目文档](https://linxfeng.github.io/flow-eda)

此分支使用 Feign 替代了 Dubbo+Nacos 的技术方案，方便用户本地调试。使用此分支无需安装 Nacos，本地启动应用仅需要 Mysql 数据库和 RabbitMQ 服务即可，各服务之间采用 Feign 进行 http 调用。

欢迎各路大神共同参与开发，项目持续扩展完善中

#### 项目名称

- **flow**: 流程图、流式编程
- **eda**: 事件驱动应用程序 (Event Driven Application)
- **flow-eda**: 一种基于事件驱动的流式低代码编程应用程序

### 项目特点

- 整个流程运行引擎全都在后端实现，前端非常轻量，所有的数据、参数、包括输入框等数据全由后端配置提供，前端仅负责根据数据展示。后期开发仅扩展后端功能即可，前端无需更改，自动兼容。
- 流程运行引擎做到模块化、配置化。后期在进行功能扩展和开发时，仅需要针对新增的功能，新增配置和新增模块功能代码即可，运行引擎自动适配。
- 后端设计采用微服务架构，各个模块之间按业务功能相互解耦。便于后期二次开发和功能扩展。
- 代码风格严格按照规范执行，后端代码遵循 Alibaba 规范，代码格式使用 google-java-format 格式化，前端代码使用 Prettier 格式化，消除代码中的标黄警告等，做到编码规范化。

### 在线演示

作者本人自费租了云服务器，搭建了在线演示 DEMO，开源不易，请珍惜服务器资源，感谢！

Vue3 版本[在线 demo](http://120.48.9.40:80)

React 版本[在线 demo](http://120.48.9.40:90)

喜欢请点点 star，谢谢^.^

### 代码仓库

- [GitHub](https://github.com/Linxfeng/flow-eda)
- [Gitee](https://gitee.com/Linxff/flow-eda)

### 项目展示

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/flows.gif)

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/logs.gif)

**Vue3 版本界面：**

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/vue3.png)

**React 版本界面：**

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/react.png)

**编辑器界面：**

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/editor.png)

期待您的参与，项目持续扩展完善中...

### 项目模块

- **flow-eda-common** 公共工具模块
- **flow-eda-runner** 流程运行引擎模块
- **flow-eda-web** 后台管理 web 模块
- **flow-eda-logger** 日志管理模块
- **flow-eda-oauth2** 认证鉴权模块
- **flow-eda-vue** 前端 vue 模块
- **flow-eda-react** 前端 react 模块

### 主要功能

- 支持用户登录和注册，接口鉴权、数据隔离
- 流程管理、日志管理。菜单简洁，便于用户使用
- 支持绘制任意的流程图，没有繁琐的条件约束，仅针对某些节点的必填参数做了校验
- 各功能节点地位平等，不分头尾，可任意连接绘制，连接数量无上限
- 支持用户自定义参数，使用占位符${}即可获取参数值，由上至下可无限传递，可在任意位置取值
- 支持在绘制流程图时使用常用快捷键对节点进行操作
- 流程图支持导入/导出功能，可以快速的导入绘好的流程并进行修改
- 可实时查看流程运行状态变化，便于用户了解流程运行至哪一步，以及各节点当前的状态、错误信息等
- 支持查看流程实时运行日志功能，可查看实时运行日志和历史运行日志，内含各个节点的输入输出参数
- 支持并发，流程采用并行运行的方式，会自动从流程中找出所有起始节点同时开始运行，数据隔离，互不干扰
- 提供了大量的流程图示例可供参考，每个功能节点都有对应的流程图示例

更多功能，等着你发现！

### 系统架构图

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/architecture.png)

### 后端技术栈

| 技术            | 版本     | 说明                 |
| --------------- | -------- | -------------------- |
| Docker          | 20.10.14 | 应用容器引擎         |
| Docker Compose  | 2.5.0    | 应用容器部署工具     |
| Spring Boot     | 2.6.4    | 微服务框架           |
| Mybatis         | 3.5.9    | ORM 框架             |
| Mysql           | 8.0.28   | 数据库               |
| Maven           | 3.6.3    | 项目构建管理工具     |
| PageHelper      | 5.3.0    | MyBatis 物理分页插件 |
| Lombok          | 1.18.22  | 代码插件             |
| Nacos           | 2.0.4    | 服务注册中心         |
| Dubbo           | 3.0.7    | 服务远程调用         |
| WebSocket       | 9.0.58   | 数据推送             |
| RabbitMQ        | 3.9.15   | 消息队列             |
| Spring Security | 5.6.2    | 认证和授权框架       |
| Security Oauth2 | 2.3.6    | 认证和授权框架       |

### 项目部署

#### Docker 部署

部署 mysql

```shell script
docker run --privileged=true -d -v /root/app/mysql/data/:/var/lib/mysql -v /root/app/mysql/conf.d:/etc/mysql/conf.d -v /root/app/mysql/my.cnf:/etc/mysql/my.cnf -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql
```

部署 rabbitmq

```shell script
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq -v /root/app/rabbitmq/data:/var/lib/rabbitmq rabbitmq
# 开启后台管理页面的插件（可选）
docker exec -it rabbitmq bash
rabbitmq-plugins enable rabbitmq_management
exit
```

部署 nacos

```shell script
docker run -d -e MODE=standalone -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=host.docker.internal -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=123456 -e MYSQL_SERVICE_DB_NAME=nacos_config -e JVM_XMS=256m -e JVM_XMX=256m -e JVM_XMN=256m -p 8848:8848 -p 9848:9848 -p 9849:9849 --privileged=true --name nacos nacos/nacos-server
```

部署 springboot 应用

```shell script
# 打包镜像
sh build.sh
# 启动应用
sh start.sh
```

#### docker-compose 部署

```shell script
# 打包镜像
sh build.sh
# 创建容器，在`docker-compose.yml`文件路径下执行
docker-compose up -d
```

#### 无注册中心部署

```shell script
# 切换分支
git checkout feign-replace-dubbo
# 打包镜像
sh build.sh
# 创建容器，在`docker-compose.yml`文件路径下执行
docker-compose up -d
```

### 待开发项

- [ ] 由于流程是并行运行，而且非阻塞节点运行速度很快，这就导致使用 websocket 在同一个 session 下推送消息会报错，目前采用的是加锁的形式避免问题，后期考虑使用 EMQX 替代 websocket 进行消息推送
- [x] 新增 oauth 模块，用于鉴权认证，整个项目需要实现鉴权和数据隔离
- [x] 考虑到广大开发者朋友们的本地调试便捷性，使用了 Feign 代替 Dubbo，去除注册中心 nacos，减少部署占用，方便本地运行和调试，切换到分支 feign-replace-dubbo 即可
- [ ] 由于部署在线 demo 云服务器的资源限制（穷！），本项目全部线上部署目前占用总内存 4G 左右，导致很多中间件引入进来后服务器资源不够部署，后期考虑等服务器资源足够后，引入 redis 做分布式缓存，将 mysql 进行读写分离，引入 Prometheus+Grafana 实现项目监控，采用 k8s 部署等一系列优化（等买的起新的服务器再说-.-）

PS：由于现有的服务器资源已经无法支撑后续开发的项目部署，如果你觉得此项目对您有帮助，可以进行 [捐赠](https://gitee.com/Linxff/flow-eda#%E4%BA%A4%E6%B5%81%E7%BE%A4) ，捐赠所得会全部用于项目开发，感谢！

### 交流群

若需要技术支持或者想进行技术交流，可以扫码添加本人微信或者微信群，也可以进群提开发要求，我会尽可能进行扩展开发。欢迎加群进行技术交流！

![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/weixin.jpg)
![image](https://gitee.com/Linxff/flow-eda/raw/master/docs/img/group.jpg)
