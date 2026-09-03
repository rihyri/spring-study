<br>

# 3. Spring Proxy Pattern

<br>

Proxy는 실제 객체(Target)를 대신해서 요청을 받고, 필요한 부가 작업을 수행한 뒤 실제 객체에게 요청을 전달하는 객체이다.

Spring에서는 AOP, `@Transactional`, `@Cacheable` 등 여러 기능에서 Proxy가 중요한 역할을 한다.

이번 예제에서는 다음 내용을 확인할 수 있다.

- Proxy Pattern
- Target과 Proxy의 관계
- Static Proxy
- Spring `ProxyFactory`
- JDK Dynamic Proxy와 CGLIB Proxy

<br>
<hr>
<br>

## 1. Proxy Pattern

<br>

Proxy의 기본 구조는 다음과 같다.

```aiignore
    Client
      ↓
    Proxy
      ↓
    Target
```

클라이언트는 Target을 직접 호출하지 않고 Proxy를 호출한다.

Proxy는 실제 기능을 직접 수행하는 것이 아니라 Target에게 요청을 전달하면서 앞뒤에 추가 기능을 넣을 수 있다.

```java
public void order(String productName, int quantity) {
    
    // 부가 기능
    long startTime = System.currentTimeMillis();
    
    // 실제 기능
    target.order(productName, quantity);
    
    // 부가 기능
    long endTime = System.currentTimeMillis();
}
```

Target은 주문과 같은 **핵심 비즈니스 로직**에 집중하고, Proxy는 로그나 실행 시간 측정 같은 **부가 기능**을 담당할 수 있다.

```aiignore
  Proxy
    ├─ Logging
    ├─ Time Check
    ├─ Security
    │
    └─ Target
        └─ Business Logic
```

<br>
<hr>
<br>

## 2. Static Proxy 

<br>

가장 단순한 방법은 Proxy 클래스를 직접 작성하는 것이다.

```java
public class OrderServiceProxy implements OrderService {
    
    private final OrderService target;
    
    public OrderServiceProxy(OrderService target) {
        this.target = target;
    }
}
```
Target과 Proxy가 같은 인터페이스를 구현하기 때문에 사용하는 입장에서는 동일한 타입으로 다룰 수 있다.

```java
OrderService target = new RealOrderService();

OrderService proxy = new OrderServiceProxy(target);
```

하지만 메서드가 많아지면 각 Proxy 메서드마다 비슷한 코드가 반복된다.

```aiignore
order()
    → 실행 시간 측정
    → target.order()

cancel()
    → 실행 시간 측정
    → target.cancel()

payment()
    → 실행 시간 측정
    → target.payment()
```

이 문제를 줄이기 위해 런타임에 Proxy 객체를 동적으로 만들 수 있다.

<br>
<hr>
<br>

## 3. Spring ProxyFactory

<hr>

Spring에서는 `ProxyFactory`를 이용해 Proxy를 직접 생성할 수 있다.

```java
ProxyFactory factory = new ProxyFactory(target);

factory.addAdvice(new ExecutionTimeInterceptor());

OrderService proxy = (OrderService) factory.getProxy();
```

이 경우 `OrderServiceProxy`와 같은 클래스를 직접 만들 필요가 없다.

메서드 호출을 가로채는 부가 기능은 `MethodInterceptor`로 분리할 수 있다.

```java
public Object invoke(MethodInvocation invocation) throws Throwable {
    
    System.out.println("메서드 실행 전");
    
    Object result = invocation.proceed();
    
    System.out.println("메서드 실행 후");
    
    return result;
}
```

`invocation.proceed()`가 호출되면 실제 Target 메서드가 실행된다.

```aiignore
    Client 
      │   
      ▼ 
  Spring Proxy 
      │ 
      ├─ Interceptor 실행 
      │ 
      ▼ Target Method
```

<br>
<hr>
<br>

## 4. JDK Dynamic Proxy와 CGLIB

<br>

Spring은 대표적으로 두 가지 방식으로 Proxy를 생성한다.

<br>

### JDK Dynamic Proxy

인터페이스를 기본으로 Proxy를 만든다.

```aiignore
        OrderService
           ▲    ▲ 
           │    │
        Proxy RealOrderService
```

Proxy 객체는 Target 클래스 자체가 아니라 **인터페이스 타입을 기반으로 만들어진 객체**다.

```java
OrderService proxy = (OrderService) factory.getProxy();
```

런타임에 생성된 클래스는 다음과 같은 형태로 확인할 수 있다.

```aiignore
jdk.proxy2.$Proxy...
```

<br>

### CGLIB Proxy

CGLIB는 Target 클래스를 상속하는 방식으로 Proxy 클래스를 생성한다.

```aiignore
RealOrderService 
      ▲ 
      │ extends 
      │ 
  CGLIB Proxy
```

`ProxyFactory`에서는 다음과 같이 클래스 기반 Proxy를 강제할 수 있다.

```java
factory.setProxyTargetClass(true);
```

Proxy 클래스는 대략 다음과 같은 형태로 생성된다.

```aiignore
RealOrderService$$SpringCGLIB$$...
```

클래스를 상속하는 방식이기 때문에 `final` 클래스는 Proxy 대상이 될 수 없고, `final` 메서드 역시 Override 할 수 없어 가로챌 수 없다.

<br>
<hr>
<br>

## 5. 왜 Proxy를 사용할까?

<br>

Proxy의 중요한 목적 중 하나는 **핵심 기능과 부가 기능을 분리하는 것**이다.

Proxy를 사용하지 않는다면 비즈니스 코드 안에 부가 기능이 섞일 수 있다.


```java
public void order() {
    
    // Logging
    
    // Time Check
    
    // 실제 주문 처리
    
    // Logging
}
```

Proxy를 사용하면 구조를 분리할 수 있다.

```aiignore
    ExecutionTimeInterceptor
                │
                │ 부가 기능
                ▼
              Proxy
                │
                ▼
         RealOrderService
                │
                └─ 주문 처리
```

`RealOrderService`는 주문 처리 자체에만 집중할 수 있다.

<br>
<hr>
<br>

## 6. 정리

<br>

```aiignore
* Proxy 없이 

  Client
    │
    ▼
  Target 


* Proxy 사용

  Client
    │
    ▼
  Proxy
    │
    ├─ 부가 기능
    │
    ▼
  Target
```

Spring의 Proxy는 런타임에 생성할 수 있으며 대표적으로 다음 두 방식을 사용한다.

<br>

<table>
    <thead>
        <th>방식</th>
        <th>특징</th>
    </thead>
    <tbody>
        <tr>
            <td>JDK Dynamic Proxy</td>
            <td>인터페이스 기반 Proxy</td>
        </tr>
        <tr>
            <td>CGLIB</td>
            <td>클래스 상속 기반 Proxy</td>
        </tr>
    </tbody>
</table>

<br>

Proxy는 Spring AOP를 이해하기 위한 핵심 기반이다.

<br>