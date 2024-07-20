### 팩토리 메서드 패턴

- 객체 생성 과정을 서브클래스로 캡슐화하여, 객체 생성 로직을 유연하게 확장할 수 있게 해주는 패턴
- 객체 생성 로직을 별도의 메서드로 캡슐화하여, 서브클래스에서 객체 생성 방식을 변경할 수 있도록 함
    - 팩토리 메소드 패턴을 이용하면 클래스의 인스턴스를 만드는 일을 서브클래스에게 맡기는 것
- 제품이 추가 되었을 때, 제품 생성 로직이 한 곳에 모여 있어 추가 및 변경하기 쉬운 구조를 제공
- 오버라이드된 메서드가 객용반환하는 패턴
- 의존 역전 원칙을 활
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
