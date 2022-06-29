# Dubbo_Spring



### 微服务介绍
#### 微服务的核心是远程通信和服务治理。
- 远程通信提供了服务之间通信的桥梁；
- 服务治理则提供了服务的后勤保障。  
#### 微服务架构中的服务通信多数是基于RPC通信实现
- Springcloud基于Feigh组件实现RPC通信（**基于http协议（短连接）+Json序列化**）；
- Dubbo基于SPI拓展了很多PRC通信框架，包括RMI、Dubbo、Hessian等通信框架（**默认是Dubbo协议（单一长连接）+Hessian序列化**）；
- Dubbo通信可以支持抢购类的高并发，在这个业务场景中，请求的特点是瞬时高峰、请求量大和传入、传出参数数据包较小；
#### RPC
- 概念：远程服务调用，是通过网络请求远程计算机程序服务的通信技术。RPC框架封装好了底层网络通信、序列化等技术，我们只需要在项目中引入各个服务的接口包，就可以实现在代码中调用RPC服务同调用本地方法一样。正因为这种方便、透明的远程调用，RPC被广泛应用与当下企业级以及互联网项目中，是实现分布式系统的核心。
- 种类：springcloud Feigh、gRPC、thrift、RMI、dubbo、HadoopRPC。
- 核心（重点围绕）：**通信协议和序列化（xml、json、protobuf、thrift）**。
- 原理：Client（调用） --> ClientStub（序列化） 《==（网络传输）==》 ServerStub（反序列化）--> Server（处理）

![image](https://github.com/HSshuo/PictureBed/blob/main/rpc.png)



### Dubbo
#### dubbo原理
![image](https://github.com/HSshuo/PictureBed/blob/main/dubbo.png)

初始化的时候已经进行：
1. Provider：提供者，将服务暴露（@DubboService），启动时将服务注册到Registry注册中心；
2. Consumer：消费者，调用服务（@Reference），向Registry注册中心订阅服务，也可以直接通过Dubbo直接提供者暴露地址。默认服务容错是Failover Cluster失败自动切换，配合设置重试次数使用retries；
3. Registry：注册中心，提供给消费者服务者提供的服务，如果有变更，会基于长连接推送变更数据给消费者；

使用：

4. invoke：调用，消费者调用提供者提供的服务；

异步执行：

5. Monitor：监控中心，将消费者，提供者在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。


#### dubbo框架


#### dubbo提供的服务
##### 服务治理（服务注册和服务发现）：可以用zookeeper、或者dubbo直连（@Reference（url = ""））；
##### 服务调用：dubbo协议+Hessian序列化；
##### 监控中心：使用dubbo-monitor-simple查看情况；
##### 服务降级：使用dubbo-admin，操作服务降级；
概念：当服务器压力剧增的情况下，根据实际业务情况以及流量对一些服务和页面有策略的不处理或者换种简单的方式处理，从而释放一些服务器资源来保证核心交易正常运作和高效运作。

种类：
1. mock=force:return null：表示消费者对该服务方法调用直接返回null值，**不发起远程调用**。用来屏蔽不重要的服务**不可用**时对调用方的影响；
2. mock=fail:return null：表示消费者对该服务方法调用在失败后，再返回null值，不抛出异常。用来容忍不重要服务**不稳定**时对调用方的影响。

##### 服务容错：统一配置<dubbo:provider cluster = "" />配置 < 接口配置 < 方法配置；

种类：
1. Failover cluster：失败自动切换，一般用在触发**重试**的时候retries；重试要注意**幂等性**如果幂等的时候可以不断重试（查询、修改、删除），非幂等操作不可以重试（新增）；
2. Failfast cluster：快速失败，只发起一次调用，失败后立即报错，用于**非幂等**操作，比如新增记录；
3. Failsafe cluster：失败安全，出现异常时，直接忽略。通常用于**审计日志**等操作；
4. Failback cluster：失败自动恢复，后台记录失败请求，定时重发。用于**消息通知**操作；
5. Forking cluster：只要一个成功即返回，通常用于**实时性要求较高的读**操作，但浪费更多服务资源；
6. Broadcast cluster：广播所有提供者，逐一调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或者日志等本地资源操作，也就是**每台服务器上的信息要求一致**。

##### 负载均衡：使用dubbo-admin，操作可以增加权重；
1. 基于权重的随机负载均衡机制（Random LoadBalance）**默认**;
2. 基于权重的轮询负载均衡机制（RoundRobin LoadBalance）；
3. 最少活跃数负载均衡机制（LeastActive LoadBalance）；最少活跃数根据的是上一次服务器处理请求花费的时间；
4. 一致性hash负载均衡机制（ConsistentHash LoadBalance）；可以保证高可用，当一台服务器宕机后可以均匀的将服务的请求快速的迁移到其他几台机子上分摊压力。（类似于getUser?id = * 一致的id访问一样的服务）。


#### dubbo配置生效层级
统一配置<dubbo:provider cluster = "" />配置 **<** 接口配置<dubbo:service />、<dubbo:reference /> **<** 方法配置<dubbo:method />；


#### dubbo超时时间（timeout）的生效层级
- 精确优先（根据方法配置、接口配置、统一配置）；
- 同一级别下，消费者优先。


#### dubbo对于注解的解析
类似于dubbo.application、registry、protocol等通过**BeanDefinitionParser接口解析**，调用具体的DubboBeanDefinitionParser，逐一解析。


#### dubbo常见问题
zookeeper宕机，还能否远程调用？
- 可以，因为服务提供者和服务消费者仍可以通过本地缓存通讯。


本地存根的作用？
- 用于做数据校验，在调用远程接口前处理，之后进行序列化通过网络协议传送数据。


@EnableDubbo的作用？
- 主要目的是开启包扫描的功能。



### 拓展
- 使用XML配置dubbo命名空间出现问题：[通配符的匹配很全面, 但无法找到元素'dubbo:application'](https://blog.csdn.net/Ciel_Y/article/details/118895806)
- springcloud：![image](https://github.com/HSshuo/PictureBed/blob/main/springcloud.png)



