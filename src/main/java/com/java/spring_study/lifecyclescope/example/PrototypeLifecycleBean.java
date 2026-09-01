package com.java.spring_study.lifecyclescope.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeLifecycleBean {

    public PrototypeLifecycleBean() {
        System.out.println("[Prototype] 객체 생성");
    }

    @PostConstruct
    public void init() {
        System.out.println("[Prototype] @PostConstruct - 초기화");
    }

    public void use() {
        System.out.println("[Prototype] Bean 사용");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[Prototype] @PreDestroy - 소멸");
    }
}
