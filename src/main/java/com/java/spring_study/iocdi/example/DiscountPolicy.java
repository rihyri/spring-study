package com.java.spring_study.iocdi.example;

/**
 * 할인 정책을 정의하는 인터페이스
 */
public interface DiscountPolicy {

    int discount(int price);
}
