package com.java.spring_study.proxypattern.example;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

public class ProxyStudyMain {

    public static void main(String[] args) {

        /*
         * 실제 비즈니스 로직을 담당하는 Target 객체
         */
        OrderService target = new RealOrderService();

        /*
         * 1. Proxy 없이 직접 호출
         */
        System.out.println("====== 1. Target 직접 호출 ======");

        target.order("키보드", 2);

        /*
         * 2. 직접 만든 Static Proxy 사용
         *
         * 클라이언트는 실제 객체가 아니라 Proxy를 호출한다.
         * Proxy는 부가 기능을 수행한 후 Target에게 요청을 위임한다.
         */
        System.out.println();
        System.out.println("====== 2. 직접 만든 Proxy ======");

        OrderService staticProxy = new OrderServiceProxy(target);

        staticProxy.order("마우스", 1);
        staticProxy.cancel("마우스");

        /*
         * 3. Spring ProxyFactory
         *
         * 개발자가 OrderServiceProxy와 같은 클래스를 직접 만들지 않고
         * Spring이 런타임에 Proxy 객체를 생성한다.
         */
        System.out.println();
        System.out.println("====== 3. Spring JDK Dynamic Proxy ======");

        // Spring이 Proxy를 생성해주는 팩토리 클래스
        ProxyFactory jdkProxyFactory = new ProxyFactory(target);

        // 부가 기능(interceptor) 등록
        jdkProxyFactory.addAdvice(new ExecutionTimeInterceptor());

        // 실제 Proxy 객체 생성
        OrderService jdkProxy = (OrderService) jdkProxyFactory.getProxy();

        System.out.println("Proxy 클래스: " + jdkProxy.getClass().getName());
        System.out.println("JDK Dynamic Proxy: " + AopUtils.isJdkDynamicProxy(jdkProxy));

        jdkProxy.order("모니터", 1);

        /*
         * 4. CGLIB Proxy
         *
         * proxyTargetClass를 true로 설정하면
         * 인터페이스 기반이 아닌 클래스 기반 Proxy를 생성하도록 강제한다.
         */
        System.out.println();
        System.out.println("====== 4. Spring GCLIB Proxy ======");

        ProxyFactory cglibProxyFactory = new ProxyFactory(target);

        // 인터페이스가 있어도 강제로 CGLIB 사용
        cglibProxyFactory.setProxyTargetClass(true);

        cglibProxyFactory.addAdvice(new ExecutionTimeInterceptor());

        RealOrderService cglibProxy = (RealOrderService) cglibProxyFactory.getProxy();

        System.out.println("Proxy 클래스: " + cglibProxy.getClass().getName());
        System.out.println("CGLIB proxy : " + AopUtils.isCglibProxy(cglibProxy));

        cglibProxy.order("노트북", 1);
    }
}
