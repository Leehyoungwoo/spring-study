### 전략 패턴

- 동일한 계열의 알고리즘군을 정의하고(전략 메서드를 가진 전략 객체) 각 알고리즘군을 캡슐화하여 클라이언트와 상관없이 독립적으로 알고리즘을 목적에 맞게 변경할 수 있게 하는 패턴.
- 알고리즘이 독립적으로 변경 가능하도록 하며, 클라이언트에 영향을 주지 않고 다양한 알고리즘을 적용할 수 있게 해줌
- 클라이언트가 전략을 생성해 전략을 실행할 컨텍스트에 주입하는 패턴
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
  - Strategy(전략)
    - 알고리즘을 정의하는 인터페이스나 추상 클래스
    - 구체적인 알고리즘을 어떻게 실행할지 정의
    ```Java
    package java.util;
    // Comparator 인터페이스 정의
    public interface Comparator<T> {
        int compare(T o1, T o2);  // 두 객체를 비교하여 정렬 순서를 결정
    }
    ```
  - ConcreteStrategy(구체적인 전략)
    - Comparator를 구현한 클래스들은 구체적인 정렬 전략을 제공
    - 문자열의 알파벳 순서로 정렬하거나 숫자의 크기 순서로 정렬할 수 있음
    ```java
    import java.util.Comparator;
    
    // 이름에 따라 정렬하는 Comparator
    public class NameComparator implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }
    ```
  - Context (컨텍스트)
    - Collections.sort 메서드는 Comparator를 사용하여 리스트를 정렬
    -  메서드는 정렬 로직을 실행하기 위한 컨텍스트 역할을 하며, Comparator를 통해 동적으로 정렬 전략을 변경할 수 있음
    ```java
    package java.util;

    import java.util.List;

    // Collections 클래스 정의
    public class Collections {
    // Comparator를 사용하여 리스트를 정렬
        public static <T> void sort(List<T> list, Comparator<? super T> c) {
            if (c == null) {
            // Comparator가 제공되지 않은 경우 자연 순서로 정렬
                list.sort(null);
            } else {
            // Comparator를 사용하여 정렬
                list.sort(c);
            }
        }
    }
    ```
  - Strategy (Comparator 인터페이스): 정렬 기준을 정의
  - ConcreteStrategy (NameComparator, AgeComparator 등): 구체적인 정렬 기준을 제공
  - Context (Collections.sort 메서드): 정렬을 수행하는 역할을 하며, Comparator를 통해 동적으로 정렬 전략을 변경할 수 있음

- 전략 패턴의 장점
    - 알고리즘을 정의하고 캡슐화하여 런타임 시에 알고리즘을 선택하는 데 사용됨
    - 알고리즘을 쉽게 변경 및 대체할 수 있으므로 **유연함, 확장성, 재사용성**
    - 각각 알고리즘을 독립적으로 테스트할 수 있으므로 **용이함**
- 전략 패턴의 단점
    - 클라이언트가 전략을 명시적으로 선택해야 함
    - 런타임 시 알고리즘 선택하는데 추가적인 오버헤드 발생 가능
    - 컨텍스트와 전략 간의 결합도 증가
- 템플릿 메서드 패턴과의 차이점
  - 동일한 문제에 대해 상속으로 해결하면 템플릿 메서드 패턴, 인터페이스로 해결하면 전략 패턴
  - 단일 상속만 지원하는 자바에서는 전략 패턴을 더 많이 사용