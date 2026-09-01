package com.java.spring_study.lifecyclescope.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring 설정 파일임을 선언
@ComponentScan(basePackageClasses = LifecycleScopeConfig.class) // 빈을 스캔할 위치를 지정 (지정된 클래스가 속한 패키지 기준으로)
public class LifecycleScopeConfig {
}
