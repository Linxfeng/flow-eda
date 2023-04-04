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
线程池管理工具`com.flow.eda.runner.runtime.FlowThreadPool`内容如下：

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
对于单条流程的运行我们也专门封装一个类来处理，`com.flow.eda.runner.runtime.FlowExecutor`类内容如下：

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
            nodeInstance.run(p -> this.runNext(currentNode, nodeInstance, p));
        } catch (Exception e) {
            throw FlowException.wrap(e);
        }
    }

    /** 执行下一节点 */
    private void runNext(FlowData currentNode, Node nodeInstance, Document p) {
        // 多个下游节点，需要并行执行
        List<FlowData> nextNodes = getNextNode(currentNode);
        forEach(nextNodes, n -> getThreadPool(flowId).execute(() -> this.run(setInput(n, p))));
    }

    /** 获取当前节点的下游节点 */
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

### 流程状态更新

**1. 流程状态说明**

当用户在前端页面上开始运行一个流程后，前端就需要实时获取该流程的运行状态，以此来判断该展示运行按钮还是停止按钮。
总的来说，流程的状态分为两种：可运行、可停止。

实际上，针对不同的服务来说，流程状态略有差异。对于 runner 运行引擎，流程的运行状态分为 3 种：运行中、运行完成、运行失败；
对于 web 服务，流程状态分为：未运行、运行中、运行完成、运行失败。
web 服务多出来一个未运行的状态，这是由于用户新建了流程之后还未画流程图或从未运行过，故而使用未运行状态来标记该流程。
对于前端来说，除了运行中状态可停止流程，其余状态均可运行流程。

**2. 计算流程运行状态**

当一个流程开始运行时，我们就需要开始实时计算该流程的运行状态，并将其通过 websocket 推送至前端。
计算流程的运行状态服务我们写在 runner 运行引擎中，这是由于流程的运行状态是由多个节点运行状态决定的。
我们可以根据每个节点的运行状态更新来刷新流程运行状态，具体实现逻辑写在`com.flow.eda.runner.status.FlowStatusService`类中。

```java
/** 流程状态服务，主要负责实时计算流程运行状态并监控其状态变更 */
@Service
public class FlowStatusService {
    /** 正在运行的节点 */
    private final Map<String, Set<String>> runningMap = new ConcurrentHashMap<>();

    public void startRun(String flowId, List<FlowData> starts, List<FlowData> timer) {
        this.runningMap.put(flowId, new ConcurrentHashSet<>());
        forEach(starts, node -> this.runningMap.get(flowId).add(node.getId()));
        forEach(timer, node -> this.runningMap.get(flowId).add(node.getId()));
    }

    /** 实时计算流程状态 */
    public String getFlowStatus(String flowId, Document message) {
        String nodeId = message.getString("nodeId");
        if (nodeId == null) {
            return flowInfoService.getFlowStatus(flowId);
        }
        String status = message.getString("status");
        if (Node.Status.FAILED.name().equals(status)) {
            runningMap.get(flowId).remove(nodeId);
            return status;
        } else if (Node.Status.FINISHED.name().equals(status)) {
            runningMap.get(flowId).remove(nodeId);
            if (runningMap.get(flowId).isEmpty()) {
                return status;
            }
        } else {
            runningMap.get(flowId).add(nodeId);
        }
        return Node.Status.RUNNING.name();
    }

    /** 判断当前流程状态是否已完成 */
    public boolean isFinished(String flowId) {
        if (!runningMap.containsKey(flowId)) {
            String status = flowInfoService.getFlowStatus(flowId);
            return Node.Status.FINISHED.name().equals(status);
        }
        return runningMap.get(flowId).isEmpty();
    }
}
```

通过代码我们可以看到，计算流程的实时运行状态并不复杂，通过节点的运行状态可很便捷地计算出流程的运行状态。

- 若节点运行失败，则当前流程状态为运行失败
- 若节点运行中，则当前流程状态为运行中
- 若节点运行完成，检查发现还有待运行的节点，则当前流程状态为运行中
- 若节点运行完成，检查待运行的节点为空（所有节点都运行完了），则当前流程状态为运行完成

在运行引擎中，每当一个节点的运行状态更新时，都会向前端推送一条 websocket 消息，
消息内容包含了当前节点的最新状态信息（若当前节点发生异常，则也会包含异常信息），我们在每次推送消息前，调用一下上述代码的`getFlowStatus`方法，
即可实时获取当前流程的运行状态，可合并到消息内容中一并推送至前端，这样就实现了向前端推送流程实时运行状态的功能。

**3. 流程状态同步**

我们知道流程在运行之后，会将流程运行状态实时推送到前端，由前端进行处理展示。
那如果前端页面关闭了呢？还有 web 服务获取流程数据时，数据库中的流程状态要如何保证正确性？这些问题都可以通过同步流程状态来解决。

我们在每个节点的运行状态更新时，会实时计算一次当前流程的运行状态，在我们向前端推送节点状态信息时，会同时向 RabbitMQ 中发送消息。

```java
public class FlowNodeWebsocket {
    /** 推送节点状态信息 */
    public void sendMessage(String flowId, Document message) {
        // 获取流程实时状态信息一起推送
        String flowStatus = message.getString("flowStatus");
        if (flowStatus == null) {
            flowStatus =
                    ApplicationContextUtil.getBean(FlowStatusService.class)
                            .getFlowStatus(flowId, message);
            message.append("flowStatus", flowStatus);
        }
        if (SESSION_POOL.get(flowId) != null) {
            try {
                synchronized (SESSION_POOL.get(flowId)) {
                    SESSION_POOL.get(flowId).getBasicRemote().sendText(message.toJson());
                }
            } catch (Exception e) {
                log.error("Send websocket message failed:{}", e.getMessage());
            }
        }
        // 向mq中推送流程的实时运行状态信息
        ApplicationContextUtil.getBean(FlowStatusMqProducer.class)
                .sendFlowStatus(flowId, flowStatus);
        if (Node.Status.FINISHED.name().equals(flowStatus)) {
            FlowLogs.info(flowId, "flow {} run finished", flowId);
        }
    }
}
```

这样一来，每次节点运行状态更新时，都会将状态信息推送到 RabbitMQ 中，保证了流程状态同步的消息投递。那么我们还需要在 web 服务里接收并处理这些消息。

在 web 服务中，接收 RabbitMQ 消息很简单，关键在于如何处理这些流程状态信息。由于节点运行速度非常快，这就可能导致会在极短的时间内收到大量的流程状态信息。
而如此短暂且多量的信息，是不可能每次都去更新数据库的。那么我们采用只更新最后收到的消息行不行？答案是不行，
因为无法预料是否还会继续收到消息，也无法知道下一条消息什么时候来。由于本项目暂时没有引入 Redis 作为应用缓存，这就为更新数据库中的流程状态带来了困难。

本项目采用了看门狗用于监听流程状态变化，在`com.flow.eda.web.flow.status.FlowStatusWatchDog`类中

```java
/** 看门狗，用于监听流程状态变化 */
public class FlowStatusWatchDog {
    /** 刷新周期(秒) */
    private static final long SLEEP = 2;
    /** key为流程id，流程状态更新时会更新对应的value值为true */
    private static final Map<String, Boolean> MAP = new ConcurrentHashMap<>();

    /** 刷新看门狗 */
    public static void refresh(String flowId, Consumer<String> callback) {
        if (MAP.isEmpty()) {
            MAP.put(flowId, true);
            watch(callback);
        } else {
            MAP.put(flowId, true);
        }
    }

    /** 监视对应的流程是否进行刷新，若固定周期内未刷新，则踢出map并进行回调 */
    private static void watch(Consumer<String> callback) {
        Runnable runnable =
                () -> {
                    while (!MAP.isEmpty()) {
                        MAP.keySet()
                                .forEach(
                                        flowId -> {
                                            if (MAP.get(flowId)) {
                                                MAP.put(flowId, false);
                                            }
                                        });
                        try {
                            TimeUnit.SECONDS.sleep(SLEEP);
                        } catch (InterruptedException e) {
                            LogAspect.error(e.getMessage());
                        }
                        MAP.keySet()
                                .forEach(
                                        flowId -> {
                                            if (!MAP.get(flowId)) {
                                                MAP.remove(flowId);
                                                callback.accept(flowId);
                                            }
                                        });
                    }
                };
        new Thread(runnable).start();
    }
}
```

每当 web 服务接收到 RabbitMQ 中的状态信息时，就去刷新一下看门狗，这样一来，当超过看门狗固定的刷新周期还未收到新消息时，我们就可以进行数据库状态更新了。
具体的实现逻辑在`com.flow.eda.web.flow.status.FlowStatusService`类中

```java
@Service
public class FlowStatusService {
    private final Map<String, String> statusMap = new HashMap<>();
    @Autowired private FlowMapper flowMapper;

    /** 刷新缓存中的流程状态 */
    public void update(Document payload) {
        String flowId = payload.getString("flowId");
        String status = payload.getString("status");
        statusMap.put(flowId, status);
        // 刷新看门狗
        FlowStatusWatchDog.refresh(flowId, this::updateStatus);
    }

    /** 将当前流程的状态更新到数据库 */
    private void updateStatus(String flowId) {
        if (statusMap.containsKey(flowId)) {
            String status = statusMap.get(flowId);
            flowMapper.updateStatus(flowId, status);
        }
    }
}
```

可以看到，我们使用看门狗机制，很好的解决了上述短暂且大量消息的数据更新问题。

这样的设计方案完美的解决了流程运行引擎和 web 服务以及数据库之间的流程状态同步问题。
同时也避免了频繁更新数据库的情况，虽然会带来短暂的数据延迟，但对于流程列表中的数据来说，短暂的延迟也是可以接受的；
同时对于前端来讲，流程状态是实时推送的，并不会受到任何影响。

### 停止流程运行

从设计角度思考一下，当一个流程运行起来之后，我们要如何去停止它？

实际上，停止一个正在运行的流程实现起来会有些复杂，举个例子，你运行的流程中含有一个延时器节点，延迟一个小时，当前流程正好执行到这里，
卡在延时器节点处于等待中，此时，你想停止当前流程的运行，要怎么实现？
若换成是 WS 服务端等类似的[阻塞节点](getting-started/flow-node?id=节点性质类型)呢，要如何停止？

答案显而易见，我们除了要统一管理所有负责节点运行的线程之外，我们还需要统一管理那些可能会卡状态的节点。
前面在[流程运行设计](advanced/design-philosophy?id=流程运行设计)里面已经介绍了统一管理线程池的方案，这里再介绍一下阻塞节点的统一管理方案。
在`com.flow.eda.runner.runtime.FlowBlockNodePool`类中

```java
/** 用于统一管理流程中的所有阻塞节点 */
public class FlowBlockNodePool {
    private static final Map<String, List<BlockNode>> POOL = new ConcurrentHashMap<>();

    public static void addBlockNode(String flowId, BlockNode blockNode) {
        synchronized (POOL) {
            if (POOL.containsKey(flowId)) {
                List<BlockNode> list = POOL.get(flowId);
                // 若判断两个阻塞节点相同，需要进行替换更新
                list.removeIf(node -> node.eq(blockNode));
                list.add(blockNode);
            } else {
                List<BlockNode> list = new ArrayList<>();
                list.add(blockNode);
                POOL.put(flowId, list);
            }
        }
    }

    public static void shutdownBlockNode(String flowId) {
        if (POOL.containsKey(flowId)) {
            synchronized (POOL) {
                POOL.get(flowId).forEach(BlockNode::destroy);
                POOL.remove(flowId);
            }
        }
    }

    /** 阻塞节点需要实现此接口，重写销毁方法 */
    public interface BlockNode {
        void destroy();

        /** 提供比较两个阻塞节点是否相同的扩展方法 */
        default boolean eq(BlockNode blockNode) {
            return false;
        }
    }
}
```

可以看到类中定义了一个阻塞节点接口`BlockNode`，当某个节点为阻塞节点时，需要实现该接口，重写`destroy`方法，定义该节点的中断和销毁逻辑，
然后交给`FlowBlockNodePool`类统一管理。这样我们就实现了阻塞节点的统一管理方案，当停止流程时，就可以调用一些统一的停止/销毁方法来停止流程的运行。

```java
public class FlowDataRuntime {
    /** 停止流程 */
    public void stopFlowData(String flowId) {
        // 停止流程中运行节点的普通线程
        FlowThreadPool.shutdownThreadPool(flowId);
        // 停止定时任务线程
        FlowThreadPool.shutdownSchedulerPool(flowId);
        // 停止/销毁阻塞节点
        FlowBlockNodePool.shutdownBlockNode(flowId);
    }
}
```

### 清理流程缓存

当流程运行结束或流程停止运行后，需要对流程运行时产生的一些线程池、集合容器等缓存数据进行清理。
若不及时清理这些数据，一方面是占用不必要的内存空间，可能会造成内存泄露等问题；
另一方面是可能会引起流程数据的线程安全等问题。那么我们如何清理，什么时候清理呢？

当流程运行结束后，会通知到 web 服务，并进行数据库的状态更新。
我们为了保证数据一致性，选择在更新数据库之后，调用清理流程接口进行流程缓存数据清理。
在`com.flow.eda.web.flow.status.FlowStatusService`类中

```java
public class FlowStatusService {
    private final Map<String, String> statusMap = new HashMap<>();

    /** 将当前流程的状态更新到数据库 */
    private void updateStatus(String flowId) {
        if (statusMap.containsKey(flowId)) {
            // 将流程状态更新到数据库
            flowMapper.updateStatus(flowId, status);
            // 如果流程已执行完毕，则需要清理缓存数据
            if (!Flow.Status.RUNNING.name().equals(status)) {
                statusMap.remove(flowId);
                flowDataService.clearFlowData(flowId);
            }
        }
    }
}
```

此处调用了 runner 服务的清理方法，具体实现在类`com.flow.eda.runner.runtime.FlowDataRuntime`中

```java
public class FlowDataRuntime {
    /** 清理流程运行的缓存数据 */
    public void clearFlowData(String flowId) {
        // 销毁流程中运行节点的普通线程
        FlowThreadPool.shutdownThreadPool(flowId);
        // 销毁定时任务线程
        FlowThreadPool.shutdownSchedulerPool(flowId);
        // 销毁阻塞节点数据
        FlowBlockNodePool.shutdownBlockNode(flowId);
        // 清理状态服务的集合容器
        flowStatusService.clear(flowId);
    }
}
```

此清理缓存数据的方法与停止流程的方法逻辑类似，但是还有一些业务上的区别，如 websocket 消息推送等等，故而将它们分开。

这样设计的好处除了可以保证数据一致性之外，无论是手动停止流程，还是流程自己运行结束，都可以执行到清理方法。
