### **어댑터 패턴(Adapter Pattern)**

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
      - 스프링 시큐리티는 `UserDetails` 인터페이스를 통해 사용자 정보를 처리
      - 엔티티 클래스 `Member`는 `UserDetails` 인터페이스를 구현하지 않기 때문에 직접 사용할 수 없음
      - `UserDetails`를 구현하는 커스텀 클래스(`UserFormLoginDto`)를 만들고 `UserService`를 구현하는 커스텀 클래스(`CustomUserDetailsService`)를 엔티티와 시큐리티 시스템 간의 호환성 만듬
      - `MemberRepository`가 `UserDetails`과 상관없는 `Member`를 넘겨주기 때문에 이를 다시 UserDetails로 변환해주는 어댑터가 필요
      1. `Member` ( 엔티티 클래스)
        1. JPA를 통해 데이터베이스에서 사용자 정보를 관리
        2. 일반적으로 스프링 시큐리티의 `UserDetails` 인터페이스를 구현하지 않음

        ```java
        @Entity
        public class Member {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            
            private String username;
            private String password;
            private String role;
            private boolean isAccountNonLocked;
            private boolean isCredentialsNonExpired;
            private boolean isEnabled;
        
            // Getters and Setters
        }
        ```

      1. `UserFormLoginDto` 클래스
        1. `UserDetails`를 구현하며, `Member` 엔티티에서 사용자 정보를 추출하여 `UserDetails`의 요구 사항을 충족

        ```java
        public class UserFormLoginDto implements UserDetails {
            private String username;
            private String password;
            private String role;
            private boolean isAccountNonLocked;
            private boolean isCredentialsNonExpired;
            private boolean isEnabled;
        
            // Constructor, Getters, and Setters
            
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority(role));
            }
        
            @Override
            public String getPassword() {
                return password;
            }
        
            @Override
            public String getUsername() {
                return username;
            }
        
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }
        
            @Override
            public boolean isAccountNonLocked() {
                return isAccountNonLocked;
            }
        
            @Override
            public boolean isCredentialsNonExpired() {
                return isCredentialsNonExpired;
            }
        
            @Override
            public boolean isEnabled() {
                return isEnabled;
            }
        }
        ```

    1. `CustomUserDetailsService` 클래스
      1. `UserDetailsService`를 구현하며, `UserFormLoginDto`를 생성하여 스프링 시큐리티에 사용자 정보를 제공

        ```java
        @Service
        public class CustomUserDetailsService implements UserDetailsService {
        
            @Autowired
            private MemberRepository memberRepository;
        
            @Override
            public UserFormLoginDto loadUserByUsername(String username) throws UsernameNotFoundException {
                Member member = memberRepository.findByUsername(username);
        
                if (member == null) {
                    throw new UsernameNotFoundException("User not found");
                }
        
                return UserFormLoginDto.builder()
                        .username(member.getUsername())
                        .password(member.getPassword())
                        .role(member.getRole())
                        .isAccountNonLocked(member.isAccountNonLocked())
                        .isCredentialsNonExpired(member.isCredentialsNonExpired())
                        .isEnabled(member.isEnabled())
                        .build();
            }
        }
        ```

    - 어뎁터
      - 엔티티 `Member`의 데이터를 `UserDetails` 인터페이스에 맞게 변환하는 역할을 수행
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