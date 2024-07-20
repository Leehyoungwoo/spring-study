### 도메인 모델 아키텍처 패턴

- 도메인 로직, 비즈니스 로직을 어디에 둘지를 결정하는 패턴
    - 트랜잭션 스크립트
        - 비즈니스 로직을 서비스 메서드에 직접 구현하는 패턴
        - 각 트랜잭션(작업)이 하나의 스크립트로 처리되며, 이는 주로 서비스 계층에서 이루어짐
        - 간단한 비즈니스 로직과 복잡하지 않은 시스템에 적합
    - 도메인 모델
        - 도메인 모델 오브젝트
            - 비즈니스 로직을 도메인 오브젝트에 캡슐화하는 패턴
            - 예시 코드

                ```java
                public class Order {
                    private BigDecimal totalAmount;
                    private List<Item> items;
                
                    public void addItem(Item item) {
                        items.add(item);
                        totalAmount = totalAmount.add(item.getPrice());
                    }
                
                		// 비즈니스 로직을 도메인 객체 내부에 캡슐화
                    public void applyDiscount(Discount discount) {
                        totalAmount = totalAmount.subtract(discount.calculateDiscount(totalAmount));
                    }
                }
                ```

- 실제로는 두 가지 패턴을 조합하여 사용하는 경우가 많음
- 도메인 오브젝트 테스트
    - 비즈니스 로직을 도메인 객체에 구현한 경우 해당 로직이 올바르게 동작하는지 검증하는 과정