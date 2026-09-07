package com.java.spring_study.iocdi;

/**
 * 할인 정책을 이용해 주문 금액을 계산하는 서비스
 *
 * OrderService는 구체적인 할인 정책을 직접 생성하지 않고
 * DiscountPolicy를 생성자를 통해 전달받는다.
 */
public class OrderService {

    private final DiscountPolicy discountPolicy;

    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public int calculatePrice(int price) {
        int discountAmount = discountPolicy.discount(price);
        return price - discountAmount;
    }
}
