package com.java.spring_study.oop.example;

/**
 * 고정 금액 할인 정책
 * 상품 가격과 관계 없이 최대 1,000원을 할인
 **/
public class FixedDiscountPolicy implements DiscountPolicy {

    private static final int DISCOUNT_AMOUNT = 1000;

    @Override
    public int discount(int price) {
        return Math.min(price, DISCOUNT_AMOUNT);
    }
}
