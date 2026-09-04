package com.java.spring_study.aop.example;

import java.lang.annotation.*;

/**
 * 실행 시간을 측정할 메서드에 붙이는 커스텀 어노테이션
 *
 * 이 어노테이션이 붙은 메서드만 ExecutionTimeAspect의 Pointcut 대상이 된다.
 */
@Target(ElementType.METHOD) // 메서드에서만 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 정보를 런타임까지 유지
@Documented
public @interface TrackExecutionTime {
    // 커스텀 어노테이션 정의
    // 이 어노테이션이 붙은 메서드만 AOP의 대상이 됨
}
