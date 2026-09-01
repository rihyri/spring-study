package com.java.spring_study.lifecyclescope.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/**
 * Singleton Scope (기본값) : Spring Container가 생성도리 때 단 한 번 객체가 생성되고, Container가 종료될 때까지 같은 인스턴스를 계속 사용
 */
@Component  // Spring이 관리하는 빈으로 등록
public class SingletonLifecycleBean {

    public SingletonLifecycleBean() {
        System.out.println("[Singleton] 객체 생성");
    }

    @PostConstruct  // 의존성 주입이 끝난 후 자동으로 실행되는 메서드 (초기화 용도)
    public void init() {
        System.out.println("[Singleton] @PostConstruct - 초기화");
    }

    public void use() {
        System.out.println("[Singleton] Bean 사용");
    }

    @PreDestroy  // Bean이 소멸되기 직전에 자동으로 실행되는 메서드 (정리 용도)
    public void destroy() {
        System.out.println("[Singleton] @PreDestroy - 소멸");
    }
}
