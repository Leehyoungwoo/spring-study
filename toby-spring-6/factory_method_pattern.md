### 팩토리 메서드 패턴

- 객체 생성 과정을 서브클래스로 캡슐화하여, 객체 생성 로직을 유연하게 확장할 수 있게 해주는 패턴
- 객체 생성 로직을 별도의 메서드로 캡슐화하여, 서브클래스에서 객체 생성 방식을 변경할 수 있도록 함
    - 팩토리 메소드 패턴을 이용하면 클래스의 인스턴스를 만드는 일을 서브클래스에게 맡기는 것
- 제품이 추가 되었을 때, 제품 생성 로직이 한 곳에 모여 있어 추가 및 변경하기 쉬운 구조를 제공
- 오버라이드된 메서드가 객체를 반환하는 패턴
- 의존 역전 원칙을 활용
![](https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/FactoryMethod.svg/1920px-FactoryMethod.svg.png)

- 구조
    - **Creator (추상 팩토리 클래스)**: 팩토리 메서드를 선언하여 객체 생성의 인터페이스를 제공
    - **ConcreteCreator (구체 팩토리 클래스)**: 팩토리 메서드를 구현하여 특정 제품 객체를 생성
    - **Product (제품 인터페이스 또는 추상 클래스)**: 생성될 객체의 인터페이스를 정의
    - **ConcreteProduct (구체 제품 클래스)**: 실제로 생성될 객체를 구현
- 단점
    - 상속이 깊어졌을 때, 구조 파악이 어렵고 변경이 어려워 유지보수성에 좋지 않으며 메서드 호출 시 오버헤드가 발생할 수 있음
- 예시 코드
  - java.util.Calendar
    - Calendar 클래스는 추상 클래스로, 구체적인 구현은 GregorianCalendar 등 다양한 서브클래스에서 제공
    - Calendar 클래스에는 getInstance()라는 정적 메서드가 있으며, 이 메서드는 실제로 팩토리 메서드 패턴을 사용하여 적절한 Calendar 객체를 생성
  ```java
  // 오버로딩으로 다양한 서브 클래스를 생성하는 createCalendar 메서드가 존재
      public static Calendar getInstance()
  {
    Locale aLocale = Locale.getDefault(Locale.Category.FORMAT);
    return createCalendar(defaultTimeZone(aLocale), aLocale);
  }
  
  public static Calendar getInstance(TimeZone zone)
  {
    return createCalendar(zone, Locale.getDefault(Locale.Category.FORMAT));
  }
  ```
  - BeanFactory와 ApplicationContext
    - 스프링의 BeanFactory와 ApplicationContext 인터페이스는 팩토리 메서드 패턴을 사용하여 빈(Bean) 객체를 생성
    - BeanFactory 인터페이스는 실제 빈 객체를 생성하는 구체적인 로직을 숨김
    - XmlBeanFactory 또는 DefaultListableBeanFactory와 같은 클래스는 BeanFactory 인터페이스를 구현하며, 실제 빈 객체의 생성 로직을 포함
  ```java
  public interface BeanFactory {
      Object getBean(String name);
      <T> T getBean(String name, Class<T> requiredType);
  }
  
  public class DefaultListableBeanFactory implements BeanFactory {
    // BeanFactory methods implementation
    @Override
    public Object getBean(String name) {
      // 실제 빈 객체 생성 및 반환
    }
  
    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
      // 실제 빈 객체 생성 및 반환
    }
  }
  ```
  