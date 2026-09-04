package com.java.spring_study.aop.example;

import org.springframework.stereotype.Service;

/**
 * 결제와 관련된 핵심 비즈니스 로직
 *
 * OrderService와 전혀 다른 클래스이지만 동일한 @TrackExecutionTime을 사용하면 같은 AOP 기능을 적용할 수 있다.
 */
@Service
public class PaymentService {

    @TrackExecutionTime
    public void pay(String orderId, int amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("결제 금액은 0원보다 커야 합니다.");
        }

        simulateWork(120);

        System.out.printf(
                "[PaymentService] %s / %,d원 결제 완료%n",
                orderId,
                amount
        );
    }

    private void simulateWork(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();;
        }
    }
}
