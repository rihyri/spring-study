package com.java.spring_study.singleton_prototype.example;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * ObjectProvider를 이용해 Prototype Bean을 조회하는 예제
 * 
 * getObject()를 호출할 때 Spring Container에 PrototypeCounter를 새롭게 요청
 * ObjectProvider를 사용하면 Singleton 안에서도 Prototype을 매번 새로 받을 수 있다.
 */

@Service
public class ProviderPrototypeService {

    private final ObjectProvider<PrototypeCounter> counterProvider;

    public ProviderPrototypeService(ObjectProvider<PrototypeCounter> counterProvider) {
        this.counterProvider = counterProvider; // Provider 자체를 주입
    }

    public void increase() {

        // ⭐ 메서드가 호출될 때마다 새 Prototype을 요청한다.
        PrototypeCounter prototypeCounter = counterProvider.getObject();

        int count = prototypeCounter.increase();

        System.out.printf(
                "Prototype #%d / count = %d%n",
                prototypeCounter.getInstanceId(),
                count
        );
    }
}
