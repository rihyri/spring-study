package com.java.spring_study.iocdi;

/**
 * 비율 할인 정책
 */
public class RateDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public RateDiscountPolicy(int discountRate) {
        if (discountRate < 0 || discountRate > 100) {
            throw new IllegalArgumentException("할인율은 0이상 100이하어야 합니다.");
        }

        this.discountRate = discountRate;
    }

    @Override
    public int discount(int price) {
        return price * discountRate / 100;
    }
}
