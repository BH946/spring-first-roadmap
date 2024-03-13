# Intro

**스프링 DB 2편 - 데이터 접근 활용 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

소스 코드는 커밋 위주로 보는게 좋습니다.

* **프로젝트는 ?개를 진행**
  * **?** -> ?

<br><br>

# 2. 데이터 접근 활용 "기술"

이번 강의에서는 **"데이터 접근 기술 활용 방안" 과 "트랜잭션 전파"** 관련해서 집중적으로 확인하겠다.

초반에 **"데이터 접근 기술들 소개(JDBC, JPA 등)"**하는 부분들은 **필요할 때 자세히 공부**하면 된다고 생각한다.

해당 프로젝트에서는 **컴포넌트 스캔 범위를 한정**하여 **수동으로 빈을 등록**했다는점을 참고

- 방식 : **main 소스**에 `@Import(설정파일)` 로 스프링 빈 등록 -> 설정 파일은 `@Configuration` 사용
- 목적 : `hello.itemservice.web` 범위로 스캔을 한정하여 @Controller 만 스캔되게 하였다. 왜냐하면, **데이터 접근 기술들(ex:레포지토리)은 별도로 쉽게 설정을 바꾸기 위함**
- 테스트분리 : **프로필**을 사용하여 프로필 이름 "local" 일 때 "자동 데이터 생성" 빈을 등록 가정,  
  - **테스트 코드에선 프로필을 local 이외로 설정**하면 "자동 데이터 생성" 빈이 등록되지 않으니까 **"테스트 분리"가 성공적!**
  - `@PostConstruct` 보다 `@EventListener` 를 사용 추천 (좀 더 타이밍이 안전)

<br>

데이터 접근 기술... 관련 요약과 트랜잭션 전파... 관련 요약(기억할것들)





<br>

**아래부터는 "데이터 접근 기술들 소개(간략히), 데이터 접근 기술 활용 방안, 트랜잭션 전파" 순으로 정리**

<br><br>





**프로젝트 간단히 따라하면서 "커밋" 자주**





## 데이터 접근 기술 소개

**스프링 JdbcTemplate, MyBatis, JPA, 스프링 데이터 JPA, Querydsl** 순서로 살펴보자.

데이터 접근 기술을 크게 2가지로 분류 -> **SQLMapper, ORM 관련 기술**

- SQLMapper -> JdbcTemplate, MyBatis
  - SQL 만 작성하면 객체로 편리하게 매핑

- ORM 관련 기술 -> JPA, Hibernate, 스프링 데이터 JPA, Querydsl
  - SQL 조차 작성하지 않음

<br>

**해당 프로젝트 구현내용을 잠깐 살펴보자**

- 레포지토리 속 검색 함수에 사용된 Map클래스 문법 ->  `map.values().stream().filter(람다함수).collect(Collectors.toList());`
  - 특히 검색으로 들어온 값이 null 이면 전체를 검색한것처럼 처리!
- 서비스는 인터페이스 도입 -> 보통 인터페이스 사용X (테스트 때문에 사용했다고 함)
- Dto는 제일 하위에서 가지는 계층에 두자
- `@EventListenr(ApplicationReadyEvent.class)` 가 타이밍 관련해서는 `@PostConstruct` 보다 문제 없다. -> 단, 사용시 **"스프링 빈" 등록** 필수
  - 동작시점 : AOP를 포함한 **스프링 컨테이너가 완전히 초기화 된 이후**에 호출
- ItemRepository의 구현체가 아니라 **인터페이스를 테스트하는게 좋다**
- 테이블의 기본 키를 선택하는 전략은 크게 2가지 -> 자연 키(주민번호), 대리 키(auto_increment)
  - **대리 키를 권장 -> 변화하는 비지니스 환경 때문**

<br>

아래 그림은 **프로젝트의 실행 모습**이다. 해당 프로젝트에 **데이터 접근 기술들을 다양하게 적용**할 예정이다.

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/32ffa831-5db0-440f-999b-e932b293d3b4) 

<br><br>

### JdbcTemplate

라이브러리 추가하여 사용

- 커넥션 흭득
- `statement` 를 준비하고 실행
- 결과를 반복하도록 루프를 실행
- 커넥션 종료, `statement, resultset` 종료
- 트랜잭션 다루기 위한 커넥션 동기화
- 예외 발생시 스프링 예외 변환기 실행
- 참고) jdbc 로그를 보려면 따로 properties에 설정 추가

<br>

**동적쿼리 문제 예시**

- 검색 조건이 없을 때, 상품명으로 검색 때, 최대가격 검색 때, 상품명과 최대가격 둘다 검색 때
- 위 총 4가지를 전부 동적으로 SQL 생성해야하는데 코드가 복잡해진다. -> MyBatis 가 훨씬 간단

<br>

**소스파일 참고**

- JdbcTemplateItemRepositoryV1 -> JdbcTemplate 기본 사용 문법
- JdbcTemplateV1Config -> 레포지토리, 서비스 "빈 등록"
- JdbcTemplateItemRepositoryV2 -> 이름 지정 파라미터 사용 (모호함 제거, 유지보수 상승)
  - 이름 지정 파라미터 -> 모호함을 제거
    - NamedParameterJdbcTemplate
  - Map 과 같이 보통 키, 값 으로 이루어진 자료구조 (SQL에 특화된 param)
    - SqlParameterSource
      - BeanPropertySqlParameterSource
      - MapSqlParameterSource
    - Map
  - 매퍼의 기능을 "자동화"
    - BeanPropertyRowMapper	
- 







<br><br>

## 데이터 접근 기술 활용 방안





<br><br>

## 트랜잭션 전파





<br><br>

# Folder Structure

생략
