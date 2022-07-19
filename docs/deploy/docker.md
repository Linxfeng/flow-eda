# Docker 部署

部署项目之前，需要先将项目所依赖的中间件服务部署好，正常运行后，再部署项目。

> 本项目部署的所有服务都在`/root/app/`目录下，你也可以对应修改为你自己的目录。

### 部署 mysql

我们在`/root/app/`目录下新建一个`mysql`目录，然后在 mysql 目录下分别新建`data`目录和`conf.d`目录以及`my.cnf`文件。

```shell
cd /root/app
mkdir mysql
cd mysql
mkdir data
mkdir conf.d
touch my.cnf
```

其中，`my.cnf`文件用于配置数据库，其内容为：

```shell
[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
authentication_policy= mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000
secure_file_priv=/var/lib/mysql
skip-name-resolve=1

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
default-time-zone='+8:00'
```

然后我们执行 docker 命令，启动 mysql 服务。此处我们设置的 root 用户默认密码为 123456，你也可以根据自身需求进行修改。

```shell
docker run --privileged=true -d -v /root/app/mysql/data/:/var/lib/mysql -v /root/app/mysql/conf.d:/etc/mysql/conf.d -v /root/app/mysql/my.cnf:/etc/mysql/my.cnf -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql
```

执行成功后，可以使用`docker ps -a`命令查看容器运行状态。

你可以使用 Navicat 等 mysql 客户端工具进行连接，若连接成功，则 mysql 服务部署成功，正常运行。

> 注意服务器防火墙等因素可能会导致客户端无法连接的情况。

### 部署 rabbitmq

我们在`/root/app/`目录下新建一个`rabbitmq`目录，然后在 rabbitmq 目录下新建`data`目录。此 data 目录用于存储 rabbitmq 服务的数据。

```shell
cd /root/app
mkdir rabbitmq
cd rabbitmq
mkdir data
```

然后我们执行 docker 命令，启动 rabbitmq 服务。

```shell
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq -v /root/app/rabbitmq/data:/var/lib/rabbitmq rabbitmq
```

执行成功后，可以使用`docker ps -a`命令查看容器运行状态。

若需要开启 rabbitmq 的后台管理页面插件，则可以执行以下命令

```shell
docker exec -it rabbitmq bash
rabbitmq-plugins enable rabbitmq_management
exit
```

开启成功后，就可以使用 ip:15672 访问后台管理页面了，默认的用户名密码为 guest/guest。

> 注意若服务器有防火墙，则需要开放对应端口。

### 部署 nacos

若项目依赖了 nacos 服务作为服务注册中心，则需要部署 nacos 服务。

> 本项目提供了无注册中心部署，可以切换到项目分支 feign-replace-dubbo 进行打包部署，则不需要部署 nacos 服务。
> 有条件的情况下还是推荐使用 master 分支，部署 nacos 服务。

部署 nacos 服务稍微繁琐一点，主要是因为它需要连接到 mysql 数据库，便于持久化存储数据。

我们在部署 nacos 之前，需要先连接到我们的 mysql 数据库，创建一个名为`nacos_config`的数据库，然后导入 sql 文件。
本项目下载了 sql 文件，在项目`flow-eda-common`中，`sql/nacos_config.sql`文件。

创建好数据库，成功导入 sql 文件后，我们执行 docker 命令，启动 nacos 服务。
注意修改命令中的`MYSQL_SERVICE_HOST`和`MYSQL_SERVICE_PASSWORD`的值，分别为你的 mysql 服务的 ip 地址和 root 用户的密码。

```shell script
docker run -d -e MODE=standalone -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=host.docker.internal -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=123456 -e MYSQL_SERVICE_DB_NAME=nacos_config -e JVM_XMS=256m -e JVM_XMX=256m -e JVM_XMN=256m -p 8848:8848 -p 9848:9848 -p 9849:9849 --privileged=true --name nacos nacos/nacos-server
```

执行成功后，可以使用`docker ps -a`命令查看容器运行状态。若遇到部署问题，可以查看[官方 issues](https://github.com/alibaba/nacos/issues)寻找类似的问题和解决方案。

### 打包 Docker 镜像

我们可以将项目的 jar 包打包成 docker 镜像，便于进行 docker 部署。项目的 jar 包来源详见[项目打包](deploy/packaging.md)。

我们在`/root/app/`目录下新建一个`springboot`目录，在 springboot 目录下新建一个`logger`目录，
在 logger 目录下新建一个`logs`目录，用于存放项目的日志文件。

```shell
cd /root/app
mkdir springboot
cd springboot
mkdir logger
cd logger
mkdir logs
cd /root/app/springboot
```

然后我们回到`springboot`目录中，将打包好的项目 jar 包上传至此目录下。
再将 flow-eda 项目根目录下的`Dockerfile`文件和`build.sh`文件上传至 springboot 目录中。

![image](../img/docker1.png ":size=60%")

我们项目打包使用的基础镜像是`anapsix/alpine-java`镜像，我们在打包镜像前需要先拉取该镜像，避免拉取镜像时间过长导致项目打包失败。
执行命令拉取基础镜像：

```shell
docker pull anapsix/alpine-java
```

基础镜像拉取成功后，我们执行命令`sh build.sh`开始打包 docker 镜像，此命令可以将项目的 jar 包打包为`docker image`。

```shell
sh build.sh
```

打包完成后，使用命令`docker images`查看镜像列表。

```shell
docker images
```

![image](../img/docker2.png ":size=60%")

### 部署后端应用

当后端项目所依赖的中间件服务部署完成后，我们就可以开始部署后端服务了。
