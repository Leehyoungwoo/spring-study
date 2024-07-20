### 전략 패턴

- 동일한 계열의 알고리즘군을 정의하고 각 알고리즘군을 캡슐화하여 클라이언트와 상관없이 독립적으로 알고리즘을 목적에 맞게 변경할 수 있게 하는 패턴.
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
