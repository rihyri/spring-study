<br>

# 0. 객체 지향의 기본

<br>

Spring을 본격적으로 이해하기 전에 먼저 객체지향 설계에서 자주 등장하는 개념을 정리한다.
이번 예제에서는 할인 정책 코드를 통해 다음 내용을 살펴본다.

- 다형성 (Polymorphsim)
- 강한 결합과 느슨한 결합
- OCP(Open-Closed Principle)
- 객체지향 설계와 Spring DI의 관계

<br>

<hr>

<br>

## 1. 다형성  (Polymorphism)

<br>

다형성은 하나의 타입으로 여러 구현 객체를 다룰 수 있는 객체지향의 특성이다.

```java
public interface DiscountPolicy {
    int discount(int price);
}
```
`DiscountPolicy`를 구현하는 서로 다른 할인 정책을 만들 수 있다.

```java
public class FixedDiscountPolicy implements DiscountPolicy {
    // 고정 금액 할인
}

public class RateDiscountPolicy implements DiscountPolicy {
    // 비율 할인
}
```
두 객체는 서로 다른 클래스지만 같은 인터페이스 타입으로 사용할 수 있다.
```java
DiscountPolicy fixedPolicy = new FixedDiscountPolicy();
DiscountPolicy ratePolicy = new RateDiscountPolicy(10);
```

사용하는 쪽에서는 `DiscountPolicy`만 알고 있으면 되고, 실제 동작은 전달된 구현체에 따라 달라진다.

```aiignore
    DiscountPolicy
           ▲ 
    ┌──────┴──────┐ 
    │             │ 
FixedPolicy  RatePolicy
```
이러한 구조는 구체적인 구현이 아닌 **역할(인터페이스)과 계약에 의존**하도록 설계할 수 있게 해준다.

<br>
<hr>
<br>

## 2. 강한 결합과 느슨한 결합

<br>

### 강한 결합

객체 내부에서 특정 구현체를 직접 생성하면 두 클래스 사이의 결합도가 높아진다.

```java
private final FixedDiscountPolicy discountPolicy = new FixedDiscountPolicy();
```

할인 정책을 `RateDiscountPolicy`로 변경하려면 사용하는 클래스의 코드도 수정해야 한다. 

```aiignore
    OrderService
         │ 
         ▼
  FixedDiscountPolicy
```
구현체의 변경이 사용하는 객체의 변경으로 이어지는 구조다.

<br>

### 느슨한 결합
구체 클래스 대신 인터페이스에 의존하도록 코드를 변경할 수 있다.

```java
private final DiscountPolicy discountPolicy;

public OrderService(DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy;
}
```

`OrderService`는 어떤 할인 정책이 사용되는지 알 필요가 없다.

```java
new OrderService(new FixedDiscountPolicy());
```
또는
```java
new OrderService(new RateDiscountPolicy(10));
```
처럼 외부에서 구현체를 전달할 수 있다.

```aiignore
    OrderService 
          │ 
          ▼ 
    DiscountPolicy 
    ▲            ▲ 
    │            │ 
FixedPolicy   RatePolicy
```

이렇게 구체적인 구현에 대한 직접적인 의존을 줄이면 객체 사이의 결합도를 낮출 수 있다.

<br>
<hr>
<br>

## 3. OCP - Open Closed Principle

<br>

OCP는 객체지향 설계 원칙인 SOLID 중 하나이다.

> 소프트웨어 요소는 확장에는 열려 있어야 하고, 변경에는 닫혀 있어야 한다.

현재 `OrderService`는 `DiscountPolicy` 인터페이스에 의존한다.

```java
public class OrderService {
    
    private final DiscountPolicy discountPolicy;
    
    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```
새로운 할인 정책이 필요하다면 `DiscountPolicy`를 구현하는 클래스를 추가하면 된다.

```java
public class SpecialDiscountPolicy implements DiscountPolicy {
    
    @Override
    public int discount(int price) {
        // 새로운 할인 정책
    }
}
```

새로운 기능은 **확장**되었지만 기존 `OrderService`의 코드는 변경하지 않는다.

```aiignore
    DiscountPolicy 
          ▲ 
     ┌────┼────┐ 
     │    │    │ 
   Fixed Rate Special
```

다형성을 활용하면 이러한 변경에 유연한 구조를 만들 수 있다. 

<br>
<hr>
<br>

## 4. Spring과의 관계

<br>

현재 예제에서는 main() 메서드에서 직접 객체를 생성하고 의존성을 전달한다.

```java
DiscountPolicy discountPolicy = new RateDiscountPolicy(10);

OrderService orderService = new OrderService(discountPolicy);
```

여기서 중요한 점은 `OrderService`가 자신이 사용할 객체를 직접 생성하지 않는다는 것이다.

```aiignore
    객체 생성
        ↓
  DiscountPolicy 구현체
        ↓
  OrderService에 전달
```
이처럼 필요한 객체를 외부에서 전달받는 것을 **Dependency Injection(DI)**라고 한다. 

현재는 `main()`에서 직접 객체를 생성하고 연결하지만, Spring을 사용하면 **Spring Container가 객체 생성과 의존 관계 연결을 대신 담당한다.**

```aiignore
* 현재 

main()
├─ DiscountPolicy 생성
└─ OrderService에 전달


* Spring

Spring Container
├─ Bean 생성
└─ 의존성 주입
```

다형성, 느슨한 결합, OCP에 대한 이해는 이후 Spring의 **IoC와 DI**를 이해하기 위한 기반이 된다.

<br>
<hr>
<br>

## 실행

`OopStudyMain`의 `main()` 메서드를 실행하면 고정 할인과 비율 할인을 각각 적용한 결과를 확인할 수 있다.

```aiignore
===== 1. 강한 결합 =====
상품 가격: 20000원 
최종 가격: 19000원 

===== 2. 다형성 - 고정 할인 ===== 
상품 가격: 20000원 
할인 정책: FixedDiscountPolicy 
최종 가격: 19000원 

===== 3. 다형성 - 비율 할인 ===== 
상품 가격: 20000원 
할인 정책: RateDiscountPolicy 
최종 가격: 18000원
```
