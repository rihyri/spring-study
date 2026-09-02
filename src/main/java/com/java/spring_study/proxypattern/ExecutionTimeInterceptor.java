package com.java.spring_study.proxypattern;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Spring Proxy에 적용할 부가 기능
 *
 * Target 메서드가 실행되기 전과 후를 가로채서 실행 시간을 측정한다.
 * innovation.proceed()를 호출해야 실제 Target 메서드가 실행된다.
 */
public class ExecutionTimeInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();

        try {
            System.out.printf(
                    "[Interceptor] %s () 시작%n",
                    invocation.getMethod().getName()
            );

            // 실제 Target 객체의 메서드를 호출한다.
            return invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();

            System.out.printf(
                    "[Interceptor] %s 실행 시간: %dms%n",
                    invocation.getMethod().getName(),
                    endTime - startTime
            );
        }
    }
}
