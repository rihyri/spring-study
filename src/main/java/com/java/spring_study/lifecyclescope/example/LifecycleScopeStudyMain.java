package com.java.spring_study.lifecyclescope.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LifecycleScopeStudyMain {

    public static void main(String[] args) {

        System.out.println("====== Spring Context 생성 ======");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LifecycleScopeConfig.class);

        System.out.println();
        System.out.println("====== Singleton Bean 사용 ======");

        SingletonLifecycleBean singletonBean = context.getBean(SingletonLifecycleBean.class);
        singletonBean.use();

        System.out.println();
        System.out.println("====== Prototype Bean 사용 ======");

        PrototypeLifecycleBean prototypeBean1 = context.getBean(PrototypeLifecycleBean.class);
        prototypeBean1.use();

        System.out.println();

        PrototypeLifecycleBean prototypeBean2 = context.getBean(PrototypeLifecycleBean.class);
        prototypeBean2.use();

        System.out.println();
        System.out.println("====== Spring Context 종류 ======");

        context.close();
    }
}
