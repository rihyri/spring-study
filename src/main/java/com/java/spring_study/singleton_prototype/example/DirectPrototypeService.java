package com.java.spring_study.singleton_prototype.example;

import org.springframework.stereotype.Service;

/**
 * Singleton Bean에 Prototype Bean을 직접 주입
 *
 * Prototype이라고 해서 메서드를 호출할 때마다 새로운 객체가 생성되는 것은 아니다.
 * DirectPrototypeService는 Singleton이므로 생성 시점에 PrototypeCounter를 한 번 주입받고 그 객체를 계속 사용한다.
 */
@Service
public class DirectPrototypeService {

    private final PrototypeCounter prototypeCounter;    // Prototype을 필드로 주입

    public DirectPrototypeService(PrototypeCounter prototypeCounter) {
        this.prototypeCounter = prototypeCounter;   // 생성 시 한 번만 주입
    }

    public void increase() {
        int count = prototypeCounter.increase();

        System.out.printf(
                "Prototype #%d / count = %d%n",
                prototypeCounter.getInstanceId(),
                count
        );
    }
}
