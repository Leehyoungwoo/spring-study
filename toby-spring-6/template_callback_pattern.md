### 템플릿 콜백 패턴(Template Callback Pattern - 견본/회신 패턴)
- 콜백이란
  - 특정 작업이나 이벤트가 발생한 후에 호출되는 함수나 메서드를 의미
![](https://blog.kakaocdn.net/dn/xKi11/btqGIp6s9Oq/YapkKktkYDzjy7RUrvHHWk/img.png)
-  복잡한 작업의 흐름을 캡슐화하여 코드의 중복을 줄이고, 클라이언트 코드가 필요한 부분만 콜백을 통해 구현할 수 있게 하는 패턴
- 스프링의 3대 프로그래밍 모델 중 하나인 DI에서 사용하는 특별한 형태의 전략 패턴
- 전략 패턴과 모든 것이 동일하지만 전략을 익명 내부 클래스로 정의해서 사용한다는 특징
- 구성 요소
  - 템플릿
    - 알고리즘 기본 구조를 정의하고, 일부 구체적인 동작을 외부에서 콜백으로 받음
  - 콜백
    - 구체적인 동작을 정의한 코드 블록으로, 템플릿에 의해 호출
  - 익명 내부 클래스
    - 콜백을 정의하는데 사용됨
- 예시 코드
  - RestTemplate
  ```java
  RestTemplate restTemplate = new RestTemplate();
  String result = restTemplate.execute("http://example.com/api/resource", HttpMethod.GET, null, new ResponseExtractor<String>() {
      @Override
      public String extractData(ClientHttpResponse response) throws IOException {
          return StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
      }
  });
  ```
    - HTTP 요청을 보내고 응답을 처리하는 작업을 간소화
  - JdbcTemplate
  ```java
  JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
  List<User> users = jdbcTemplate.query("SELECT * FROM users", new RowMapper<User>() {
      @Override
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
          User user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
          return user;
      }
  });
  ```
  - SQL 쿼리 실행, 업데이트, 트랜잭션 관리 등의 작업을 간소화
    - 콜백 인터페이스: RowMapper, PreparedStatementCreator, PreparedStatementCallback, ResultSetExtractor 등.
  - TransactionTemplate
  ```java
  TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
  transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
          // 트랜잭션 내에서 실행할 코드
      }
  });
  ```
  - 트랜잭션 관리를 단순화하며, 트랜잭션 내에서 실행할 코드를 콜백으로 전달하여 안전하게 트랜잭션을 처리할 수 있게 해줌
  - 콜백 인터페이스: TransactionCallback, TransactionCallbackWithoutResult.
- 전략을 익명 내부 클래스로 구현한 전략 패턴
- 개방 폐쇄 원칙과 의존 역전 원칙이 적용