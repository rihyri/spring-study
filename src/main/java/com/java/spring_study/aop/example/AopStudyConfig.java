package com.java.spring_study.aop.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring AOP 설정 클래스
 *
 * @EnableAspectJAutoProxy 통해
 * @Aspect 기반의 Spring AOP를 활성화한다.
 */
@Configuration
@ComponentScan(basePackageClasses = AopStudyConfig.class)
@EnableAspectJAutoProxy // Spring이 Advice 대상 Bean에 대해 자동으로 Proxy 생성
public class AopStudyConfig {
}
