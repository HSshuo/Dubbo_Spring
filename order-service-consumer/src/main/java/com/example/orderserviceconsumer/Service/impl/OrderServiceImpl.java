package com.example.orderserviceconsumer.Service.impl;

import Service.OrderService;
import Service.UserService;
import bean.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SHshuo
 * @data 2022/6/24--17:26
 *
 * 1、将服务提供者注册到注册中心
 *    1.1、导入dubbo依赖
 *    1.2、配置服务提供者
 * 2、让服务消费者去注册中心订阅服务提供者的服务地址
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserService service;
    
    @Override
    public void initOrder(String userId) {
//        1、查询用户的信息，需要调用另一个服务，service就是服务暴露的接口，已经声明可以远程调用
        List<UserAddress> userAddressList = service.getUserAddressList(userId);
        System.out.println(userAddressList);

    }
}