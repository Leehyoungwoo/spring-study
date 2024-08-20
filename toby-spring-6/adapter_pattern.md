### **어댑터 패턴(Adapter Pattern)**

![](https://velog.velcdn.com/images/papakang22/post/4f78710f-0667-4d4a-badb-9736953f1efd/R1280x0.png)

- 서로 호환되지 않는 인터페이스를 가진 클래스들이 함께 작업할 수 있도록 중간에서 인터페이스를 변환해주는 디자인 패턴
- 기존 코드와 새로운 코드를 연결할 때 유용
- 예시
  - 다양한 데이터베이스 시스템을 공통의 인터페이스 JDBC를 이용해 조작
  - 플랫폼 별 JRE
- 개방 폐쇄 원칙을 활용한 설계 패턴이라 볼 수 있음
  - 기존 코드에 영향을 미치지 않고 새로운 기능을 추가할 수 있게 해줌
- 예시 코드
  - Spring Security의 Form Login에서 `UserDetails`, `UserDetailSerive` 인터페이스
    - 상황
      - Adapetee
        - **`Member`**, **`MemberService`**

        ```java
        @Getter
        @Setter
        public class Member {
            private String username;
            private String password;
        }
        ```

        ```java
        public class MemberService {
            public member findMemberByUsername(String username) {
                Member member = new Member();
                member.setUsername(username);
                member.setPassword("11111111");
                return member;
            }
        }
        ```

      - Adapter
        - **`UserDetailsDto`**, **`CustomMemberService`**

          ```java
          public class UserDetailsDto implements UserDetails {
              private Member member;
              
              public UserDetailsDto(Member member) {
                  this.member = member;
              }
              @Override
              public String getUsername() {
                  return this.member.getUsername();
              }
              
              @Override
              public String getPassword() {
                  return this.member.getPassword();
              }
           }
          ```

          ```java
          public class CustomMemberService implements UserDetailsService {
              private MemberService memberService;
              
              public CustomMemberService(MemberService memberService) {
                  this.memberService = memberService;
              }
              
              @Override
              public UserDetails loadUserByUsername(String username) {
                  Member member = memberService.findMemberByUsername(username);
                  return new UserDetailsDto(member);
              }
          }
          ```

      - 기존 코드(**`Member`**, **`MemberSerivce`** )를 클라이언트가 사용하는(스프링 시큐리티에서 요구하는) 인터페이스(**`UserDetails` , `UserDetailsService`**)의 구현체(**`CustomMemberService`**, **`UserDetailsDto`**)로 바꿔주는 패턴
  - java.util.Arrays#asList(T...)
    - 배열을 리스트로 변환
    - 배열 - (어댑터) → 리스트
    - `T...`
      - 가변인자 - 내부적으로는 배열로 넘겨받음
  - java.util.Collections#list(Enumeration), java.util.Collections#enumeration()
    - 컬렉션을 Enumeration으로 변환
    - `strings` : Adaptee
    - `Collections.enumeration()` : Adapter
    - `Enumerations<String>` : Target
  - java.io.InputStreamReader(InputStream)
    1. 문자열 → InputStream (변환 필요)
      1. `BufferedReader`에서 필요한 타입이 `Reader` 타입
    2. `InputStream` → `InputStreamReader` (어댑터 역할)
    3. `InputStreamReader` → `BufferedReader` (추가 변환)
  - java.io.OutputStreamWriter(OutputStream)
    - `OutputStream`을 `Writer`로 변환
    -
    - `OutputStream` → `OutputStreamWriter` (어댑터 역할)
    - `OutputStreamWriter`를 사용하여 문자열 출력을 처리
  - Spring의 HandlerAdpter
    - 다양한 형태의 핸들러 코드를 스프링 MVC가 실행할 수 있는 형태로 변환
    - 핸들러는 다양한 형태이기 때문에 `Object` 타입으로 받아옴
    - `HandlerAdapter`는 이 핸들러를 처리할 수 있는 적절한 구현체를 찾아 반환
    - `HttpServletRequest`와 `HttpServletResponse`를 받아서 `ModelAndView`를 반환하는 방식으로 요청을 처리
- 사용할 때
  - 기존 시스템과의 통합
    - 기존 시스템과 호환되지 않는 새로운 시스템을 통합할 때 사용
- 단점
  - 클래스를 추가로 작성해야 하기 때문에 소스코드가 늘어남