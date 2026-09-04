package com.java.spring_study.aop;

import org.springframework.stereotype.Service;

/**
 * 주문과 관련된 핵심 비지니스 로직
 *
 * 실행 시간 측정 코드는 직접 장석하지 않고, 필요한 메서드에 @TrackExecutionTime만 선언한다.
 */
@Service
public class OrderService {

    @TrackExecutionTime     // 커스텀 어노테이션 - 실행 시간 측정 대상
    public String order(String productName, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }

        simulateWork(200);

        String orderId = "ORDER-" + productName.toUpperCase();

        System.out.printf(
                "[OrderService] %s 상품 %d개 주문 완료%n",
                productName,
                quantity
        );

        return orderId;
    }

    /**
     * @TrackExecutionTime이 없으므로 AOP의 실행 시간 측정 대상이 아니다. (AOP 대상 아님)
     */
    public void checkStatus(String orderId) {

        System.out.printf(
                "[OrderService] %s 상태 조회%n",
                orderId
        );
    }

    /**
     * self Inovation을 확인하기 위한 메서드
     *
     * ⚠️ 같은 객체 내부에서 order()를 호출하면 Proxy를 거치지 않고 this.order() 형태로 호출한다.
     */
    public void orderAndNotify(String productName, int quantity) {

        System.out.println("[OrderService] 주문 + 알림 처리 시작");

        // 내부 호출이므로 Spring AOP Proxy를 거치지 않는다.
        String orderId = order(productName, quantity);

        System.out.printf(
                "[OrderService] %s 주문 완료 알림 전송%n",
                orderId
        );
    }

    private void simulateWork(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
