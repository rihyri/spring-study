package com.java.spring_study.iocdi.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Container에 등록할 객체와 객체 사이의 의존 관계를 설정한다.
 */
@Configuration
public class AppConfig {

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy(10);
    }

    @Bean
    public OrderService orderService(DiscountPolicy discountPolicy) {
        return new OrderService(discountPolicy);
    }
}
