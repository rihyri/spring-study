package com.java.spring_study.singleton_prototype.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonPrototypeStudyMain {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SingletonPrototypeConfig.class);

        /*
         * 1. Singleton Scope
         *
         * 같은 Bean을 여러번 조회해도 Spring Container가 관리하는 동일한 객체가 반환된다.
         */
        System.out.println("====== 1. Singleton Scope =======");

        SingletonCounter singleton1 = context.getBean(SingletonCounter.class);
        SingletonCounter singleton2 = context.getBean(SingletonCounter.class);

        System.out.println("singleton1 == singleton2 : " + (singleton1 == singleton2));
        System.out.println("singleton1 count : " + singleton1.increase());
        System.out.println("singleton2 count : " + singleton2.increase());

        /*
         * 2. Prototype Scope
         *
         * getBean()을 호출할 때마다 새로운 객체가 생성된다.
         * 따라서 각 객체가 가진 count도 독립적이다.
         */
        System.out.println();
        System.out.println("====== 2. Prototype Scope ======");

        PrototypeCounter prototype1 = context.getBean(PrototypeCounter.class);
        PrototypeCounter prototype2 = context.getBean(PrototypeCounter.class);

        System.out.println("prototype1 == prototype2 : " + (prototype1 == prototype2));
        System.out.printf("prototype1 (#%d) count : %d%n", prototype1.getInstanceId(), prototype1.increase());
        System.out.printf("prototype1 (#%d) count : %d%n", prototype1.getInstanceId(), prototype1.increase());
        System.out.printf("prototype2 (#%d) count : %d%n", prototype2.getInstanceId(), prototype2.increase());

        /*
         * 3. Singleton Bean에 Prototype을 직접 주입
         *
         * Prototype Bean을 주입했지만 Singleton Bean이 생성될 때 한 번만 주입된다.
         */
        System.out.println();
        System.out.println("====== 3. Singleton + Prototype 직접 주입 ======");

        DirectPrototypeService directService = context.getBean(DirectPrototypeService.class);

        directService.increase();
        directService.increase();

        /*
         * 4. ObjectProvider 사용
         *
         * 필요한 시점마다 Spring Container에 Prototype Bean을 요청한다.
         */
        System.out.println();
        System.out.println("====== 4. ObjectProvider + Prototype ======");

        ProviderPrototypeService providerService = context.getBean(ProviderPrototypeService.class);

        providerService.increase();
        providerService.increase();

        context.close();
    }
}
