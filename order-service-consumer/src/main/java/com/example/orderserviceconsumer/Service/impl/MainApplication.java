package com.example.orderserviceconsumer.Service.impl;

import Service.OrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author SHshuo
 * @data 2022/6/27--13:41
 */
public class MainApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        OrderService bean = context.getBean(OrderService.class);
        bean.initOrder("1");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
