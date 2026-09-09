<br>

# 1. IoC / DI / Spring Container

<br>

이전 객체지향 예제에서는 `OrderService`가 자신이 사용할 할인 정책을 직접 생성하지 않고 외부에서 전달받도록 구현했다.

```java
DiscountPolicy discountPolicy = new RateDiscountPolicy(10);
OrderService orderService = new OrderService(discountPolicy);
```

이 구조에서는 `OrderService`와 `RateDiscountPolicy`의 결합은 줄어들었지만, 여전히 객체를 생성하고 의존 관계를 연결하는 작업은 개발자가 직접 담당한다.

이번 예제에서는 이 역할을 Spring Container에 맡기면서 다음 내용을 확인한다.

- Dependency Injection
- Inversion of Control
- Spring Container
- Spring Bean
- ApplicationContext
- `@Configuration`
- `@Bean`

<br>
<hr>
<br>

## 1. Dependency Injection

<br>

`OrderService`는 자신이 사용할 할인 정책을 직접 생성하지 않는다.

```java
public class OrderService {
    
    private final DiscountPolicy discountPolicy;
    
    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```

필요한 `DiscountPolicy` 객체를 생성자를 통해 외부에서 전달받는다.

```java
DiscountPolicy discountPolicy = new RateDiscountPolicy(10);
OrderService orderService = new OrderService(discountPolicy);
```

이처럼 객체가 필요로 하는 의존 객체를 외부에서 전달하는 것을 **Dependency Injection(DI)**이라고 한다.

```aiignore
    RateDiscountPolicy 생성 
            ↓
   OrderService 생성자에 전달
            ↓
       OrderService
```

DI 자체는 Spring이 없어도 사용할 수 있다.
중요한 것은 객체가 자신이 사용할 구현 객체를 직접 생성하지 않는다는 점이다.

<br>
<hr>
<br>

## 2. 객체를 누가 생성할까?

<br>

Spring을 사용하지 않는 현재 구조에서는 `main()` 메서드가 객체를 생성한다.

```java
DiscountPolicy discountPolicy = new RateDiscountPolicy(10);
OrderService orderService = new OrderService(discountPolicy);
```

따라서 `main()`은 다음 역할을 모두 담당한다.

```aiignore
main()
├── RateDiscountPolicy 생성
├── OrderService 생성
└── 두 객체의 의존 관계 연결
```

애플리케이션이 커지면 관리해야 하는 객체와 의존 관계도 많아진다.
Spring에서는 이러한 객체의 생성과 관리 역할을 **Spring Container**에 맡길 수 있다.

<br>
<hr>
<br>

## 3. Spring Container 

<br>

Spring Container는 애플리케이션에서 사용할 객체를 생성하고 관리한다.
이번 예제에서는 다음 설정 클래스를 사용한다.

```java
@Configuration
public class AppConfig {

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy(10);
    }

    @Bean
    public OrderService orderService(DiscountPolicy discountPolicy) {
        return new OrderService(discountPolicy);
    }
}
```

`@Configuration`은 해당 클래스가 Spring 설정 정보를 가지고 있음을 나타낸다.

```java
@Configuration
public class AppConfig{
}
```

`@Bean`이 붙은 메서드가 반환하는 객체는 Spring Container에 등록된다.

```java
@Bean
public DiscountPolicy discountPolicy() {
    return new RateDiscountPolicy(10);
}
```

따라서 `RateDiscountPolicy` 객체는 개발자가 직접 관리하는 객체가 아니라 Spring Container가 관리하는 객체가 된다.

Spring Container가 관리하는 객체를 **Spring Bean**이라고 한다.

```aiignore
* Spring Container

├── discountPolicy 
│       └── RateDiscountPolicy 
│ 
└── orderService 
        └── OrderService
```

<br>
<hr>
<br>

## 4. Spring에서의 Dependency Injection

<br>

`OrderService` Bean은 `DiscountPolicy`를 필요로 한다.

```java
@Bean
public OrderService orderService(DiscountPolicy discountPolicy) {
    return new OrderService(discountPolicy);
}
```

Spring Container에는 이미 다음 Bean이 등록되어 있다.

```java
@Bean
public DiscountPolicy discountPolicy() {
    return new RateDiscountPolicy(10);
}
```

Spring Container는 `OrderService`를 생성할 때 필요한 `DiscountPolicy` Bean을 찾아 전달한다.

```aiignore
    Spring Container
            │
            ├── RateDiscountPolicy 생성
            │         │
            │         ▼
            └── OrderService 생성
                      │
                      └── DiscountPolicy 주입
```

개발자가 직접 객체를 생성하고 연결하는 코드를 작성하지 않아도 된다.

```java
new OrderService(new RateDiscountPolicy(10));
```

객체 생성과 의존 관계 연결을 Spring Container가 담당한다.


<br>
<hr>
<br>

## 5. IoC - Inversion of Control

<br>

Spring을 사용하기 전에는 개발자가 객체의 생성과 흐름을 직접 제어했다.

```aiignore
* 개발자 코드

RateDiscountPolicy 생성 
        ↓
OrderService 생성
        ↓
OrderService 실행
```

Spring을 사용하면 객체 생성과 의존 관계 관리의 주체가 Spring Container로 바뀐다.

```aiignore
* Spring Container

    Bean 생성
        ↓
  의존 관계 연결
        ↓
    Bean 관리
        ↓
  애플리케이션에서 사용
```

이처럼 객체의 생성과 관리에 대한 제어권이 개발자 코드에서 프레임워크로 이동하는 것을 **Inversion of Control(IoC), 제어의 역전**이라고 한다.

즉,

```aiignore
* 기존

       개발자
         ↓
객체 생성 / 연결 / 관리


* Spring
        
       개발자
         ↓
   설정 정보 제공
         ↓
  Spring Container
         ↓
 객체 생성 / 연결 / 관리
```

의 구조가 된다.

<br>
<hr>
<br>

## 6. ApplicationContext

<br>

Spring Container를 직접 생성해보면 다음과 같다.

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
```

`ApplicationContext`는 Spring Container를 나타내는 핵심 인터페이스이다.

이번 예제에서는 Java 설정 클래스인 `AppConfig`를 사용하기 때문에 `AnnotationConfigApplicationContext`를 사용한다.

```aiignore
            AppConfig
                ↓
AnnotationConfigApplicationContext
                ↓
        Spring Container 생성
                ↓
            @Bean 등록
```

Container가 생성되면 `AppConfig`에 정의된 Bean들이 등록된다.

<br>
<hr>
<br>

## 7. Bean 조회

<br>

Spring Container에 등록된 객체는 `getBean()`을 이용해 가져올 수 있다.

```java
DiscountPolicy discountPolicy = context.getBean(DiscountPolicy.class);

OrderService orderService = context.getBean(OrderService.class);
```

여기서 중요한 점은 객체를 새롭게 생성하는 것이 아니라, Spring Container가 관리하고 있는 Bean을 가져온다는 것이다.

```java
new OrderService(...)
```

가 아니라

```aiignore
    Spring Container
           ↓
       getBean()
           ↓
      OrderService
```

의 흐름이 된다.

<br>
<hr>
<br>

## 8. DI와 Ioc의 차이

<br>

DI와 IoC는 함께 언급되지만 의미는 조금 다르다.

<br>

<table>
    <thead>
        <th>개념</th>
        <th>의미</th>
    </thead>
    <tbody>
        <tr>
            <td>DI</td>
            <td>객체가 필요한 의존 객체를 외부에서 전달받는 방식</td>
        </tr>
        <tr>
            <td>IoC</td>
            <td>객체의 생성과 관리에 대한 제어권이 외부로 이동하는 것</td>
        </tr>
        <tr>
            <td>Spring Container</td>
            <td>객체를 생성하고 의존 관계를 연결하며 관리하는 주체</td>
        </tr>
        <tr>
            <td>Bean</td>
            <td>Spring Container가 관리하는 객체</td>
        </tr>
    </tbody>
</table>

<br>

예를 들어 다음 코드는 Spring 없이도 DI이다.

```java
DiscountPolicy discountPolicy = new RateDiscountPolicy(10);
OrderService orderService = new OrderService(discountPolicy);
```

외부에서 `DiscountPolicy`를 전달하고 있기 때문이다.

Spring을 사용하면 객체 생성과 DI까지 Spring Container가 담당한다.

```aiignore
DI = 의존 객체를 외부에서 전달

IoC = 객체 관리의 제어권이 외부로 이동

Spring = Spring Container가 IoC와 DI를 담당
```

<br>
<hr>
<br>

## 9. 전체 흐름

<br>

이번 예제의 전체 흐름은 다음과 같다.

```aiignore
AppConfig
    │
    ├── @Bean
    │   RateDiscountPolicy
    │
    └── @Bean
        OrderService
            ▲
            │
     DiscountPolicy 주입
            │
            │
     Spring Container
            │
            ▼
        Application    
```

기존에는 개발자가 직접 객체를 만들고 연결했다.

```java
DiscountPolicy discountPolicy = new RateDiscountPolicy(10);
OrderService orderService = new OrderService(discountPolicy);
```

Spring을 사용하면 개발자는 어떤 객체를 사용할지 설정만 하고, 실제 객체 생성과 의존 관계 관리는 Spring Container가 담당한다.

<br>
<hr>
<br>

## 10. 정리

<br>

Spring의 핵심은 객체 자체를 특별하게 만드는 것이 아니다.

`OrderService`와 `RateDiscountPolicy`는 여전히 일반적인 Java 객체이다.

```java
public class OrderService {
}

public class RateDiscountPolicy implements DiscountPolicy {
}
```

Spring은 이러한 객체를 Container에 등록하고 객체의 생성, 연결, 관리를 담당한다.

```aiignore
일반 Java 객체
      ↓
Spring Container에 등록
      ↓
Spring Bean
      ↓
의존 관계 주입
      ↓
애플리케이션에서 사용
```

핵심은 다음과 같다.

```aiignore
DI → 필요한 객체를 외부에서 전달받는다.

IoC → 객체 관리의 제어권이 Spring으로 이동한다.

Spring Container → Bean을 생성하고 연결하고 관리한다.

Bean → Spring Container가 관리하는 객체이다.
```

이번 단계에서는 Bean을 `@Bean`을 이용하여 직접 등록했다.

<br>
<hr>
<br>

## 실행

<br>

`IocDiStudyMain`의 `main()` 메서드를 실행한다.

```aiignore
===== Spring IoC / DI ===== 
DiscountPolicy Bean: RateDiscountPolicy 
OrderService Bean: OrderService 
상품 가격: 20000원 
최종 가격: 18000원
```

이 예제를 통해 개발자가 직접 객체를 생성하는 방식과 Spring Container가 Bean을 생성하고 의존 관계를 주입하는 방식의 차이를 확인할 수 있다.