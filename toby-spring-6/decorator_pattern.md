### 데코레이터 패턴

- 객체의 결합을 통해 기능을 동적으로 유연하게 확장 할 수 있게 해주는 패턴
- 개방 폐쇄 원칙과 의존 역전 원칙이 적용된 설계 패턴
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
- 프록시 패턴과 구현 방법이 같음
  - 다만 프록시 패턴은 클라이언트가 최종적으로 돌려받는 반환값을 조작하지 않고 그대로 전달
  - 데코레이터 패턴은 클라이언트가 받는 반환값에 장식을 덧입힘