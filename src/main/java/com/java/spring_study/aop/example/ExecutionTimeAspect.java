package com.java.spring_study.aop.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 실행 시간 측정이라는 공통 관심사를 담당하는 Asepect
 *
 * OrderService나 PaymentService는 실행 시간 측정 방법을 알 필요가 없고,
 * Asepect가 Proxy를 통해 메서드 실행 전/후에 부가 기능을 수행한다.
 */
@Aspect
@Component
public class ExecutionTimeAspect {

    /**
     * @TrackExecutionTime이 선언된 메서드를 AOP 적용 대상으로 지정한다.
     */
    @Pointcut(
            "@annotation(com.java.spring_study.aop.example.TrackExecutionTime)"
    ) public void trackExecutionTimePointcut() {
    }

    /**
     * @Around Advice
     *
     * 대상 메서드 실행 전과 후를 모두 처리할 수 있다.
     * ProceedingJoinPoint : 실제 대상 메서드의 정보를 담고 있는 객체
     * joinPoint.proceed()가 호출되는 순간 실제 Target 메서드가 실행된다.
     */
    @Around("trackExecutionTimePointcut()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();

        long startTime = System.nanoTime(); // 나노초 단위로 시작 시간 측정

        System.out.printf(
                "[AOP] %s 실행 시작%n",
                methodName
        );

        try {
            // 실제 Target 메서드 실행
            Object result = joinPoint.proceed();

            System.out.printf(
                    "[AOP] %s 정상 종료%n",
                    methodName
            );

            return result;
        } catch (Throwable throwable) {

            System.out.printf(
                    "[AOP] %s 예외 발생: %s%n",
                    methodName,
                    throwable.getMessage()
            );

            throw throwable;
        } finally {

            long endTime = System.nanoTime();

            double elapsedMillis = (endTime - startTime) / 1_000_000.0;

            System.out.printf(
                    "[AOP] %s 실행 시간: %.2fms%n",
                    methodName,
                    elapsedMillis
            );
        }
    }
}
