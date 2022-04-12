# flow-eda

#### 介绍
一个由Java语言开发的基于事件驱动的流式低代码编程应用程序，您可以在编辑器中采用拖拽的形式来实现业务编程工作，一键部署，支持自定义组件和功能扩展。让开发和部署工作变得更简单高效。

项目名称介绍：
- flow: 流式编程
- eda: 事件驱动应用程序(Event Driven Application)

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

#### 模块说明

1.  flow-eda-common 公共工具模块
2.  flow-eda-runner 流运行引擎模块
3.  flow-eda-web 后台管理页面的所有web接口模块
4.  flow-eda-vue 前端模块

#### 支持的功能节点

1.  start节点，是触发流程执行的起始节点
2.  end节点，标志着流程结束，会进行资源释放
3.  HTTP节点，发送HTTP请求，支持herder、token等请求头，支持url带参数，支持url中带上由上游节点输入的变量
4.  timer节点，定时器，定时触发流程，支持周期触发（cron表达式暂未支持）

#### TODO功能点列表

1.  全局异常封装定义和处理--已完成
2.  所有非web服务的API接口均加上权限，仅后台微服务可调用--改为使用dubbo调用
3.  新增节点类型时，新增接口过滤type字段设置为自定义
4.  仅可删除自定义的节点类型，系统内置节点类型不可删除
5.  节点类型表，添加一个type字段的唯一索引，每一个type都要去节点类型枚举中进行维护
6.  前端所有的API接口请求时，统一封装一个异常处理和返回值处理，封装所有API接口超时的统一弹框提醒
7.  所有节点数据在进行保存时，后端需要统一进行节点数据校验，包括必填性校验和合法性校验
8.  后端表的id全部用8位字符串，尽量避免使用自增数字id带来的安全隐患(先评估是否存在安全隐患)
9.  每个流程中的定时器节点在执行的时候都是单独的一个定时器线程去跑，所以需要限制含有定时器的流程数量
10.  前端bug：查看流程时加载页面会被缓存，再打开新的流程页面时，不会重新加载页面

#### 运行引擎功能点

1.  保存流数据后，立即开始执行具有定时器作为起始节点的流程--已完成
2.  保存流数据后，发起执行请求后，则会触发开始节点执行流程--已完成




