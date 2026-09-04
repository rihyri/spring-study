<br>

# 4. Spring AOP

<br>

AOP(Aspect-Oriented Programming)은 여러 객체에 반복되는 **공통 관심사(Cross-Cutting Concern)**를 핵심 비즈니스 로직과 분리하는 프로그래밍 방식이다.

예를 들어 다음 기능은 여러 서비스에서 반복될 가능성이 높다.

- Logging
- 실행 시간 측정
- Transaction
- Security
- Cache

이번 예제에서는 실행 시간 측정 기능을 AOP로 분리한다.

<br>
<hr>
<br>

## 1. 왜 AOP를 사용할까?

<br>

AOP를 사용하지 않으면 비즈니스 코드에 부가 기능이 섞일 수 있다.

```java
public void order() {
    
    long start = System.currentTimeMillis();
    
    // 실제 주문 처리
    
    long end = System.currentTimeMillis();
}
```

결제, 주문, 회원 서비스 등 여러 곳에서 동일한 기능이 필요하다면 코드가 중복된다.

```aiignore
    OrderService
        ├─ 실행 시간 측정
        └─ 주문 처리

    PaymentService
        ├─ 실행 시간 측정
        └─ 결제 처리

    MemberService
        ├─ 실행 시간 측정
        └─ 회원 처리
```

AOP를 사용하면 공통 기능을 별도의 Aspect로 분리할 수 있다.

```aiignore
    ExecutionTimeAspect
             │ 
   ┌─────────┴─────────┐ 
   ▼                   ▼
OrderService      PaymentService
 주문 처리            결제 처리
```

서비스는 핵심 비즈니스 로직에 집중하고 실행 시간 측정은 Aspect가 담당한다.

<br>
<hr>
<br>

## 2. Spring AOP 핵심 용어

<br>

<table>
    <thead>
        <th>용어</th>
        <th>의미</th>
    </thead>
    <tbody>
        <tr>
            <td>Aspect</td>
            <td>공통 관심사를 모듈화한 클래스</td>
        </tr>
        <tr>
            <td>Advice</td>
            <td>실제로 수행할 부가 기능</td>
        </tr>
        <tr>
            <td>Join Point</td>
            <td>Advice가 적용될 수 있는 지점</td>
        </tr>
        <tr>
            <td>Pointcut</td>
            <td>어떤 Join point에 Advice를 적용할지 결정하는 조건</td>
        </tr>
        <tr>
            <td>Target</td>
            <td>실제 비즈니스 로직을 가진 객체</td>
        </tr>
        <tr>
            <td>Proxy</td>
            <td>Target 대신 요청을 받고 Advice를 적용하는 객체</td>
        </tr>
    </tbody>
</table>

<br>

Spring AOP에서 Join Point는 **Spring Bean의 메서드 실행**을 의미한다.

전체 구조는 다음과 같다.

```aiignore
  Client
    │
    ▼
Spring AOP Proxy
    │
    ├─ Advice
    │
    ▼
Target Method
```

3단계에서 직접 만들었던 Proxy를 Spring이 자동으로 생성한다고 생각하면 이해하기 쉽다.

<br>
<hr>
<br>

## 3. Aspect와 Pointcut

<br>

Aspect는 공통 관심사를 담당한다.

```java
@Aspect
@Component
public class ExecutionTimeAspect {
}
```

이번 예제에서는 `@TrackExecutionTime`이 붙은 메서드만 대상으로 지정한다.

```java
@Pointcut(
        "@annotation(com.java.spring_study.aop.example.TrackExecutionTime)"
) 
public void trackExecutionTimePointcut() {
}
```

따라서 

```java
@TrackExecutionTime
public String order(...) {
}
```

는 AOP 대상이지만,

```java
public void checkStatus(...) {
}
```

는 대상이 아니다.

Pointcut은 **어디에 적용할 것인가,** Advice는 **무엇을 실행할 것인가**를 담당한다.

<br>
<hr>
<br>

## 4. Advice

<br>

Spring AOP는 여러 종류의 Advice를 제공한다.

<br>

<table>
    <thead>
        <th>Advice</th>
        <th>실행 시점</th>
    </thead>
    <tbody>
        <tr>
            <td>@Before</td>
            <td>메서드 실행 전</td>
        </tr>
        <tr>
            <td>@AfterReturning</td>
            <td>정상 반환 후</td>
        </tr>
        <tr>
            <td>@AfterThrowing</td>
            <td>예외 발생 후</td>
        </tr>
        <tr>
            <td>@After</td>
            <td>정상/예외 여부와 관계없이 실행 후</td>
        </tr>
        <tr>
            <td>@Around</td>
            <td>메서드 실행 전과 후</td>
        </tr>
    </tbody>
</table>

<br>

이번 예제에서는 실행 시간을 측정해야 하므로 `@Around`를 사용한다.

```java
@Around("trackExecutionTimePointcut()")
public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

    long start = System.nanoTime();

    Object result = joinPoint.proceed();

    long end = System.nanoTime();

    return result;
}
```

`joinPoint.proceed()`가 호출되면 실제 Target 메서드가 실행된다.

```aiignore
    @Around Advice 시작
            ↓
    실행 시간 측정 시작
            ↓
    joinPoint.proceed()
            ↓
    Target Method
            ↓
    실행 시간 측정 종료
```

<br>
<hr>
<br>

## 5. AOP와 Proxy

<br>

Spring AOP는 Proxy 기반으로 동작한다.

```aiignore
    호출자
      │
      ▼
    Proxy
      │
      ├─Aspect / Advice
      │
      ▼
    Target
```

따라서 호출자가 Spring Bean을 호출하면 Proxy가 요청을 먼저 가로채고, Pointcut 조건에 해당하는 경우 Advice를 실행한다.

```java
orderService.order("keyboard", 2);
```

실제로는 다음과 같은 흐름이 된다.

```aiignore
      Client  
        ↓
  OrderService Proxy
        ↓
  ExecutionTimeAspect
        ↓
  OrderService.order()
```

<br>
<hr>
<br>

## 6. Self Invocation

<br>

Spring AOP에서 특히 주의해야 할 부분은 **같은 객체 내부에서 자신의 메서드를 호출하는 경우**다.

```java
public void orderAndNotify() {
    order();
}
```

외부에서 `order()`를 호출하면 Proxy를 통과한다.

```aiignore
    Client  
      ↓
    Proxy
      ↓
    order()
```

하지만 같은 객체 내부에서 호출하면 다음과 같이 동작한다.

```aiignore
  orderAndNotify()
        ↓  
    this.order()
```

`this.order()`는 Proxy를 거치지 않고 Target 내부에서 직접 호출된다.

따라서 `order()`에 AOP가 적용되어 있더라도 **Self Invocation에서는 Advice가 실행되지 않는다.**

이 특성은 `@Transactional`처럼 Spring AOP 기반 기능을 사용할 때도 중요하게 확인해야 한다.

<br>
<hr>
<br>

## 7. 정리

<br>

AOP의 핵심 목적은 **핵심 관심사와 공통 관심사를 분리하는 것**이다.

```aiignore
* 핵심 관심사
- 주문
- 결제
- 회원 처리

* 공통 관심사
- Logging
- Transaction
- Security
- 실행 시간 측정
```

Spring AOP에서는

```aiignore
Pointcut → 어디에 적용할 것인가

Advice → 무엇을 실행할 것인가

Aspect → Pointcut과 Advice를 모듈화

Proxy → 실제로 메서드 호출을 가로채 Advice 실행
```

의 관계로 이해할 수 있다.

Spring AOP는 Proxy 기반으로 동작하므로, **Self invocation처럼 Proxy를 거치지 않는 내부 호출에는 AOP가 적용되지 않는다는 점**도 함께 기억해야 한다.

<br>