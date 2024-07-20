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

### 상속을 통한 확장의 한계

- 강한 결합도로 과도한 사용은 유지보수성을 저하
    - 시스템이 복잡해지고 요구사항이 변화할 경우 문제가 될 수 있음
        - 메스드를 각각의 요구사항에 맞춰 수정해야 할 가능성이 크고 사용하는 클래스가 너무 많은 책임을 지게 할 수 있음
- 하위 클래스가 상위 클래스의 모든 기능을 제대로 구현하지 않으면 리스코프 치환 원칙을 위반
    - LSP를 어기면 클라이언트 코드에서 예상하지 못한 결과가 발생할 수 있음
- 상속은 수직적 확장에 적합하지만, 수평적 확장에는 적합하지 않을 수 있음
    - 독립적인 기능 추가에는 인터페이스를 활용하여 각 클래스가 독립적으로 기능을 확장할 수 있도록 하는 방식이 더욱 적절
