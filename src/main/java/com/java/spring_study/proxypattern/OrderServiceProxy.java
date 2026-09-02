package com.java.spring_study.proxypattern;

/**
 * 직접 작성한 Proxy 객체
 *
 * OrderService와 동일한 인터페이스를 구현하고 내부에 실제 Target 객체를 가지고 있는다.
 * 요청을 받은 뒤 실제 객체를 호출하기 전/후에 실행 시간 측정이라는 부가 기능을 수행한다.
 */
public class OrderServiceProxy implements OrderService {

    private final OrderService target;

    public OrderServiceProxy (OrderService target) {
        this.target = target;
    }

    @Override
    public void order(String productName, int quantity) {

        long startTime = System.currentTimeMillis();    // 시작 시간 기록

        try {
            System.out.println("[Proxy] 주문 요청 시작"); // 부가 기능

            // 실제 비즈니스 로직 호출
            target.order(productName, quantity);
        } finally {
            long endTime = System.currentTimeMillis();
            
            // 실행 시간 출력
            System.out.printf(
                    "[Proxy] 실행 시간: %dms%n",
                    endTime - startTime
            );
        }
    }
    
    // 시작 시간 기록 → 부가 기능 → 실제 호출 → 실행 시간 출력
    @Override
    public void cancel(String productName) {

        long startTime = System.currentTimeMillis();

        try {
            System.out.println("[Proxy] 주문 취소 요청 시작");

            target.cancel(productName);
        } finally {
            long endTime = System.currentTimeMillis();

            System.out.printf(
                    "[Proxy] 실행 시간: %dms%n",
                    endTime - startTime
            );
        }
    }
}
