package com.java.spring_study.lifecyclescope.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = LifecycleScopeConfig.class)
public class LifecycleScopeConfig {
}
