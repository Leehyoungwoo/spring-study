### 데코레이터 패턴

- 객체의 결합을 통해 기능을 동적으로 유연하게 확장 할 수 있게 해주는 패턴
  -  기본 기능에 추가할 수 있는 기능의 종류가 많은 경우에 각 추가 기능을 Decorator 클래스로 정의 한 후 필요한 Decorator 객체를 조합함으로써 추가 기능의 조합을 설계 하는 방식
- 개방 폐쇄 원칙과 의존 역전 원칙이 적용된 설계 패턴
  - 기존의 Component 클래스와 ConcreteComponent 클래스는 변경하지 않고 새로운 기능을 추가할 수 있음
  - 새로운 Decorator를 추가하여 기능을 확장할 수 있지만 기존의 Component와 ConcreteComponent 클래스는 수정할 필요가 없음
  - 고수준 모듈 Component는 저수준 모듈 ConcreateComponent에 의존하지 않고, 대신 추상화(인터페이스)에 이존
  - ConcreateDecorator는 Component 인터페이스를 통해 ConcreteComponent와 상호작용하며 직접적으로 ConcreteComponent의 세부사항에 의존하지 않음
- 구현 방식
    - 구성 요소 인터페이스(Component Interface): 기본 동작을 정의하는 인터페이스나 추상 클래스
    - 구체 구성 요소(Concrete Component): 기본 동작을 구현하는 클래스
    - 데코레이터(Decorator): 구성 요소 인터페이스를 구현하며, 구체 구성 요소를 포함하는 클래
        - 기본 동작을 호출한 후 추가적인 동작을 수행
    - 구체 데코레이터(Concrete Decorators): 데코레이터의 서브클래스로, 추가적인 동작을 구현

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/exqO8t/btqAmt2l8MC/b6PrWbB5HTzMoFnflV6Vw1/img.png)

- 예시 코드
  - java.io 패키지의 스트림 클래스들
    - BufferedInputStream / BufferedOutputStream
      DataInputStream / DataOutputStream
      PrintStream
    - 이들은 InputStream 및 OutputStream의 데코레이터로, 기본 스트림에 버퍼링, 데이터 형식 처리, 출력 포맷팅 등의 기능을 추가
  ```java
  import java.io.*;
  
  // 구성 요소 인터페이스
  public abstract class InputStream {
      public abstract int read() throws IOException;
  }
  
  // 구체 구성 요소
  public class FileInputStream extends InputStream {
      private File file;
      private FileInputStream(File file) throws FileNotFoundException {
          this.file = file;
          // 실제 파일 열기 작업
      }
      
      @Override
      public int read() throws IOException {
          // 파일에서 1바이트 읽기
          return 0; // 임시 코드
      }
  }
  
  // 데코레이터
  public abstract class FilterInputStream extends InputStream {
      protected InputStream in;
      
      protected FilterInputStream(InputStream in) {
          this.in = in;
      }
      
      @Override
      public int read() throws IOException {
          return in.read(); // 기본 구현을 호출
      }
  }
  
  // 구체 데코레이터
  public class BufferedInputStream extends FilterInputStream {
      private byte[] buffer;
      
      public BufferedInputStream(InputStream in) {
          super(in);
          this.buffer = new byte[1024]; // 예시 버퍼 크기
      }
      
      @Override
      public int read() throws IOException {
          
          if (bufferPos >= bufferCount) {
            fillBuffer();
            if (bufferCount == -1) return -1; // EOF
            bufferPos = 0;
        }
        return buffer[bufferPos++] & 0xFF;
      }
      
      private void fillBuffer() throws IOException {
        // 버퍼를 사용하여 읽기 처리
        bufferCount = in.read(buffer);
        bufferPos = 0;
     }
  }
  
  public class DataInputStream extends FilterInputStream {
      public DataInputStream(InputStream in) {
          super(in);
      }
      
      public int readInt() throws IOException {
          // Int형 데이터 읽기 구현
          int ch1 = read(); // 기본 read 호출
          int ch2 = read();
          int ch3 = read();
          int ch4 = read();
          if ((ch1 | ch2 | ch3 | ch4) < 0)
              throw new IOException("End of stream");
          return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4);
      }
      
      @Override
      public int read() throws IOException {
          return super.read(); // 기본 read 호출
      }
  }
  ```
- 프록시 패턴과 구현 방법이 같음
  - 다만 프록시 패턴은 클라이언트가 최종적으로 돌려받는 반환값을 조작하지 않고 그대로 전달
  - 데코레이터 패턴은 클라이언트가 받는 반환값에 장식을 덧입힘