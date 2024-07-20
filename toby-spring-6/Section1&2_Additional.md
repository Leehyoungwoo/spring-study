### BufferedReader 간단 구현

- 버퍼(Buffer): 데이터를 임시로 저장하는 메모리 공간
- 데이터를 한 글자씩 읽는 대신, 일정 크기의 버퍼에 데이터를 한 번에 읽어와서 처리 속도 높힘
- 동작 방식
    - 버퍼에서 데이터 읽어옴
    - 한글자씩 처리
    - 문자열로 결합

```java
package toss;

import java.io.*;

public class SimpleBufferedReader {

    private InputStreamReader inputStreamReader;
    private char[] buffer;
    private int bufferSize;
    private int bufferPosition;
    private int bufferCount;

    public SimpleBufferedReader(int bufferSize, InputStreamReader inputStreamReader) {
        this.bufferSize = bufferSize;
        this.inputStreamReader = inputStreamReader;
        this.buffer = new char[bufferSize];
        this.bufferPosition = 0;
        this.bufferCount = 0;
    }

    private void fillBuffer() throws IOException {
        bufferCount = inputStreamReader.read(buffer, 0, bufferSize);
        bufferPosition = 0;
    }

    public String readLine() throws IOException {
        StringBuilder line = new StringBuilder();
        boolean endLine = false;

        while (!endLine) {
            if (bufferPosition >= bufferCount) {
                fillBuffer();
                if (bufferCount == -1) {
                    return line.length() > 0 ? line.toString() : null;
                }
            }

            for(; bufferPosition < bufferCount; bufferPosition++) {
                char ch = buffer[bufferPosition];
                if (ch == '\n') {
                    endLine = true;
                    bufferPosition++;
                    break;
                } else if (ch == '\r') {
                    endLine = true;
                    if (bufferPosition + 1 < bufferCount && buffer[bufferPosition + 1] == '\n') {
                        bufferPosition++;
                    }
                    bufferPosition++;
                    break;
                } else {
                    line.append(ch);
                }
            }
        }
        return line.toString();
    }

    public void close() throws IOException {
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
    }
}

```

### 템플릿 메서드 패턴

- 기능의 뼈대(템플릿)와 실제 구현을 분리하는 패턴
- 알고리즘의 특정 단계만 제어하고 싶을 때 사용

![](https://vos.line-scdn.net/landpress-content-v2_1761/1669353654260.png?updatedAt=1669353654000)

- hook 메서드
    - 추상 클래스에서 선언하며, 기본적인 내용만 구현하거나 비워놓는 메서드
    - 하위 클래스에서 오버라이딩하면 다양한 용도로 사용할 수 있음
- 의존성 부패(Dependency Rot)
    - 의존성이 얽히고설켜 복잡하게 꼬여 있는 상황
- 장점
    - 중복코드를 줄일 수 있음
    - 자식 클래스의 역할을 줄여 핵심 로직의 관리가 용이
    - 좀더 코드를 객체지향적으로 구성할 수 있음
- 문제점
    - 추상 메서드의 개수가 많아지면 하위 클래스에서 구현해야 할 메서드가 많아지고, 코드의 복잡성이 증가
    - 너무 많은 추상 메서드를 사용하면 하위 클래스에서 모든 메서드를 구현해야 하므로, 코드의 유연성이 떨어질 수 있음
        - 상속이 깊어지면서 추상 메서드가 많아지면 구조 파악하기가 어려워짐
    - 상속 계층이 깊어질수록 유지보수가 어려워짐
- 상속이 깊어지고 추상 메서드가 많아지는 경우
    
    ```java
    abstract class AbstractProcessor {
        public final void process() {
            stepOne();
            stepTwo();
            stepThree();
            stepFour();
        }
    
        protected abstract void stepOne();
        protected abstract void stepTwo();
        protected abstract void stepThree();
        protected abstract void stepFour();
    }
    
    abstract class IntermediateProcessor extends AbstractProcessor {
        @Override
        protected void stepOne() {
            System.out.println("Intermediate step one");
        }
    
        @Override
        protected abstract void stepTwo();
    }
    
    abstract class FurtherIntermediateProcessor extends IntermediateProcessor {
        @Override
        protected void stepTwo() {
            System.out.println("Further intermediate step two");
        }
    
        @Override
        protected abstract void stepThree();
    }
    
    class ConcreteProcessor extends FurtherIntermediateProcessor {
        @Override
        protected void stepThree() {
            System.out.println("Concrete step three");
        }
    
        @Override
        protected void stepFour() {
            System.out.println("Concrete step four");
        }
    }
    ```
    

### 팩토리 메서드 패턴

- 객체 생성 과정을 서브클래스로 캡슐화하여, 객체 생성 로직을 유연하게 확장할 수 있게 해주는 패턴
- 객체 생성 로직을 별도의 메서드로 캡슐화하여, 서브클래스에서 객체 생성 방식을 변경할 수 있도록 함
    - 팩토리 메소드 패턴을 이용하면 클래스의 인스턴스를 만드는 일을 서브클래스에게 맡기는 것
- 제품이 추가 되었을 때, 제품 생성 로직이 한 곳에 모여 있어 추가 및 변경하기 쉬운 구조를 제공

![](https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/FactoryMethod.svg/1920px-FactoryMethod.svg.png)

- 구조
    - **Creator (추상 팩토리 클래스)**: 팩토리 메서드를 선언하여 객체 생성의 인터페이스를 제공
    - **ConcreteCreator (구체 팩토리 클래스)**: 팩토리 메서드를 구현하여 특정 제품 객체를 생성
    - **Product (제품 인터페이스 또는 추상 클래스)**: 생성될 객체의 인터페이스를 정의
    - **ConcreteProduct (구체 제품 클래스)**: 실제로 생성될 객체를 구현
- 단점
    - 상속이 깊어졌을 때, 구조 파악이 어렵고 변경이 어려워 유지보수성에 좋지 않으며 메서드 호출 시 오버헤드가 발생할 수 있음
- 예시 코드
    
    ```java
    // Product interface
    interface Product {
        void use();
    }
    
    // Concrete Product A
    class ConcreteProductA implements Product {
        public void use() {
            System.out.println("Using Product A");
        }
    }
    
    // Concrete Product B
    class ConcreteProductB implements Product {
        public void use() {
            System.out.println("Using Product B");
        }
    }
    
    // Factory
    abstract class Creator {
        public abstract Product factoryMethod();
    
        public void someOperation() {
            Product product = factoryMethod();
            product.use();
        }
    }
    
    // Concrete Factory A
    class ConcreteCreatorA extends Creator {
        public Product factoryMethod() {
            return new ConcreteProductA();
        }
    }
    
    // Concrete Factory B
    class ConcreteCreatorB extends Creator {
        public Product factoryMethod() {
            return new ConcreteProductB();
        }
    }
    
    // Client code
    public class Client {
        public static void main(String[] args) {
            Creator creator = new ConcreteCreatorA();
            creator.someOperation();
    
            creator = new ConcreteCreatorB();
            creator.someOperation();
        }
    }
    
    ```
    

### 상속을 통한 확장의 한계

- 강한 결합도로 과도한 사용은 유지보수성을 저하
    - 시스템이 복잡해지고 요구사항이 변화할 경우 문제가 될 수 있음
        - 메스드를 각각의 요구사항에 맞춰 수정해야 할 가능성이 크고 사용하는 클래스가 너무 많은 책임을 지게 할 수 있음
- 하위 클래스가 상위 클래스의 모든 기능을 제대로 구현하지 않으면 리스코프 치환 원칙을 위반
    - LSP를 어기면 클라이언트 코드에서 예상하지 못한 결과가 발생할 수 있음
- 상속은 수직적 확장에 적합하지만, 수평적 확장에는 적합하지 않을 수 있음
    - 독립적인 기능 추가에는 인터페이스를 활용하여 각 클래스가 독립적으로 기능을 확장할 수 있도록 하는 방식이 더욱 적절

### 전략 패턴

- 동일한 계열의 알고리즘군을 정의하고 각 알고리즘군을 캡슐화하여 클라이언트와 상관없이 독립적으로 알고리즘을 목적에 맞게 변경할 수 있게 하는 패턴.
- 알고리즘이 독립적으로 변경 가능하도록 하며, 클라이언트에 영향을 주지 않고 다양한 알고리즘을 적용할 수 있게 해줌

![](https://velog.velcdn.com/images/hero6027/post/7f771761-b293-4467-a405-d5e689723a38/strategy.png)

### 클래스 정의

- Strategy(Compositor) : 제공하는 모든 알고리즘에 대한 공통의 연산들을 인터페이스화, Context는 인터페이스화된 Strategy를 통해서 구체화된 실제 알고리즘을 사용
- ImplA,ImplB : Strategy 인터페이스의 공통으로 묶어놓은 알고리즘을 실제로 구현
- Context : Strategy에 있는 참조자(ImplA,ImplB)를 관리
    - Strategy의 서브클래스 인스턴스를 가지고 있음으로써 구체화
- 동작
    - Client는 필요하다고 판단되는 알고리즘을 선정하여 Context에게 요청
    - Context 클래스와 Strategy 클래스는 의사교환을 통해 선택한 알고리즘을 구현
    - Context는 연산에 필요한 데이터를 Strategy 클래스에 넘겨줌
        - 실제로 구현된 Impl 클래스는 Context와 함께 동작
- 예시 코드
    
    ```java
    // 전략 인터페이스
    interface DeliveryStrategy {
        int calculateShippingCost(int orderAmount);
    }
    
    // 구체적인 전략: 일반 배송
    class RegularDeliveryStrategy implements DeliveryStrategy {
        @Override
        public int calculateShippingCost(int orderAmount) {
            return orderAmount < 100 ? 5 : 0;
        }
    }
    
    // 구체적인 전략: 특급 배송
    class ExpressDeliveryStrategy implements DeliveryStrategy {
        @Override
        public int calculateShippingCost(int orderAmount) {
            return orderAmount * 2;
        }
    }
    
    // 구체적인 전략: 무료 배송
    class FreeDeliveryStrategy implements DeliveryStrategy {
        @Override
        public int calculateShippingCost(int orderAmount) {
            return 0;
        }
    }
    
    // 주문 클래스 (컨텍스트)
    class Order {
        private DeliveryStrategy deliveryStrategy;
    
        public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
            this.deliveryStrategy = deliveryStrategy;
        }
    
        public int calculateTotalOrderCost(int orderAmount) {
            int shippingCost = deliveryStrategy.calculateShippingCost(orderAmount);
            return orderAmount + shippingCost;
        }
    }
    
    // 메인 클래스
    public class Main {
        public static void main(String[] args) {
            Order order = new Order();
    
            // Regular delivery 선택
            order.setDeliveryStrategy(new RegularDeliveryStrategy());
            int totalCost1 = order.calculateTotalOrderCost(80); // 80 + 5 = 85
            System.out.println("Total cost with regular delivery: " + totalCost1);
    
            // Express delivery 선택
            order.setDeliveryStrategy(new ExpressDeliveryStrategy());
            int totalCost2 = order.calculateTotalOrderCost(100); // 100 + (100 * 2) = 300
            System.out.println("Total cost with express delivery: " + totalCost2);
    
            // Free delivery 선택
            order.setDeliveryStrategy(new FreeDeliveryStrategy());
            int totalCost3 = order.calculateTotalOrderCost(50); // 50 + 0 = 50
            System.out.println("Total cost with free delivery: " + totalCost3);
        }
    }
    
    ```
    
- 전략 패턴의 장점
    - 알고리즘을 정의하고 캡슐화하여 런타임 시에 알고리즘을 선택하는 데 사용됨
    - 알고리즘을 쉽게 변경 및 대체할 수 있으므로 **유연함, 확장성, 재사용성**
    - 각각 알고리즘을 독립적으로 테스트할 수 있으므로 **용이함**
- 전략 패턴의 단점
    - 클라이언트가 전략을 명시적으로 선택해야 함
    - 런타임 시 알고리즘 선택하는데 추가적인 오버헤드 발생 가능
    - 컨텍스트와 전략 간의 결합도 증가

### 싱글톤 레지스트리

- 싱글톤 레지스트리는 디자인 패턴 중 하나인 싱글톤 패턴의 구현 방식을 보완한 기능으로 직접 싱글톤 형태의 오브젝트를 만들고 관리하는 기능
- 스프링은 기본적으로 별다른 설정이 없다면 내부에서 생성하는 빈 오브젝트를 모두 싱글톤으로 만들기 때문에 같은 객체를 여러번 호출해도 동일한 객체를 반환
- **싱글톤 패턴의 문제점**
    - 싱글톤 패턴은 생성자를 private으로 제한, private 생성자를 가진 클래스는 객체지향의 장점인 상속과 이를 이용한 다형성을 적용할 수 없음
    - 싱글톤은 초기화 과정에서 생성자 등을 통해 사용할 오브젝트를 직접 주입하기도 힘들기 때문에 필요한 오브젝트는 직접 오브젝트를 만들어 사용할 수 밖에 없음
        - 테스트용 오브젝트로 대체하기가 힘듦
    - 여러 개의 JVM에 분산돼서 설치가 되는 경우에도 각각 독립적으로 오브젝트가 생기기 때문에 싱글톤으로서의 가치를 보장하지 못함
    - 싱글톤의 static 메소드를 이용해 언제든지 싱글톤에 접글할 수 있어 전역 상태로 사용될 수 있음
    - 예시코드
        
        ```java
        class suvCar {
        
           private static final INSTANCE = new suvCar();
        
           private static suvCar getInstance() { return INSTANCE; }
        
           private suvCar(){}
        
        }
        ```
        
- 싱글톤 레지스트리는 private 생성자를 사용해야 하는 방법이 아닌 평범한 자바 클래스를 싱글톤으로 활용할 수 있게 해줌
- IoC 방식의 컨테이너를 사용해서 생성과 관계설정, 사용 등에 대한 제어권을 컨테이너에게 넘기면서 손쉽게 싱글톤 방식으로 관리
- 주의점
    - 무상태(stateless)로 설계해야 함
        - 특정 클라이언트에 의존적인 필드가 있으면 안됨
        - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안됨
        - 가급적 읽기만 가능해야 함
        - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 함

### 데코레이터 패턴

- 객체의 결합을 통해 기능을 동적으로 유연하게 확장 할 수 있게 해주는 패턴
- 구현 방식
    - 구성 요소 인터페이스(Component Interface): 기본 동작을 정의하는 인터페이스나 추상 클래스
    - 구체 구성 요소(Concrete Component): 기본 동작을 구현하는 클래스
    - 데코레이터(Decorator): 구성 요소 인터페이스를 구현하며, 구체 구성 요소를 포함하는 클래
        - 기본 동작을 호출한 후 추가적인 동작을 수행
    - 구체 데코레이터(Concrete Decorators): 데코레이터의 서브클래스로, 추가적인 동작을 구현

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/exqO8t/btqAmt2l8MC/b6PrWbB5HTzMoFnflV6Vw1/img.png)

- 예시 코드
    
    ```java
    // 구성 요소 인터페이스
    public interface Component {
        void operation();
    }
    
    // 구체 구성 요소
    public class ConcreteComponent implements Component {
        public void operation() {
            System.out.println("기본 동작");
        }
    }
    
    // 데코레이터
    public abstract class Decorator implements Component {
        protected Component component;
    
        public Decorator(Component component) {
            this.component = component;
        }
    
        public void operation() {
            component.operation();
        }
    }
    
    // 구체 데코레이터
    public class ConcreteDecorator extends Decorator {
        public ConcreteDecorator(Component component) {
            super(component);
        }
    
        public void operation() {
            super.operation();
            System.out.println("추가 동작");
        }
    }
    
    // 사용 예시
    public class Client {
        public static void main(String[] args) {
            Component component = new ConcreteComponent();
            Component decoratedComponent = new ConcreteDecorator(component);
            decoratedComponent.operation();
        }
    }
    
    ```
    

### **프록시 패턴 (Proxy Pattern)**

- 대상 원본 객체를 대리하여 대신 처리하게 함으로써 로직의 흐름을 제어하는 행동 패턴

![](https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Proxy_pattern_diagram.svg/1200px-Proxy_pattern_diagram.svg.png)

- 구현방식
    - 구성 요소 인터페이스(Subject Interface): 실제 객체와 프록시 객체가 구현하는 공통 인터페이스
    - 구체 구성 요소(Real Subject): 실제 객체로, 실제 비즈니스 로직을 구현
    - 프록시(Proxy): 구성 요소 인터페이스를 구현하며, 실제 객체에 대한 참조를 포함
        - 프록시는 요청을 실제 객체에 전달하거나, 추가적인 기능을 수행
- 예시 코드
    
    ```java
    // 구성 요소 인터페이스
    public interface Subject {
        void request();
    }
    
    // 구체 구성 요소
    public class RealSubject implements Subject {
        public void request() {
            System.out.println("실제 요청 처리");
        }
    }
    
    // 프록시
    public class Proxy implements Subject {
        private RealSubject realSubject;
    
        public void request() {
            if (realSubject == null) {
                realSubject = new RealSubject();
            }
            System.out.println("프록시를 통한 요청");
            realSubject.request();
        }
    }
    
    // 사용 예시
    public class Client {
        public static void main(String[] args) {
            Subject proxy = new Proxy();
            proxy.request();
        }
    }
    ```
    
    ### 차이점
    
    - **데코레이터 패턴은 객체에 새로운 기능을 동적으로 추가하는 데 사용**
    - **프록시 패턴은 객체에 대한 접근을 제어하거나 추가적인 기능을 제공하는 데 사용**
    
    ### 의존성 역전 원칙
    
    - 상위 수준의 모듈은 하위 수준의 모듈에 의존해서는 안 된다. 둘 모두 추상화에 의존해야 한다.
        - 인터페이스 소유권의 역전이 필요
            - Separated Interface 패턴
    - 추상화는 구체적인 사항에 의존해서는 안된다. 구체적인 사항은 추상화에 의존해야 한다.
    
    ```java
    // 추상화
    public interface MessageService {
        void sendMessage(String message);
    }
    
    // 저수준 모듈
    public class EmailService implements MessageService {
        @Override
        public void sendMessage(String message) {
            // 이메일 전송 로직
        }
    }
    
    // 고수준 모듈
    public class Notification {
        private MessageService messageService;
    
        public Notification(MessageService messageService) {
            this.messageService = messageService;
        }
    
        public void notifyUser(String message) {
            messageService.sendMessage(message);
        }
    }
    ```
