package com.acc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan({"com.acc.work.mapper", "com.acc.system.mapper"})
public class AccApplication {

    public static boolean DEBUG = true;

    public static void main(String[] args) {
        SpringApplication.run(AccApplication.class, args);
    }

}
