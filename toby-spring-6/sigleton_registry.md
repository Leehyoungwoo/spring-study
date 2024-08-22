### 싱글톤 레지스트리

- 싱글톤 레지스트리는 디자인 패턴 중 하나인 싱글톤 패턴의 구현 방식을 보완한 기능으로 직접 싱글톤 형태의 오브젝트를 만들고 관리하는 기능
- 스프링은 기본적으로 별다른 설정이 없다면 내부에서 생성하는 빈 오브젝트를 모두 싱글톤으로 만들기 때문에 같은 객체를 여러번 호출해도 동일한 객체를 반환
- **싱글톤 패턴의 문제점**
    - 싱글톤 패턴은 생성자를 private으로 제한, private 생성자를 가진 클래스는 객체지향의 장점인 상속과 이를 이용한 다형성을 적용할 수 없음
    - 싱글톤은 초기화 과정에서 생성자 등을 통해 사용할 오브젝트를 직접 주입하기도 힘들기 때문에 필요한 오브젝트는 직접 오브젝트를 만들어 사용할 수 밖에 없음
        - 테스트용 오브젝트로 대체하기가 힘듦
    - 여러 개의 JVM에 분산돼서 설치가 되는 경우에도 각각 독립적으로 오브젝트가 생기기 때문에 싱글톤으로서의 가치를 보장하지 못함
    - 싱글톤의 static 메소드를 이용해 언제든지 싱글톤에 접근할 수 있어 전역 상태로 사용될 수 있음
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
        - 특정 클라이언트에 의존적인 필드가 있으면 안됨
        - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안됨
        - 가급적 읽기만 가능해야 함
        - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 함
- 스프링 프레임워크는 싱글톤 패턴의 문제를 해결하기 위해 IoC(Inversion of Control) 컨테이너를 사용
  - DI (Dependency Injection): 객체를 직접 생성하는 대신, 컨테이너가 객체를 생성하고 주입, 이를 통해 테스트용 객체를 쉽게 주입할 수 있음
  - 상속과 다형성: @Configuration을 사용하여 빈을 정의하고, @Bean 메서드를 통해 생성할 수 있으므로, 상속이나 다형성을 사용하는 것이 가능해짐
  - JVM 분산 문제: 분산 환경에서는 스프링의 클러스터링 기능을 통해 싱글톤의 일관성을 유지할 수 있음
  - BeanFactory와 ApplicationContext를 통해 싱글톤 빈을 관리
  - DefaultSingletonBeanRegistry와 DefaultListableBeanFactory 클래스에서 싱글톤 빈을 어떻게 관리하는지 확인
  - DefaultSingletonBeanRegistry
    - ApplicationContext의 빈을 싱글톤으로 관리하는 내부 클래스로 싱글톤 빈을 저장하고 관리하는 책임
    - SingletonBeanRegistry 인터페이스를 구현하며, 싱글톤 빈을 관리하기 위해 주요 데이터를 저장
    ```java
    public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
    
        private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
        private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
        private final Map<String, Object> singletonFactories = new HashMap<>(16);
        private final Map<String, Object> registeredSingletons = new CopyOnWriteArrayList<>();
    
        @Override
        public Object getSingleton(String beanName) {
            return this.singletonObjects.get(beanName);
        }
    
        @Override
        public void registerSingleton(String beanName, Object singletonObject) {
            if (this.singletonObjects.containsKey(beanName)) {
                throw new IllegalStateException("Already registered singleton: " + beanName);
            }
            this.singletonObjects.put(beanName, singletonObject);
            this.registeredSingletons.add(beanName);
        }
    
        @Override
        public void destroySingletons() {
            synchronized (this.singletonObjects) {
                // Clear singleton objects and related maps
                this.singletonObjects.clear();
                this.earlySingletonObjects.clear();
                this.singletonFactories.clear();
                this.registeredSingletons.clear();
            }
        }
    }
    ```
    - singletonObjects: 실제로 싱글톤 객체를 저장하는 ConcurrentHashMap.
    - registerSingleton: 새로운 싱글톤 객체를 등록하는 메서드.
    - getSingleton: 이미 등록된 싱글톤 객체를 반환하는 메서드.
    - destroySingletons: 모든 싱글톤 객체를 제거하는 메서드.

  - DefaultListableBeanFactory
    - 스프링의 기본 빈 팩토리 구현체로, BeanFactory와 ApplicationContext의 기능을 모두 제공하며, 빈의 정의와 의존성을 관리
    - 싱글톤 빈의 생성 및 관리도 DefaultListableBeanFactory가 담당
    -  내부적으로 DefaultSingletonBeanRegistry를 상속받아 싱글톤 빈의 관리 기능을 포함
    ```java
    public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {
    
        private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    
        @Override
        protected void refreshSingletons() {
            // Method to initialize singleton beans
        }
    
        @Override
        public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
            this.beanDefinitionMap.put(beanName, beanDefinition);
            // Register the bean definition and initialize the singleton
            this.registerSingleton(beanName, createBean(beanDefinition));
        }
    
        private Object createBean(BeanDefinition beanDefinition) {
            // Method to create and return a new bean instance
        }
    }
    ```
    - beanDefinitionMap: 빈 정의를 저장하는 ConcurrentHashMap.
    - registerBeanDefinition: 빈 정의를 등록하고 싱글톤 빈을 초기화하는 메서드.
    - createBean: 빈 정의를 기반으로 빈을 생성하는 메서드.
