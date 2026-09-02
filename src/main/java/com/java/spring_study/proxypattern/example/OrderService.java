package com.java.spring_study.proxypattern.example;

/**
 * 주문 기능의 역할을 정의한다.
 *
 * 클라이언트는 실제 구현체가 RealOrderService인지,
 * Proxy 객체인지 알 필요 없이 OrderService 인터페이스만 사용한다.
 */
public interface OrderService {

    void order(String productName, int quantity);

    void cancel(String productName);
}
