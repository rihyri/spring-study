<br>

# 1. Bean Lifecycle & Scope 

<br>

Spring Container는 Bean의 생성부터 소멸까지 생명주기를 관리한다.
또한 Bean을 언제 생성하고 얼마 동안 유지할지는 **Scope**를 통해 결정할 수 있다.

이번 예제에서는 다음 내용을 다룬다.

- Spring Bean의 생명주기
- `@PostConstruct`, `@PreDestroy`
- Bean Scope
- Singleton과 Prototype의 생명주기 차이

<br>
<hr>
<br>

## 1. Bean Lifecycle

<br>

일반적인 Java 객체는 직접 생성하고 사용한다.

```java
MyObject object = new MyObject();
```

반면 Spring Bean은 객체 생성 관리의 주체가 `Spring Container`다.

Bean의 전체 생명주기를 단순화하면 다음과 같다.

```aiignore
    Spring Container 생성 
            ↓
        Bean 생성
            ↓
        의존성 주입
            ↓
    초기화 @PostConstruct
            ↓
        Bean 사용
            ↓
    소멸 @PreDestroy
            ↓
    Spring Container 종료
```

실제 Spring 내부에는 BeanPostProcessor 등의 추가 과정이 존재하지만, 이번에는 **생성 → 초기화 → 사용 → 소멸** 흐름을 중심으로 이해한다.

<br>
<hr>
<br>

## 2. `@PostConstruct`와 `@PreDestroy`

<br>

Bean이 생성된 직후 수행할 작업이 있다면 `@PostConstruct`를 사용할 수 있다.

```java
@PostConstruct
public void init() {
    System.out.println("Bean 초기화");
}
```

의존성 주입이 완료된 이후 호출되므로 초기 설정이나 준비 작업에 사용할 수 있다.

Bean이 소멸되기 전에는 `@PreDestroy`를 사용할 수 있다.

```java
@PreDestroy
public void destroy() {
    System.out.println("Bean 소멸");
}
```

연결이나 리소스를 정리해야 할 때 사용할 수 있다.

Spring에서는 `InitializingBean`, `DisposableBean`과 같은 인터페이스도 제공하지만, 일반적인 애플리케이션에서는 특정 Spring 인터페이스에 Bean을 의존시키지 않는 `@PostConstruct`, `@PreDestroy` 방식이 권장된다.

<br>
<hr>
<br>

## 3. Bean Scope

<br>

Scope는 **Spring Bean이 생성되고 유지되는 범위**를 의미한다.

Spring Framework는 다음과 같은 Scope를 지원한다.

<br>

<table>
    <thead>
        <th>Scope</th>
        <th>범위</th>
    </thead>
    <tbody>
        <tr>
            <td>singleton</td>
            <td>Spring Container당 하나의 Bean</td>
        </tr>
        <tr>
            <td>prototype</td>
            <td>Bean을 요청할 때마다 새로운 객체 생성</td>
        </tr>
        <tr>
            <td>request</td>
            <td>하나의 HTTP Request</td>
        </tr>
        <tr>
            <td>session</td>
            <td>하나의 HTTP Session</td>
        </tr>
        <tr>
            <td>application</td>
            <td>하나의 ServletContext</td>
        </tr>
        <tr>
            <td>websocket</td>
            <td>하나의 WebSocket session</td>
        </tr>
    </tbody>
</table>

<br>

기본 Scope는 `singleton`이다.

```java
@Component
public class SingletonLifecycleBean {
}
```

별도의 Scope를 지정하지 않으면 singleton으로 관리된다.

Scope를 변경하려면 `@Scope`를 사용할 수 있다.

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeLifecycleBean {
}
```

`request`, `session`, `application`, `websocket`은 웹 환경에서 사용하는 Scope이므로 이번 예제에서는 다루지 않는다. 

<br>
<hr>
<br>

## 4. Scope에 따른 Lifecycle 차이

<br>

이번 예제에서 주목할 부분은 `singleton`과 `prototype`의 소멸 지점이다.

### Singleton
```aiignore
Container 생성
    ↓
Bean 생성
    ↓
@PostConstruct
    ↓
Bean 사용
    ↓
@PreDestroy
    ↓
Container 종료
```
Spring Container가 생성부터 소멸까지 Bean의 생명주기를 관리한다.

<br>

### Prototype
```aiignore
getBean()
↓
@PostConstruct
↓
Bean 반환
↓
이후 관리는 사용하는 쪽의 책임
```

Prototype Bean의 생성과 초기화는 Spring이 담당하지만, Bean을 반환한 이후의 생명주기는 Spring이 관리하지 않는다.

따라서 Prototype Bean에 `@PreDestroy`가 존재하더라도 Spring Container가 해당 메서드를 자동으로 호출하지 않는다.

이번 예제에서

```java
@PreDestroy
public void destroy() {
    System.out.println("[Prototype] @PreDestroy - 소멸");
}
```

를 작성했지만 실행 결과에 출력되지 않은 이유가 바로 이것이다.

<br>
<hr>
<br>

## 5. 정리 

<br>

Bean Lifecycle에서 핵심은 **객체의 생성과 소멸을 개발자가 직접 관리하지 않고 Spring Container가 관리한다는 점**이다.

```aiignore
* 일반 Java 객체
개발자 → 객체 생성 → 사용 → 정리

* Spring Bean
Spring Container → Bean 생성 → 초기화 → 관리 → 소멸
```

그리고 Scope는 Bean이 **어떤 범위에서 생성되고 유지될지**를 결정한다.
