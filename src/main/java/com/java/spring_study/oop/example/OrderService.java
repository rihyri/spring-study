package com.java.spring_study.oop.example;

/**
 * 느슨한 결합 (Loose Coupling)을 확인하기 위한 예제
 *
 * OrderService는 FixedDiscountPolicy나 RatediscountPolicy와 같은 구체 구현 클래스를 직접 알지 않는다.
 * DiscountPolicy 인터페이스에만 의존하고, 실제 구현 객체는 생성자를 통해 외부에서 전달받는다.
 * */
public class OrderService {

    private final DiscountPolicy discountPolicy;

    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    // 할인 적용 후 최종 가격을 계산
    public int calculatePrice(int price) {
        int discountAmount = discountPolicy.discount(price);

        return price - discountAmount;
    }
}
