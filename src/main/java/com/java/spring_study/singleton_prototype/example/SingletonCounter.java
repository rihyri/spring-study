package com.java.spring_study.singleton_prototype.example;

import org.springframework.stereotype.Component;

/**
 * Singleton Scope Bean
 *
 * 별도의 @Scope를 지정하지 않았기 때문에 Spring의 기본 Scope인 Singleton으로 관리된다.
 * 하나의 객체를 여러 곳에서 공유하므로 객체가 가진 상태(count)도 함께 공유된다.
 */
@Component
public class SingletonCounter {

    // 모든 사용자가 공유
    private int count;

    public int increase() {
        return ++count;
    }

    public int getCount() {
        return count;
    }
}
