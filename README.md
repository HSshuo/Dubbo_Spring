# Dubbo_Spring

<br>

### 微服务介绍

#### 微服务的核心是远程通信和服务治理。
- 远程通信提供了服务之间通信的桥梁；
- 服务治理则提供了服务的后勤保障。  

<br>

#### 微服务架构中的服务通信多数是基于RPC通信实现
- Springcloud基于Feigh组件实现RPC通信（**HTTP短连接 + Json序列化**）；
- Dubbo基于SPI拓展了很多PRC通信框架，包括RMI（**TCP协议**）、Dubbo（**单一长连接**）、Hessian（**HTTP短连接**）等通信框架（**默认是Dubbo协议（单一长连接）+ Hessian序列化**）；
- Dubbo通信可以支持抢购类的高并发，在这个业务场景中，请求的特点是瞬时高峰、请求量大和传入、传出参数数据包较小；

<br>

#### RPC
- [RPC](https://blog.nowcoder.net/n/42ee6a2d05a34618813148ab0e783eef)
- [RMI机制](https://github.com/HSshuo/RMI)

<br>
<br>

### Dubbo
- 是一款高性能、轻量级的开源Java RPC框架，提供了三大核心能力：面向接口的远程方法调用、智能容错和负载均衡、服务自动注册和发现。

#### dubbo原理
![image](https://github.com/HSshuo/PictureBed/blob/main/dubbo.png)

初始化的时候已经进行：
1. Provider：提供者，将服务暴露（@DubboService），启动时将服务注册到Registry注册中心；
2. Consumer：消费者，调用服务（@Reference），向Registry注册中心订阅服务，也可以**直接通过Dubbo暴露提供者的地址**。默认服务容错是Failover Cluster失败自动切换，配合设置重试次数使用retries；
3. Registry：注册中心，**用于发布和订阅服务的平台**，用于替代SOA结构体系框架中的ESB服务总线。提供给消费者服务者提供的服务，如果有变更，会基于长连接推送变更数据给消费者；

使用：

4. invoke：调用，消费者调用提供者提供的服务；

异步执行：

5. Monitor：监控中心，将消费者，提供者在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。

<br>

##### 注册中心
1. Zookeeper（支持分布式，有广泛的周边开源产品；但是依赖于Zookeeper的稳定性）
2. Redis（支持基于客户端双写的集群方式、性能高；但是要求服务器时间同步，用于检查心跳过期脏数据）
3. Multicast
4. Simple

###### 服务治理 
Service服务下线怎么办？
- 心跳、健康检测

注册中心是可选的？
- 直接在配置文件里面写url、实现提供方和消费方直连

注册中心宕机了怎么办？
- 不会影响已经正常运行的提供者和消费者，因为消费者有本地缓存提供者的信息

<br>
<br>

#### Dubbo框架

#### Dubbo分层架构

总体分三层，Business（业务层）、RPC层、Remoting

![image](https://user-images.githubusercontent.com/70870058/180641979-b76cbaa8-cb2a-4dcd-8161-d4bf810204e6.png)

细分有10层：
1. Service 接口层：给服务提供者和消费者来实现
2. Config 配置层：主要对Dubbo进行各种配置，以SeviceConfig、ReferenceConfig 为中心，可以直接初始化配置类，也可以通过Spring解析配置生成配置类
3. Proxy 服务代理层：生成服务客户端Stub、服务端Skeleton，代理之间进行网络通信，做远程调用和返回结果（以ServiceProxy为中心，拓展接口为ProxyFactory）
4. Registry 注册中心层：负责服务的注册与发现，（以服务URL为中心，拓展接口为RegistryFactory、Registry、RegistryService）
5. Cluster 路由层：封装多个服务提供者的路由以及负载均衡，（以Invoker为中心，扩展接口为Cluster、Directory、Router、LoadBalance）
6. Monitor 监控层：对RPC调用次数和调用时间监控
7. Protocol 远程调用层：封装RPC的调用，（以Invocation、Result为中心，扩展接口为 Protocol、Invoker、Exporter）
8. exchange 信息交换层：封装请求响应模式，同步转异步，（以Request、Response为中心，扩展接口为Exchanger、ExchangeChannel、ExchangeClient、ExchangeServer）
9. transport 网络传输层：抽象了网络传输的统一接口，可以使用mina或者 netty
10. serialize 数据序列化层：数据序列化成二进制流，二进制流反序列化成数据

![image](https://user-images.githubusercontent.com/70870058/180195049-3aa1e1ca-b770-4168-8942-08179816d586.png)

<br>
<br>

#### dubbo提供的服务
1. 服务治理（服务注册和服务发现）：可以用zookeeper、或者dubbo直连（@Reference（url = ""））；
2. 服务调用：dubbo协议+Hessian序列化；
3. 监控中心：使用dubbo-monitor-simple查看情况；
4. 服务降级：使用dubbo-admin，操作服务降级；统一配置<dubbo:provider cluster = "" /> **<** 接口配置<dubbo:service />、<dubbo:reference /> **<** 方法配置<dubbo:method />；
6. 负载均衡：使用dubbo-admin，操作可以增加权重；

<br>


##### 服务降级、服务容错、负载均衡
- [Dubbo 集群容错、负载均衡](https://blog.nowcoder.net/n/078d7469b60f4fc78066519d506a027c)

<br>
<br>

#### Dubbo调用过程
- [Dubbo服务暴露](https://blog.nowcoder.net/n/5bda4e8f3f654c47a5bd5d12f972c9bc)
- [Dubbo服务消费](https://blog.nowcoder.net/n/b28f6c311ac146ab81439495b67781ed)
- [Dubbo服务调用](https://blog.nowcoder.net/n/ff7fdfa2606e4577b2285e3fad928fca)

<br>
<br>

#### dubbo特性
##### 面向接口代理的高性能RPC调用
- 服务以接口为粒度，为开发者屏蔽远程调用底层细节

##### 智能负载均衡
- 内置多种负载均衡策略，智能感知下游节点健康状况，显著减少调用延迟，提高系统吞吐量

##### 服务自动注册与发现
- 支持多种注册中心服务，服务实例上下线实时感知

##### 运行期流量调度
- 内置条件、脚本等路由策略，通过配置不同的路由规则，轻松实现灰色发布，同机房优先等功能

##### 可视化的服务治理与运维
- 提供丰富服务治理、运维工具：随时查询服务元数据、服务健康状态及调用统计，实时下发路由策略

##### 高度可扩展能力
- Dubbo：采用微内核架构，遵循 微内核 + 插件 的设计模式，微内核只负责组装插件，插件可以拓展系统的功能；
- 通常情况下，微内核都会采用Factory、IOC、OSGI等方式管理插件声明周期。Dubbo采用Dubbo SPI的机制，支持AOP、IOC实现管理插件。

![微内核](https://user-images.githubusercontent.com/70870058/180193771-598412fc-a2d1-4fc0-9484-3b34e705f90a.png)

###### 参考
- [Dubbo拓展性](https://dubbo.apache.org/zh/docs3-v2/java-sdk/concepts-and-architecture/overall-architecture/)
- [SPI](https://github.com/HSshuo/SPI)

<br>
<br>

#### dubbo注解

##### dubbo对于注解的解析
类似于dubbo.application、registry、protocol等通过**BeanDefinitionParser接口解析**，调用具体的DubboBeanDefinitionParser，逐一解析。

<br>

##### 常见的注解提供的属性
@DubboService 暴露服务
- version 版本号 - 灰色发布
- weight 权重 - 负载均衡
- retries 重试次数 - 容错机制
- timeout 超时时间

@Reference 订阅服务
- version 版本号 - 灰色发布
- url 服务直连
- timeout 超时时间
- retries 重试次数

<br>
<br>

### dubbo常见问题

##### dubbo配置生效层级
统一配置<dubbo:provider cluster = "" /> **<** 接口配置<dubbo:service />、<dubbo:reference /> **<** 方法配置<dubbo:method />；

<br>

##### dubbo超时时间（timeout）的生效层级
- 精确优先（根据方法配置、接口配置、统一配置）；
- 同一级别下，消费者优先。

<br>

##### zookeeper宕机，还能否远程调用？
- 可以，因为服务提供者和服务消费者仍可以通过本地缓存通讯。

<br>

##### 本地存根的作用？
- 用于做数据校验，在调用远程接口前处理，之后进行序列化通过网络协议传送数据。

<br>

##### @EnableDubbo的作用？
- 主要目的是开启包扫描的功能。

<br>

##### Java SPI 及 Dubbo SPI
- [SPI](https://github.com/HSshuo/SPI)

<br>
<br>

### 拓展
- 使用XML配置dubbo命名空间出现问题：[通配符的匹配很全面, 但无法找到元素'dubbo:application'](https://blog.csdn.net/Ciel_Y/article/details/118895806)
- springcloud：![image](https://github.com/HSshuo/PictureBed/blob/main/springcloud.png)

### 参考
- [b站周瑜SPI机制讲解](https://www.bilibili.com/video/BV1zp4y1q7fg?spm_id_from=333.1007.top_right_bar_window_custom_collection.content.click&vd_source=211430be5830488947b1b481d9e2794c)
- [官网 Dubbo架构](https://dubbo.apache.org/zh/docs3-v2/java-sdk/concepts-and-architecture/code-architecture/)
- [敖丙 Dubbo系列](https://github.com/AobingJava/JavaFamily)

