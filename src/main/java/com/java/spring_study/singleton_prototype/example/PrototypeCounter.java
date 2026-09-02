package com.java.spring_study.singleton_prototype.example;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prototype Scope Bean
 *
 * Spring Container에 Bean을 요청할 때마다 새로운 PrototypeCounter 객체가 생성된다.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeCounter {

    // 생성된 객체를 구분하기 위한 번호 (객체가 생성될 때 마다 1씩 증가하는 일련번호)
    private static final AtomicInteger sequence = new AtomicInteger();

    private final int instanceId;   // 각 객체의 고유 ID
    private int count;  // 각 객체가 독립적으로 가지는 카운터

    public PrototypeCounter() {
        this.instanceId = sequence.incrementAndGet();   // 생성될 때 ID 부여
    }

    public int increase() {
        return ++count;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public int getCount() {
        return count;
    }
}
