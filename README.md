# flow-eda### 介绍此分支使用Feign替代了dubbo+nacos的技术方案，方便用户本地调试。使用此分支无需安装nacos，本地启动应用仅需要mysql数据库和rabbitmq服务即可，各服务之间采用Feign进行http调用。**说明：** 本分支与master的区别仅仅是使用Feign替代了dubbo+nacos的服务调用方案，其他代码完全一致。**项目名称介绍：**- flow: 流式编程- eda: 事件驱动应用程序(Event Driven Application)**项目特点：** - 整个运行引擎全都在后端实现，前端非常轻量，所有的数据、节点、节点类型、功能、参数等，全由后端提供，前端仅负责根据数据展示。后期开发仅扩展后端功能即可，前端自动兼容。- 后端各个模块之间相互解耦，运行引擎负责流程运行，web模块仅负责提供前端API接口和数据交互，日志模块仅负责日志采集和展示。便于后期继续开发和扩展。- 代码风格严格按照规范执行，后端代码遵循Alibaba规范，代码格式使用google-java-format格式化，前端代码使用Prettier格式化，消除代码中的标黄警告等，做到编码规范化。**在线DEMO：**作者本人自费租了云服务器，搭建了在线DEMO，开源不易，请珍惜服务器资源，感谢！Vue3版本[在线demo](http://120.48.9.40:80)React版本[在线demo](http://120.48.9.40:90)喜欢请点点star，谢谢^.^**代码仓库：**- [GitHub](https://github.com/Linxfeng/flow-eda)- [Gitee](https://gitee.com/Linxff/flow-eda)**项目展示：**![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/gif/flows.gif)![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/gif/logs.gif)**Vue3版本界面：**![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/img/vue3.png)**React版本界面：**![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/img/react.png)**编辑器界面：**![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/img/editor.png)期待您的参与，项目持续开发中...### 项目模块说明1. flow-eda-common 公共工具模块2. flow-eda-runner 流运行引擎模块3. flow-eda-web    后台管理web模块4. flow-eda-logger 日志管理模块5. flow-eda-oauth2 认证鉴权模块6. flow-eda-vue    前端vue模块7. flow-eda-react  前端react模块### 后端技术栈- 部署：docker，版本20.10.14- 部署：docker-compose，版本2.5.0- 数据库：mysql，版本8.0.28- 微服务框架：springboot，版本2.6.4- 持久化框架：mybatis，版本2.2.2- 代码插件：lombok，版本1.18.22- 服务注册中心：nacos，版本2.0.4- 服务远程调用：dubbo，版本3.0.7- 数据推送：websocket，版本2.6.4- 消息队列：rabbitmq，版本3.9.15- 认证鉴权：security-oauth2，版本2.3.6### 支持的功能节点**基础**- [x] start节点，是触发流程执行的起始节点- [x] timer节点，定时器，定时触发流程，支持周期触发和cron表达式触发，可指定执行次数，可输出指定格式的时间戳- [x] delay节点，延时器，用于延迟指定时间后再执行其下游节点- [x] output节点，输出节点，用于输出上游节点传递下来的输出参数- [x] condition节点，条件节点，用于判断条件是否满足，可使用多个此节点组成与或等逻辑判断场景**解析**- [x] json解析器，用于解析上游节点的输出参数，获取用户需要的参数信息**网络**- [x] HTTP请求节点，可发送HTTP请求，支持herder、token等请求头，支持url带参数，支持由上游节点输入的变量- [x] HTTP响应节点，可自定义http服务，指定http参数和响应内容，可根据请求参数动态响应数据- [x] WebSocket服务端，可自定义路径创建WebSocket服务，自定义发送消息内容和发送时机，收到客户端消息后会向下游输出- [x] WebSocket客户端，可连接WebSocket服务，自定义发送消息内容和发送时机，收到服务端消息后会向下游输出- [x] MQTT订阅节点，订阅指定topic中的消息，接收到消息后输出- [x] MQTT发布节点，可向指定topic中发送消息，发送的消息内容会输出**数据库**- [x] mysql节点，可连接mysql数据库，执行自定义sql语句，支持任意类型的多条sql语句，输出执行结果和内容- [x] mongodb节点，可连接mongodb数据库，执行自定义命令语句，输出执行结果和内容- [x] redis节点，可连接redis服务器，执行自定义操作，输出执行结果和内容欢迎任何有想法的朋友提issue，或者加入底部的微信交流群，我尽可能地扩展功能节点。持续开发中...### 主要功能点- 支持绘制任意的流程图，没有繁琐的约束，仅针对不同节点的参数做了校验- 各节点地位平等，不分头尾，可任意连接绘制，连接数量无上限，仅对节点的功能做了区分- 节点支持自定义参数，支持使用占位符${}获取参数值，由上至下可无限传递，可在任意位置取值- 绘制流程图支持常用快捷键，可使用快捷键对节点进行操作- 流程图支持导入/导出功能，可以快速的导入绘好的流程并进行修改- 支持查看实时运行状态变化，可在页面上实时看到流程运行至哪一步，以及各节点当前的状态、错误信息等- 查看实时运行日志功能，可在流程运行时查看日志，内含各个节点的输入输出参数- 流程运行时，支持停止运行功能，可立即停止当前流程的运行- 流程采用并行运行的方式，会自动从流程中找出所有起始节点开始并行运行，数据隔离，互不干扰- 日志管理功能，采集了所有的运行日志和操作日志，用户可在页面上查看日志内容- 用户数据权限隔离，资源权限认证，提供了大量的流程图示例可供参考更多功能，等着你发现！### 项目部署#### Docker 部署部署mysql```shell scriptdocker run --privileged=true -d -v /root/app/mysql/data/:/var/lib/mysql -v /root/app/mysql/conf.d:/etc/mysql/conf.d -v /root/app/mysql/my.cnf:/etc/mysql/my.cnf -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql```部署rabbitmq```shell scriptdocker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq -v /root/app/rabbitmq/data:/var/lib/rabbitmq rabbitmq# 开启后台管理页面的插件（可选）docker exec -it rabbitmq bashrabbitmq-plugins enable rabbitmq_managementexit```部署nacos```shell scriptdocker run -d -e MODE=standalone -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=host.docker.internal -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=123456 -e MYSQL_SERVICE_DB_NAME=nacos_config -e JVM_XMS=256m -e JVM_XMX=256m -e JVM_XMN=256m -p 8848:8848 -p 9848:9848 -p 9849:9849 --privileged=true --name nacos nacos/nacos-server```部署springboot应用```shell script# 打包镜像sh build.sh# 启动应用sh start.sh```#### docker-compose 部署```shell script# 打包镜像sh build.sh# 创建容器，在`docker-compose.yml`文件路径下执行docker-compose up -d```#### 无注册中心部署```shell script# 切换分支git checkout feign-replace-dubbo# 打包镜像sh build.sh# 创建容器，在`docker-compose.yml`文件路径下执行docker-compose up -d```### 待开发项- [ ] 由于流程是并行运行，而且非阻塞节点运行速度很快，这就导致使用websocket在同一个session下推送消息会报错，目前采用的是加锁的形式避免问题，后期考虑使用EMQX替代websocket进行消息推送- [x] 新增oauth模块，用于鉴权认证，整个项目需要实现鉴权和数据隔离- [x] 考虑到广大开发者朋友们的本地调试便捷性，使用了Feign代替Dubbo，去除注册中心nacos，减少部署占用，方便本地运行和调试，切换到分支feign-replace-dubbo即可- [ ] 由于部署在线demo云服务器的资源限制（穷！），本项目全部线上部署目前占用总内存4G左右，导致很多中间件引入进来后服务器资源不够部署，后期考虑等服务器资源足够后，引入redis做分布式缓存，将mysql进行读写分离，引入Prometheus+Grafana实现项目监控，采用k8s部署等一系列优化（等买的起新的服务器再说-.-）PS：由于现有的服务器资源已经无法支撑后续开发的项目部署，如果你觉得此项目对您有帮助，可以进行 [捐赠](https://gitee.com/Linxff/flow-eda#%E4%BA%A4%E6%B5%81%E7%BE%A4) ，捐赠所得会全部用于项目开发，感谢！### 交流群若需要技术支持或者想进行技术交流，可以扫码添加本人微信或者微信群，也可以进群提开发要求，我会尽可能进行扩展开发。欢迎进行技术交流！![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/img/weixin.jpg)![image](https://gitee.com/Linxff/flow-eda/raw/master/flow-eda-common/img/group.jpg)