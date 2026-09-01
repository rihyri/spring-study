package com.java.spring_study.lifecyclescope.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class SingletonLifecycleBean {

    public SingletonLifecycleBean() {
        System.out.println("[Singleton] 객체 생성");
    }

    @PostConstruct
    public void init() {
        System.out.println("[Singleton] @PostConstruct - 초기화");
    }

    public void use() {
        System.out.println("[Singleton] Bean 사용");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[Singleton] @PreDestroy - 소멸");
    }
}
