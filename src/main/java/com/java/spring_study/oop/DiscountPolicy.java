package com.java.spring_study.oop;

/**
 * 할인 정책을 정의하는 인터페이스
 **/
public interface DiscountPolicy {
    
    // 상품 가격을 기준으로 할인 금액을 계산
    int discount(int price);
}
