### **프록시 패턴 (Proxy Pattern)**

- 대상 원본 객체를 대리하여 대신 처리하게 함으로써 로직의 흐름을 제어하는 행동 패턴
- 개방 폐쇄 원칙과 의존 역전 원칙이 적용된 패턴
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
