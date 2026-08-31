package com.java.spring_study.oop;

public class OopStudyMain {

    public static void main(String[] args) {

        int price = 20000;

        System.out.println("====== 1. 강한 결합 ======");

        TightOrderService tightOrderService = new TightOrderService();
        int tightResult = tightOrderService.calculatePrice(price);

        System.out.println("상품 가격: " + price + "원");
        System.out.println("최종 가격: " + tightResult + "원");

        System.out.println();
        System.out.println("====== 2. 다형성 - 고정 할인 ======");

        // 인터페이스 타입의 변수에 객체를 할당
        DiscountPolicy fixedDiscountPolicy = new FixedDiscountPolicy();

        OrderService fixedOrderService = new OrderService(fixedDiscountPolicy);
        int fixedResult = fixedOrderService.calculatePrice(price);

        System.out.println("상품 가격: " + price + "원");
        System.out.println("할인 정책: FixedDiscountPolicy");
        System.out.println("최종 가격: " + fixedResult + "원");

        System.out.println();
        System.out.println("====== 3. 다형성 - 비율 할인 ======");

        // OrderService의 코드는 변경하지 않고 다른 DiscountPolicy 구현체를 전달할 수 있다.
        DiscountPolicy rateDiscountPolicy = new RateDiscountPolicy(10);

        OrderService rateOrderService = new OrderService(rateDiscountPolicy);
        int rateResult = rateOrderService.calculatePrice(price);

        System.out.println("상품 가격: " + price + "원");
        System.out.println("할인 정책: RateDiscountPolicy");
        System.out.println("최종 가격: " + rateResult + "원");
    }
}
