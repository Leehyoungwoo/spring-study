### 어댑터 패턴(Adapter Pattern)

- 서로 호환되지 않는 인터페이스를 가진 클래스들이 함께 작업할 수 있도록 중간에서 인터페이스를 변환해주는 디자인 패턴
- 기존 코드와 새로운 코드를 연결할 때 유용
- 예시
    - 다양한 데이터베이스 시스템을 공통의 인터페이스 JDBC를 이용해 조작
    - 플랫폼 별 JRE
- 개방 폐쇄 원칙을 활용한 설계 패턴이라 볼 수 있음
    - 기존 코드에 영향을 미치지 않고 새로운 기능을 추가할 수 있게 해줌
- 예시 코드

    ```java
    public class AdapterServiceA {
    	ServiceA sa1 = new ServiceA();
    	
    	void runService() {
    		sa1.runServiceA();
    	}
    }
    
    public class AdapterServiceB {
    	ServiceB sb1 = new ServiceB();
    	
    	void runService() {
    		sa1.runServiceB();
    	}
    }
    
    public class ClientWithAdapter {
    	public static void main(String[] args) {
    		AdapterServiceA asa1 = new AdapterServiceA();
    		AdapterServiceB asb1 = new AdapterServiceB();
    		
    		asa1.runService();
    		asb1.runService();
    	}
    }
    ```

    - 클라이언트(ClientWithAdapter)가 변환기를 통해 runService()라는 동일한 메서드 명으로 두 개의 메서드를 호출
    - 합성, 객체를 속성으로 만들어서 참조하는 디자인 패턴으로 **호출당하는 쪽**(`ServiceA`, `ServiceB`)**의 메서드를 호출하는 쪽**(`AdapterServiceA`, `AdapterServiceB`)**의 코드에 대응하도록 중간에 변환기를 통해 호출하는 패턴**
- 사용할 때
    - 기존 시스템과의 통합
        - 기존 시스템과 호환되지 않는 새로운 시스템을 통합할 때 사용
- 단점
    - 클래스를 추가로 작성해야 하기 때문에 소스코드가 늘어남