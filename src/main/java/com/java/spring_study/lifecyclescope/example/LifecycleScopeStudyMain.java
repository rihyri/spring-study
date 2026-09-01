package com.java.spring_study.lifecyclescope.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LifecycleScopeStudyMain {

    public static void main(String[] args) {

        System.out.println("====== Spring Context 생성 ======");

        // 1. Spring Container 생성 (이때 설정 파일을 읽어서 빈들을 생성)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LifecycleScopeConfig.class);

        System.out.println();
        System.out.println("====== Singleton Bean 사용 ======");
        
        // 2. Singleton Bean 가져오기 (이미 생성되어 있음)
        SingletonLifecycleBean singletonBean = context.getBean(SingletonLifecycleBean.class);
        singletonBean.use();

        System.out.println();
        System.out.println("====== Prototype Bean 사용 ======");
        
        // 3. Prototype Bean 가져오기 (첫 번째 요청 → 새로 생성)
        PrototypeLifecycleBean prototypeBean1 = context.getBean(PrototypeLifecycleBean.class);
        prototypeBean1.use();

        System.out.println();
        
        // 4. Prototype Bean 가져오기 (두 번째 요청 → 또 새롭게 생성)
        PrototypeLifecycleBean prototypeBean2 = context.getBean(PrototypeLifecycleBean.class);
        prototypeBean2.use();

        System.out.println();
        System.out.println("====== Spring Context 종료 ======");
    
        // 5. Container 종료 → Singleton Bean의 @PreDestroy 실행
        context.close();
    }
}
