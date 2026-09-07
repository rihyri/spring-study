package com.java.spring_study.iocdi.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IocDiStudyMain {

    public static void main(String[] args) {

        /*
         * Spring을 사용하지 않는 경우
         *
         * DiscountPolicy discountPolicy = new RateDiscountPolicy(10);
         * OrderService orderService = new OrderService(discountPolicy);
         *
         * 객체 생성과 의존 관계 연결을 개발자가 직접 담당한다.
         */
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            DiscountPolicy discountPolicy = context.getBean(DiscountPolicy.class);
            OrderService orderService = context.getBean(OrderService.class);

            int price = 20000;

            System.out.println("====== Spring IoC / DI ======");
            System.out.println("DiscountPolicy Bean: " + discountPolicy.getClass().getSimpleName());
            System.out.println("OrderService Bean: " + orderService.getClass().getSimpleName());
            System.out.println("상품 가격: " + price + "원");
            System.out.println("최종 가격: " + orderService.calculatePrice(price) + "원");
        }
    }
}
