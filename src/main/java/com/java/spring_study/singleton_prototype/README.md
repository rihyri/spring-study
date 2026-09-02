<br>

# 2. Singleton vs Prototype

<br>

Spring Bean의 Scope는 **Bean이 생성되고 공유되는 범위**를 결정한다.

Spring의 기본 Scope는 `Singleton`이며, 필요에 따라 `prototype` 등의 다른 Scope를 사용할 수 있다.

이번 예제에서는 다음 내용을 확인할 수 있다.

- Singleton Scope
- Prototype Scope
- 상태를 가진 Bean에서의 차이
- Singleton Bean에 Prototype Bean을 주입할 때의 문제
- `ObjectProvider`를 이용한 Prototype Bean 조회

<br>
<hr>
<br>

## 1. Singleton Scope

<br>

Spring의 기본 Bean Scope는 `singleton`이다.

```java
@Component
public class SingletonCounter {
}
```

Singleton Bean은 **하나의 Spring Container에서 하나의 Bean 인스턴스를 공유**한다.

```java
SingletonCounter bean1 = context.getBean(SingletonCounter.class);
SingletonCounter bean2 = context.getBean(SingletonCounter.class);

System.out.println(bean1 == bean2); // true
```

```aiignore
* Spring Container

    SingletonCounter
            ▲ 
            │ 
       ┌────┴────┐ 
       │         │
      bean1   bean2
```

따라서 Singleton Bean이 가변 상태를 가지고 있다면 그 상태 역시 여러 사용자가 공유하게 된다.

```aiignore
bean1.increase();   // 1
bean2.increase();   // 2
```

이 때문에 일반적으로 Singleton Bean은 **공유되는 가변 상태를 가지지 않는 stateless한 형태로 설계하는 것이 중요하다.**

> Spring Singleton은 GoF의 Singleton Pattern과 동일한 개념이 아니다.
> <br> Spring Singleton의 범위는 하나의 `ApplicationContext`와 하나의 Bean 정의를 기준으로 한다.

<br>
<hr>
<br>

## 2. Prototype Scope

<br>

Prototype Scope는 Bean을 요청할 때마다 새로운 객체를 생성한다.

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeCounter {
}

PrototypeCounter bean1 = context.getBean(PrototypeCounter.class);
PrototypeCounter bean2 = context.getBean(PrototypeCounter.class);

System.out.println(bean1 == bean2); // false
```

```aiignore
* Spring Container 

getBean() ──→ PrototypeCounter #1
getBean() ──→ PrototypeCounter #2
getBean() ──→ PrototypeCounter #3
```

각 객체가 독립적이므로 객체 내부에 존재하는 상태도 공유되지 않는다.

```aiignore
Prototype #1 → count = 2
Prototype #2 → count = 3
```

Spring 공식 문서에서는 일반적인 기준으로 **stateless Bean은 singleton, stateful Bean은 prototype 사용**을 권장한다.

<br>
<hr>
<br>

## 3. Singleton에 Prototype을 주입한다면?

<br>

다음과 같이 Singleton Bean이 Prototype Bean을 의존한다고 가정한다.

```java
@Service
public class DirectPrototypeService {
    
    private final PrototypeCounter prototypeCounter;
    
    public DirectPrototypeService (PrototypeCounter prototypeCounter) {
        this.prototypeCounter = prototypeCounter;
    }
}
```

`PrototypeCounter`가 Prototype Scope이므로 `increase()`를 호출할 때마다 새로운 객체가 만들어질 것처럼 보일 수 있다.

하지만 실제로는 그렇지 않다.

```aiignore
    Spring Container 시작
            ↓
   DirectPrototypeService 생성
            ↓
    PrototypeCounter 생성
            ↓
    생성자를 통해 한 번 주입
            ↓
DirectPrototypeService가 계속 같은 객체 사용
```

의존성 주입은 Singleton Bean을 생성하는 시점에 이루어진다.
따라서 다음 두 호출은 같은 Prototype 객체를 사용한다.

```aiignore
directService.increase();   // Prototype #1 / count = 1
directService.increase();   // Prototype #1 / count = 2
```

Prototype은 **객체를 사용할 때마다** 새 객체를 만든다는 뜻이 아니라, **Spring Container에 Bean을 요청할 때마다** 새 객체를 생성한다는 의미이다.

<br>
<hr>
<br>

## 4. ObjectProvider 

<br>

Singleton Bean에서 사용할 때마다 새로운 Prototype Bean이 필요하다면, Spring Container에 Bean을 다시 요청해야 한다.

이러한 경우 `ObjectProvider`를 사용할 수 있다.

```java
private final ObjectProvider<PrototypeCounter> counterProvider;
```

필요한 시점에 `getObject()`를 호출하면 Spring Container에서 Prototype Bean을 새롭게 가져온다.

```java
PrototypeCounter counter = counterProvider.getObject();
```


```aiignore
ProviderPrototypeService (Singleton) 
                │ 
                ├── getObject() → Prototype #1 
                │ 
                ├── getObject() → Prototype #2 
                │ 
                └── getObject() → Prototype #3
```

따라서 다음과 같이 각각 다른 객체가 사용된다.

```aiignore
Prototype #1 / count = 1
Prototype #2 / count = 1
Prototype #3 / count = 1
```

<br>
<hr>
<br>

## 5. 정리 

<br>

<table>
    <thead>
        <th></th>
        <th>Singleton</th>
        <th>Prototype</th>
    </thead>
    <tbody>
        <tr>
            <td>기본 Scope</td>
            <td>O</td>
            <td>X</td>
        </tr>
        <tr>
            <td>객체 생성</td>
            <td>Bean 정의당 하나</td>
            <td>요청할 때마다</td>
        </tr>
        <tr>
            <td>객체 공유</td>
            <td>O</td>
            <td>X</td>
        </tr>
        <tr>
            <td>상태 공유</td>
            <td>가능</td>
            <td>객체별 독립</td>
        </tr>
        <tr>
            <td>Spring의 소멸 관리</td>
            <td>O</td>
            <td>X</td>
        </tr>
    </tbody>
</table>

<br>
핵심은 **Scope가 단순히 객체의 개수를 결정하는 것이 아니라 객체의 상태가 어디까지 공유되는지를 결정한다는 점**이다.

또한 Prototype Bean을 Singleton Bean에 직접 주입하면 새로운 객체가 계속 생성되는 것이 아니다.

```aiignore
Singleton + Prototype 직접 주입
─→ 최초에 주입된 Prototype을 계속 사용

Singleton + ObjectProvider<Prototype>
─→ 필요한 시점마다 새로운 Prototype 조회
```

<br>