### 템플릿 콜백 패턴(Template Callback Pattern - 견본/회신 패턴)

- 스프링의 3대 프로그래밍 모델 중 하나인 DI에서 사용하는 특별한 형태의 전략 패턴
- 전략 패턴과 모든 것이 동일하지만 전략을 익명 내부 클래스로 정의해서 사용한다는 특징
- 예시 코드
    
    ```java
    public interface Strategy {
    	public abstract void runStrategy();
    }
    
    public class Soldier {
    	void runContext(Strategy straegy) {
    		System.out.println("전투 시작");
    		strategy.runStrategy();
    		System.out.println("전투 종료");
    	}
    }
    
    public class Client {
    	public static void main(String[] args) {
    		Soldier rammbo = new Soldier();
    		
    		rambo.runContext(new Strategy() {
    			@Override
    			public void runStrategy() {
    				System.out.println("총 총총!!! 총총");
    			}
    		});
    	}
    }
    
    // Refactoring
    public class Soldier {
    	void runContext(Strategy straegy) {
    		System.out.println("전투 시작");
    		strategy.runStrategy();
    		System.out.println("전투 종료");
    	}
    	
    	private Strategy executeWeapon(final String weaponSound) {
    		return new Strategy() {
    			@Override
    			public void runStrategy() {
    				System.out.println(weaponSound);
    			}
    		});
    	}
    }
    
    ```
    
- 전략을 익명 내부 클래스로 구현한 전략 패턴
- 개방 폐쇄 원칙과 의존 역전 원칙이 적용