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

#### dubbo原理
- [dubbo原理](https://blog.nowcoder.net/n/04049c7d718b4e069a89fddff824e0d4)

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

