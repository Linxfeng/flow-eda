# 设计思想

本项目的主要设计思想是采用低代码可视化拖拽编程的方式来实现一些常用的业务逻辑。
用户可以通过拖拽连接一个个功能节点来组成流程图并运行，达到实现业务功能的目的。

### 项目交互流程

用户使用本项目平台的交互流程图：

![image](../img/interaction.png ":size=60%")

通过上图可以很直观的看到用户与平台交互的整个运行过程，以及各个服务模块担任的功能职责。

- vue 和 react 为前端项目，部署后用户通过浏览器访问前端页面
- 用户登录/注册/登出等功能由 oauth2 服务提供
- 整个平台的所有接口服务等资源均由 oauth2 服务提供鉴权认证
- 用户访问平台的所有 API 接口均由 web 服务提供，用户的流程数据存储也是由 web 服务负责
- 流程运行时，由 web 服务调用 runner 服务运行流程，由 runner 服务负责执行流程的运行
- runner 服务在运行流程时会产生运行日志，通过 rabbitmq 发送到 logger 服务，同时通过 websocket 将运行信息推送到前端
- logger 服务负责收集和存储日志信息，同时也提供了查询日志的功能

### 功能节点模型设计

**1. 功能节点定义**

功能节点是将各个单独的功能逻辑封装起来的一种逻辑单元，每个功能节点都有它自己的功能属性，例如延时器节点可以延迟流程执行、输出节点可以输出参数内容等。
关于功能节点的详细定义和使用说明请查看[功能节点](getting-started/flow-node.md)。

功能节点是组成流程图的最小基本单元，一个流程图至少含有一个功能节点。将各个功能节点进行连接组合，再填写好节点参数，一个简单的流程图就绘制完成了。

**2. 对象模型设计**

功能节点的对象模型在 runner 服务中定义，主要用于流程运行时执行各个节点的逻辑功能。

在项目模块`flow-eda-runner`中，功能节点全都定义在`com.flow.eda.runner.node`包下面。我们定义了一个节点接口`Node`：

```java
public interface Node {
    /**
     * 运行当前节点实例
     *
     * @param callback 回调函数，当前节点执行完毕之后进行回调
     */
    void run(NodeFunction callback);

    /**
     * 获取当前节点的执行状态
     *
     * @return 节点状态，初始值为 Status.RUNNING
     */
    Status status();

    /** 节点的运行状态定义 */
    enum Status {
        /** 运行中 */
        RUNNING,
        /** 运行完成 */
        FINISHED,
        /** 运行失败 */
        FAILED
    }
}
```

还定义了一个节点抽象类`AbstractNode`，此类是`Node`接口的子类。

```java
/** 节点抽象类 */
public abstract class AbstractNode implements Node {
    /** 输入参数，由上个节点传递至此 */
    private final Document input;
    private final String flowId;
    private final String nodeId;
    /** 节点自定义参数，可传递至下个节点 */
    private Document payload;
    /** 节点当前的运行状态 */
    private Status status = Status.RUNNING;

    /**
     * 抽象类含参构造，用于约束子类必须有一个含参构造
     *
     * @param params 节点的输入参数，由上一个节点传递至此
     */
    public AbstractNode(Document params) {
        this.flowId = params.getString("flowId");
        this.nodeId = params.getString("nodeId");
        this.payload = params.get("payload", Document.class);
        this.input = params.get("input", Document.class);
        // 校验节点参数
        this.verify(params);
    }

    /** 节点参数校验 */
    protected abstract void verify(Document params);
}
```

项目规定所有的节点对象都必须继承此节点抽象类`AbstractNode`，便于统一运行和管理。我们以任一节点为例，查看它的依赖关系图：

![image](../img/diagrams.png ":size=40%")

由上图可以看出，每个功能节点都必须含有两个方法，其中`run`方法是节点接口的运行方法的实现，供外部调用。
`verify`方法是节点抽象类的抽象方法的实现，用于校验节点的参数。除此之外，每个功能节点都必须含有一个含参构造函数，用于将参数传递至父类抽象节点。

下面是一个最简单的节点代码示例：

```java
public class StartNode extends AbstractNode {

    public StartNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        // 设置节点运行状态为完成
        setStatus(Status.FINISHED);
        // 节点运行完之后进行回调
        function.callback(output());
    }

    @Override
    protected void verify(Document params) {}
}
```

规定所有的功能节点都必须按照这个模板的格式来开发，各节点可以在`verify`方法内校验并获取自己定义的个性化参数，在`run`方法内定义自己的运行逻辑。
这样统一规范有利于各节点的统一管理和运行，同时也便于各个节点的功能扩展和个性化开发。

### 节点运行设计

当我们开发好某个功能节点后，还需要在枚举`NodeTypeEnum`中配置该节点：

```java
public enum NodeTypeEnum {
    START("start", StartNode.class),
    OUTPUT("output", OutputNode.class),
    // ...
    ;
    private final String type;
    private final Class<? extends Node> clazz;
}
```

将我们开发的节点类型（标识）和对应的节点类配置到此枚举中，当需要运行某一节点时，可根据节点类型`type`直接获取到对应的节点类，
然后将入参传入构造函数，再通过反射，获取该节点的对象实例：

```java
public class FlowExecutor {
    /** 获取当前节点的实例 */
    private Node getInstance(FlowData currentNode) {
        try {
            Class<? extends Node> clazz = NodeTypeEnum.getClazzByNode(currentNode);
            return clazz.getConstructor(Document.class).newInstance(currentNode.getParams());
        } catch (Exception e) {
            throw FlowException.wrap(e);
        }
    }
}
```

获取到节点对象实例后，就可以调用该节点的`run`方法运行该节点了，同时在`run`方法的回调函数里接收该节点的输出参数。
这样，一个节点的运行，以及节点输入输出参数的设计处理就完成了。

### 流程运行设计

**1. 线程池管理工具**

各个流程的运行采用统一管理线程池的方式，给每个流程分配一个单独的线程池，保证各流程运行时的线程安全。
线程池管理工具`FlowThreadPool`内容如下：

```java
/** 用于执行整个流程的线程池管理工具，方便统一管理和约束线程池的创建/使用/销毁 */
public class FlowThreadPool {
    private static final Map<String, ExecutorService> POOL_MAP = new ConcurrentHashMap<>();

    public static ExecutorService getThreadPool(String flowId) {
        if (POOL_MAP.containsKey(flowId)) {
            return POOL_MAP.get(flowId);
        }
        ExecutorService threadPool = Executors.newCachedThreadPool();
        POOL_MAP.put(flowId, threadPool);
        return threadPool;
    }

    public static void shutdownThreadPool(String flowId) {
        if (POOL_MAP.containsKey(flowId)) {
            POOL_MAP.get(flowId).shutdownNow();
            POOL_MAP.remove(flowId);
        }
    }
}
```

可以看出，线程池管理工具提供了两个方法，获取线程池和关闭/销毁线程池，用于流程运行和停止运行，回收资源。
这样统一管理，可以避免造成内存泄露的风险，同时也便于统一管理和约束线程池的创建/使用/销毁。

**2. 流程运行设计**

一个流程的运行是由各个节点的单独运行连接起来的。当我们运行一个流程时，需要先找出流程中的起始节点，然后获取流程线程池，从起始节点开始，异步运行。

当流程开始运行后，先从起始节点开始，按照节点之间的连接顺序，依次向下运行，直至流程运行结束。
对于单条流程的运行我们也专门封装一个类来处理，`FlowExecutor`类内容如下：

```java
/** 单条流程的执行者 */
public class FlowExecutor {
    /** 存储当前流程的完整流程节点数据 */
    private final List<FlowData> flowData;
    /** 当前流程的id */
    private final String flowId;

    public FlowExecutor(List<FlowData> flowData) {
        this.flowData = flowData;
        this.flowId =
                Objects.requireNonNull(findFirst(flowData, n -> n.getFlowId() != null)).getFlowId();
    }

    /** 执行当前节点 */
    private void run(FlowData currentNode) {
        try {
            Node nodeInstance = getInstance(currentNode);
            // 运行节点
            nodeInstance.run(
                    (p) -> {
                        // 继续运行下一节点
                        runNext(currentNode, nodeInstance, p);
                    });
        } catch (Exception e) {
            throw FlowException.wrap(e);
        }
    }

    /** 节点数据执行后回调，继续执行下一节点 */
    private void runNext(FlowData currentNode, Node nodeInstance, Document p) {
        // 多个下游节点，需要并行执行
        List<FlowData> nextNodes = getNextNode(currentNode);
        forEach(nextNodes, n -> getThreadPool(flowId).execute(() -> this.run(setInput(n, p))));
    }

    private List<FlowData> getNextNode(FlowData currentNode) {
        List<String> ids =
                filterMap(flowData, n -> currentNode.getId().equals(n.getFrom()), FlowData::getTo);
        if (isNotEmpty(ids)) {
            return filter(flowData, n -> ids.contains(n.getId()));
        }
        return null;
    }

    /** 获取当前节点的实例 */
    private Node getInstance(FlowData currentNode) {
        try {
            Class<? extends Node> clazz = NodeTypeEnum.getClazzByNode(currentNode);
            return clazz.getConstructor(Document.class).newInstance(currentNode.getParams());
        } catch (Exception e) {
            throw FlowException.wrap(e);
        }
    }
}
```

通过上述代码可以看出，整个流程的运行逻辑，是从起始节点开始，依次运行节点。 每当一个节点运行结束时，会调用回调函数，继续运行它的下一个节点。

每当一个节点运行完成后，都会从当前流程的线程池中获取新的线程来继续运行下一节点。
若当前节点有多个下游节点，那么每个下游节点的运行都是一个单独的线程。
这样设计的目的是为了避免造成流程阻塞，保证了流程节点数据的线程安全，同时也是为了在多条下游支路的情况下能够同时运行。
