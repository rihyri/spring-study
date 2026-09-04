package com.java.spring_study.aop.example;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopStudyMain {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopStudyConfig.class);
        
        // Spring 컨테이너에서 Bean 가져오기
        OrderService orderService = context.getBean(OrderService.class);
        PaymentService paymentService = context.getBean(PaymentService.class);

        /*
         *  1. Spring이 실제 객체 대신 AOP Proxy를 반환했는지 확인한다.
         */
        System.out.println("====== 1. AOP Proxy 확인 ======");

        System.out.println("OrderService Proxy: " + AopUtils.isAopProxy(orderService));
        System.out.println("PaymentService Proxy: " + AopUtils.isAopProxy(paymentService));

        /*
         *  2. @TrackExecutionTime이 선언된 메서드
         *
         *  Proxy가 요청을 가로채고 ExecutionTimeAspect가 실행된다.
         */
        System.out.println();
        System.out.println("====== 2. Order AOP ======");

        String orderId = orderService.order("keyboard", 2);

        /*
         *  3. 다른 클래스에도 동일한 Aspect 적용
         *
         *  PaymentService 역시 같은 어노테이션을 사용하므로 동일한 실행 시간 측정 로직이 적용된다.
         */
        System.out.println();
        System.out.println("====== 3. Payment AOP ======");

        paymentService.pay(orderId, 50000);

        /*
         *  4. Pointcut 대상이 아닌 메서드
         *
         *  @TrackExecutionTime이 없기 때문에 AOP 로그가 출력되지 않는다.
         */
        System.out.println();
        System.out.println("====== 4. AOP 적용 대상이 아닌 메서드 ======");

        orderService.checkStatus(orderId);

        /*
         *  5. Self Invocation
         *
         *  orderAndNotify() 내부에서 order()를 호출하지만 같은 내부 호출이므로 Proxy를 거치지 않는다.
         *  따라서 order()의 @TrackExecutionTime은 이 호출에서 적용되지 않는다.
         */
        System.out.println();
        System.out.println("====== 5. Self Invocation ======");

        orderService.orderAndNotify("mouse", 1);

        context.close();
    }
}
