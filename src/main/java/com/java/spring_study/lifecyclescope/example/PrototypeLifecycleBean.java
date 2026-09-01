package com.java.spring_study.lifecyclescope.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Prototype Scope : getBean()을 호출할 때마다 새로운 객체를 생성해서 반환
 * Prototype Bean은 생성과 초기화(@PostConstruct)까지만 Spring이 관리하고, 그 이후의 생명주기(소멸)는 Spring이 관리하지 않는다.
 * 따라서 @PreDestroy가 실행되지 않는다.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 프로토타입 지정
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
