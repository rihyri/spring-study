package com.java.spring_study.proxypattern.example;

/**
 * 실제 비즈니스 로직을 수행하는 Target 객체
 *
 * 주문과 취소라는 핵심 기능에만 집중하고,
 * 실행 시간 측정이나 로그 출력과 같은 부가 기능은 포함하지 않는다.
 */
public class RealOrderService implements OrderService {

    @Override
    public void order(String productName, int quantity) {
        System.out.printf(
                "[OrderService] %s 상품 %d개 주문 처리%n",
                productName,
                quantity
        );

        simulateWork(300);  // 300ms 대기 (실제 작업 시뮬레이션)
    }

    @Override
    public void cancel(String productName) {
        System.out.printf(
                "[OrderService] %s 상품 주문 취소%n",
                productName
        );

        simulateWork(300);
    }

    /**
     * 실제 작업이 수행되는 것처럼 실행 시간을 만들어주기 위한 코드
     */
    private void simulateWork(long millis) {
        try {
            Thread.sleep(millis);   // 지정된 시간만큼 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
