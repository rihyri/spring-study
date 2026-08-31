package com.java.spring_study.oop.example;

/**
 * 비율 할인 정책
 * 전달받은 할인율만큼 상품 가격을 할인
 */
public class RateDiscountPolicy implements DiscountPolicy{

    private final int discountRate;

    public RateDiscountPolicy(int discountRate) {
        if (discountRate < 0 || discountRate > 100) {
            throw new IllegalArgumentException("할인율은 0 이상 100 이하이어야 합니다.");
        }

        this.discountRate = discountRate;
    }

    @Override
    public int discount(int price) {
        return price * discountRate / 100;
    }
}
