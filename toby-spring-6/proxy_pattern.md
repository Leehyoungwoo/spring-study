### **프록시 패턴 (Proxy Pattern)**

- 대상 원본 객체를 대리하여 대신 처리하게 함으로써 로직의 흐름을 제어하는 행동 패턴
- 개방 폐쇄 원칙과 의존 역전 원칙이 적용된 패턴
- 프록시 패턴은 반환값을 그대로 반환, 호출 전후에 부가적인 작업 수행
![](https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Proxy_pattern_diagram.svg/1200px-Proxy_pattern_diagram.svg.png)

- 구현방식
    - 구성 요소 인터페이스(Subject Interface): 실제 객체와 프록시 객체가 구현하는 공통 인터페이스
    - 구체 구성 요소(Real Subject): 실제 객체로, 실제 비즈니스 로직을 구현
    - 프록시(Proxy): 구성 요소 인터페이스를 구현하며, 실제 객체에 대한 참조를 포함
        - 프록시는 요청을 실제 객체에 전달하거나, 추가적인 기능을 수행
- 예시 
    - 스프링의 AOP (Aspect-Oriented Programming)
      - 스프링 AOP는 프록시 패턴을 사용하여 핵심 비즈니스 로직에 대한 부가적인 기능(트랜잭션, 보안, 로깅)을 삽입할 수 있음
      - 메서드 호출 시점에서 다양한 처리를 추가할 수 있도록 도와줌
      - @Transactional 어노테이션 선언
        - @Transactional을 사용하면, 내부적으로 트랜잭션 관리가 필요한 메서드에 대해 프록시 객체를 생성
        - 이 과정에서 스프링은 ProxyFactory나 CglibProxyFactory를 사용하여 프록시 객체를 생성
        - 이 프록시 객체는 userService.createUser() 메서드를 호출할 때, 트랜잭션 인터셉터를 통해 트랜잭션을 관리하는 추가 로직을 삽입
      - 트랜잭션 인터셉터 (TransactionInterceptor)
        -  이 클래스는 메서드 호출을 가로채서 트랜잭션을 시작하고, 종료 시점에 커밋하거나 롤백을 수행
    ```java
    public class TransactionInterceptor extends TransactionAspectSupport implements MethodInterceptor {
    
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 트랜잭션을 시작합니다.
            TransactionInfo txInfo = createTransactionIfNecessary(method, invocation);
            
            Object retVal;
            try {
                // 실제 메서드 실행
                retVal = invocation.proceed();
            } catch (Throwable ex) {
                // 예외 발생 시 트랜잭션 롤백
                completeTransactionAfterThrowing(txInfo, ex);
                throw ex;
            } finally {
                // 트랜잭션 종료
                cleanupTransactionInfo(txInfo);
            }
            // 정상 종료 시 트랜잭션 커밋
            commitTransactionAfterReturning(txInfo);
            return retVal;
        }
    }
    ```
  - 트랜잭션 관리
    - TransactionInterceptor는 내부적으로 TransactionManager를 사용하여 트랜잭션을 관리
    - 스프링에서는 DataSourceTransactionManager, JpaTransactionManager 등 다양한 트랜잭션 관리자를 제공
    ```java
    public void commit(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalTransactionStateException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }
        if (status.isRollbackOnly()) {
            processRollback(status);
        } else {
            processCommit(status);
        }
    }
    ```
    - 프록시를 통한 메서드 호출
      - 클라이언트가 UserService.createUser()를 호출하면, 실제로는 프록시 객체의 invoke() 메서드가 실행
      - 이 메서드는 트랜잭션을 시작하고, 메서드 호출을 가로챈 뒤, 트랜잭션의 상태에 따라 커밋 또는 롤백을 수행
    - 반환값 처리
      - 프록시 객체는 createUser() 메서드의 실행 결과를 클라이언트에게 반환
      - 반환값을 그대로 반환하므로, 프록시 패턴은 반환값을 수정하지 않는 것이 **일반적**
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
