package com.java.spring_study.oop;

/**
 * 강한 결합 (Tight Coupling)을 확인하는 예제
 *
 * OrderService가 구체 클래스인 FixedDiscountPolicy를 직접 생성하고 사용한다.
 * 할인 정책을 RateDiscountPolicy로 변경하려면 TightOrderService의 코드도 직접 수정해야 한다.
 */
public class TightOrderService {

    private final FixedDiscountPolicy discountPolicy = new FixedDiscountPolicy();

    // 할인 적용 후 최종 가격을 계산
    public int calculatePrice(int price) {
        int discountAmount = discountPolicy.discount(price);
        return price - discountAmount;
    }
}
