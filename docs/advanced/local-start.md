# 本地启动项目

想要在本地启动项目，需要先安装项目所依赖的一些中间件服务，然后下载项目源码，构建

### 安装中间件服务

本项目依赖的中间件服务有`Mysql`、`RabbitMQ`、`Nacos`，无论是服务器上安装还是本机安装都可以，只要这些中间件服务能够正常连接使用即可。

> 考虑到广大开发者朋友们的本地调试便捷性，可以切换到分支 feign-replace-dubbo，
> 该分支使用了 Feign 代替 Dubbo 的技术方案，可以不用安装 Nacos 服务，方便本地运行和调试。
> 有条件的情况下还是推荐使用 master 分支，安装 Nacos 服务作为注册中心。

由于网上资料比较丰富且详细，这里不再赘述，下面提供了几个博客可供参考：

- [Windows 安装 Mysql](https://blog.csdn.net/weixin_43423484/article/details/124408565)
- [Windows 安装 RabbitMQ](https://blog.csdn.net/qq_25919879/article/details/113055350)
- [Windows 安装 Nacos](https://blog.csdn.net/qq_43518425/article/details/124577232)
- [Linux 安装 RabbitMQ](https://blog.csdn.net/Lin_xiaofeng/article/details/87857536)
- [Linux 安装 Mysql](https://blog.csdn.net/Lin_xiaofeng/article/details/87628833)
- [Linux 安装 Nacos](https://blog.csdn.net/Mr_7777777/article/details/123133036)

另外，本项目文档提供了 docker 部署中间件的详细过程，可供参考：

- [docker 部署](deploy/docker.md)
- [docker-compose 部署](deploy/docker-compose.md)

### 本地构建项目

我们需要将项目的源码拉取到本地，可根据自身网络情况选择 Github 还是 Gitee 平台拉取代码。

```shell
# 从Github上拉取项目源码
git clone https://github.com/Linxfeng/flow-eda

# 从Gitee上拉取项目源码
git clone https://gitee.com/Linxff/flow-eda
```

源码拉取下来之后，使用你常用的编辑器 IDE 打开项目源码，这里我们使用 IDEA 作为演示，其他编辑器类似。

使用我们的编辑器 IDEA 打开项目，配置 JDK，推荐使用 java8 或以上，使用`maven`构建项目，耐心等待项目构建完毕。

对于前端项目，我们这里推荐也使用 IDEA 构建，当然你使用 VS 或其他编辑器都是可以的。
下面我们用`flow-eda-vue`前端项目举例，对于`flow-eda-react`前端项目也是一样的，不再赘述了。

打开我们编辑器的`Terminal`，进入前端项目目录下，执行`npm install`初始化安装前端项目依赖。

```shell
cd flow-eda-vue
npm install
```

等待前端项目依赖安装完毕，后端项目构建好之后，当前项目的本地构建就完成了。

### 本地启动项目
