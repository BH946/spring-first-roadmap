# Intro

**스프링 DB 1편 - 데이터 접근 핵심 원리**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **프로젝트는 1개를 진행**
  * **jdbc **-> 원리설명이 대부분 (2편이 활용 내용)


<br><br>

# 1. 데이터 접근 핵심 "원리"

`JDBC, 커넥션풀, 데이터소스, 트랜잭션, 예외` 는 가볍게 보고, 실제 스프링에서 제공하는 `트랜잭션, 예외 처리, 반복` 을 집중적으로 보겠다.

**강의는 크게 3가지 형태의 문제점을 해결해나간다.**

- **트랜잭션 문제**
  - JDBC 구현 기술이 서비스 계층에 누수 -> 서비스 계층에서 트랜잭션 적용 때문
  - 트랜잭션 동기화 문제 -> 같은 트랜잭션 유지위해 커넥션을 파라미터로 넘겨야 하기 때문
    - 트랜잭션 유지할 필요 없는 기능도 있으니 분리까지 신경써야 한다.
  - 트랜잭션 적용 반복 문제 -> try, catch, finally ...
- **예외 누수 문제**
  - 데이터 접근 계층의 예외가 서비스 계층으로 전파 문제
    - `SQLException` 은 체크 예외이며 JDBC 전용이다 -> 예외 반드시 처리 문제 + JPA,오라클 등으로 변경에 많은 코드 수정 문제
- **JDBC 코드 반복 문제**
  - try, catch, finally, connection, preparedstatement 등등...

<br>

**해결 방식??**

- **트랜잭션 문제** -> 서비스 계층
  - JDBC 구현 기술 서비스 계층에 누수 -> 스프링이 제공하는 "트랜잭션 매니저"를 사용
    - 트랜잭션 추상화로 해결하는 것(ex:인터페이스)
  - 트랜잭션 동기화 문제 -> 스프링이 제공하는 "트랜잭션 동기화 매니저(트랜잭션 매니저)"를 사용
  - 트랜잭션 적용 반복 문제 -> TransactionTemplate 사용
  - **(기억)이 모든것을 한번에 해결? @Transactional 사용**
- **예외 누수 문제**
  - **스프링**은 데이터 접근 계층에 대한 **"일관된 예외 추상화"**를 제공 + **"예외 변환기"** 제공
    - 스프링 제공 예외변환기 사용후 스프링이 제공하는 데이터 접근 예외 클래스를 사용
- **JDBC 코드 반복 문제** -> 레퍼지토리 계층
  - JdbcTemplate 사용 -> 커넥션 동기화, 예외변환, 리소스 등 한번에 제공
  - **(기억)결론적으로 여기서 해결법은? JdbcTemplate 사용**

<br>

**참고**

- **레퍼지토리** 계층은 **인터페이스**를 사용 권장 -> JDBC, JPA, 오라클 등 다양한 구현체 생성위해(이미 스프링에서 제공)
- **서비스** 계층은 **인터페이스**를 잘 사용하지는 않음 -> 물론, 사용하는 경우도 있음

<br><br>

## 프로젝트 환경설정 & 생성

강의는 자바 11에 스프링 2.x를 사용했지만, 본인은 자바 17에 스프링3.x를 사용한다.

의존성은 jdbc api, h2, lombok 추가 + 테스트용 lombok 직접 추가

<br><br>

## JDBC

**JDBC(Java Database Connectivity)**는 자바에서 데이터베이스에 접속할 수 있도록 하는 **자바 API**

앱 <-> DB의 커넥션 연결, SQL 전달, 응답 과정들이 DB마다 달라서 **"새로 DB학습+코드수정 비용" 문제가 있는데 JDBC 가 이를 해결**

<img src="https://github.com/BH946/spring-first-roadmap/assets/80165014/f3b667bd-d959-410b-9807-98ab03711cf2" alt="image" style="zoom: 67%;" /> 

<br>

JDBC -> SQL Mapper -> ORM 기술발전

- SQL Mapper : JdbcTemplate, MyBatis -> sql 사용
- ORM : JPA, Spring jpa data, Querydsl -> sql 사용X (물론 사용해도 됨)

<br>

JDBC 적용 : `MemberRepositoryV0.java` 와 테스트코드 참고 -> 옛날 방식

<br>

## 커넥션 풀, DataSource

**커넥션 풀**은 **실무**에서 무조건 사용, **성능테스트**를 통해 결정하는편, `hikariCP` 를 **기본으로 스프링 부트가 제공**(커넥션풀 오픈소스)

- **별도의 스레드를 사용**해서 커넥션 풀에 커넥션을 채움 -> 오래걸리기 때문
- 10개 제한인데 11개 커넥션시 **30초 후 예외터짐(기본값)**
- `MyPool - Pool stats (total=8, active=2, idle=6, waiting=0)` 로그 : 총개수, 사용개수, 풀에서 대기개수, 대기개수

<br>

**DataSource**는 다양한 커넥션 풀을 구현체로 둬서 쉽게 변경하기위한 **인터페이스**

- 기존 **DriverManager** 사용시 매번 **"신규 커넥션 생성"** -> 커넥션 풀로 변경하자! (기본 제공중)
  - 단, DriverManager는 **DataSource를 사용하지 않음** -> 사용하는 클래스 따로 제공 `DriverManagerDataSource`
  - 기본적으로 DB정보(URL,ID,PW 등)을 설정파일에서 읽는것도 지원한다.

<img src="https://github.com/BH946/spring-first-roadmap/assets/80165014/97b244c2-5445-4361-8a6b-c3a1fd8f4b5c" alt="image" style="zoom: 80%;" /> 

<br>

`ConnectionTest.java, MemberRepositoryV1.java` 코드(+테스트코드)를 참고 -> Datasource 사용과 커넥션 풀 사용모습

- 참고로 커넥션 풀 로그보려면 **로그레벨 DEBUG** 로 해서 봐야함 -> 스프링부트 3.1부터 바뀜
- 참고로 `dataSource.setUsername(USERNAME);` 이런식으로 세팅 가능하며 따로 yaml 파일에 작성후 생략도 가능하다.

<br><br>

## 트랜잭션

트랜잭션 ACID 중 **격리성(Isolation)** 이 중요

- 격리성 보장 위해서는 "순서대로" 실행 -> 단, 동시 성능이 느려짐
- 이를 위해 Isolation level 정의 -> 강의에서는 **READ COMMITTED** 단계만 사용
  - READ UNCOMMITED(커밋되지 않은 읽기) 
  - READ COMMITTED(커밋된 읽기) 
  - REPEATABLE READ(반복 가능한 읽기) 
  - SERIALIZABLE(직렬화 가능)

<br>

앱에서 DB 접근 시 커넥션 뿐만아니라 DB 내부에서는 **"세션" 을 생성** -> 세션이 SQL을 실행

**즉, 커넥션 풀이 10개면 세션도 10개**

<img src="https://github.com/BH946/spring-first-roadmap/assets/80165014/dc0409d6-16df-4f6e-aa0f-44986f4d24a3" alt="image" style="zoom:67%;" /> 

<br>

그림처럼 상태가 "임시"라면 커밋하지 않은것 -> 커밋하지 않은 데이터는 다른 세션에서 아직 못본다 -> 즉, 다른사용자는 아직 이 데이터를 못 보는 것

commit하면 상태가 "완료"로 변경, rollback하면 임시를 다 제거(처음으로 복구)

- **당연히 잘 안쓰는데** 이 데이터도 보고 싶다면 isolation level에서 첫번째 있던 READ UNCOMMITTED 레벨로 설정하면 볼 수 있다!

<img src="https://github.com/BH946/spring-first-roadmap/assets/80165014/615a7117-76fd-4d3a-b934-d8d11c914436" alt="image" style="zoom:80%;" /> 

<br>

**자동 커밋과 수동 커밋** 설정 방법 -> **기본적으로 기본값이 자동커밋**

- `set autocommit false;` 는 수동 커밋모드 설정이며, insert를 하고나서 commit을 직접 해야 반영된다.
- **수동 커밋** 모드로 설정하는 것을 **"트랜잭션을 시작"**한다고 표현한다.

<br>

**DB 락을 제공** -> 동시에 데이터 수정은 당연히 문제가 생김 -> 락으로 해결하자!

- 먼저 **트랜잭션 시작**하여 접근한 **세션**이 락을 흭득 -> 뒤늦게 접근한 세션은 락 흭득위해 대기
- `set lock_timeout 60000;` 락 흭득 시간을 60초로 설정 -> 60초 이상 대기시 예외 발생
- **일반적인 조회는 락 사용X** -> 당연하다
  - **그런데 락을 사용하고 싶은 경우가 있음** -> 예로 memberA의 금액을 조회 후 "중요한 계산"을 해야해서 완료될때까지 memberA 값이 변경되면 안될 때 사용
  - 방법 : select 할때 락을 흭득 -> 즉, set autocommit false 후 `select ... for update` 구문을 추가!

<br>

**비지니스 로직(계좌이체) 구현** -> `MemberServiceV1.java` 와 테스트코드 -> 트랜잭션 없어서 "원자성" 문제 발생

**위 문제 해결 : 앱에서 같은 커넥션 유지(파라미터로 넘김)** -> `MemberRepositoryV2.java, MemberServiceV2.java` 와 테스트코드

<br>

**중요) 하지만 위 코드의 문제점이 크게 3가지 존재 -> 이것이 처음 설명한 문제점 3가지**

- 트랜잭션 문제
- 예외 누수 문제
- JDBC 코드 반복 문제

<br><br>

## 예외

예외를 처리하지 못하고 **계속 상위로 던지면??**

- 자바의 main 스레드는 예외 로그를 출력하고 시스템 종료
- 실무에서는 바로 종료하면 안되므로 WAS가 해당 예외를 처리 -> 오류페이지를 따로 보여주는식으로

<br>

컴파일러의 체크 유무에 따라 -> **체크예외(Exception상속), 언체크예외(RuntimeException상속) 분류**

- **체크예외**는 반드시 **"잡거나 던지거나" 둘중 하나를 처리**해야 한다.
  - `throw` 로 던지거나 `try...catch...finally` 로 잡는다.
- **언체크예외**는 처리하지 않아도 된다 -> 자동으로 `throws`로 던져주기 때문
- **참고) 에러메시지 출력 흐름은??** -> Exception을 상속받은 클래스의 생성자에서 `super("에러메시지")` 를 입력하면 Exception 클래스의 detailMessage 변수에 기록하여 나중에 에러로그에 출력해준다.
  - 따라서 `throw new 클래스("에러메시지")` 를 하면 Exception이 에러메시지를 가지게 되는것이다.

<br>

**실무에서는?? -> 기본원칙 2가지 준수**

- 기본적으로 **언체크(런타임) 예외를 사용**하자(**문서화 필수!**) -> 체크 예외의 2가지 문제 때문
  - (1) 복구 불가능한 예외 문제
    - 정의된 SQL ErrorCode를 이용해 **레포지토리**에서 SQLException을 예외변환 후 **서비스**에서 이를 catch해서 복구로직 구현
  - (2) 인터페이스에 조차 throws를 해줘야해서 의존 관계의 문제
    - 런타임 예외 적용 + 예외변환으로 해결
    - 예외변환은 `throw new 커스텀예외(e);` 처럼 현재 error를 담는 e를 꼭 생성자 매개변수에 넘겨줘야 하위의 에러내용들을 다 기록하므로 이부분 주의하자!   
      (물론 스프링은 예외변환기를 제공하고 있음)
  - **이 문제들을 다 해결하는 방법을 스프링은 제공하고 있다.**
    - `DataAccessException` 에 필요한 예외 가져다 사용
    - `SQLErrorCodeSQLExceptionTranslator` 로 예외변환기 한줄작성
      - 예외변환기 사용시 코드한줄로 **SQL ErrorCode에 매핑된 스프링 제공 예외클래스를 자동으로 반환**해줌 -> 따라서 이것만 기억하면 됨
      - 코드한줄 : `throw exTranslator.translate("save", sql, e);`
    - **(중요)단, JdbcTemplate 을 사용하면 지금까지 배운 모든걸 한번에 제공..!**
- **체크 예외**는 비지니스 로직상 너무 중요해서 **의도적으로 던지는 예외에만** 사용하자 -> 예로 계좌이체 실패 예외

<br>

**런타임예외 사용그림 -> 예외 공통 처리까지 자동으로 던져줌**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/e431ccb4-ddef-43f9-b315-50937ee532c8) 

<br>

**스프링이 제공하는 예외 추상화 그림**

<img src="https://github.com/BH946/spring-first-roadmap/assets/80165014/56296381-8d05-44fd-894f-4f3d62b74841" alt="image" style="zoom:67%;" /> 

<br>

test 패키지에 있는 exception/basic 하위에 `CheckedAppTest, CheckedTest, UnCheckedAppTest, UncheckedTest` 자바파일 참고

<br><br>

## (서비스)스프링 제공 - 트랜잭션





<br><br>

## (레포지토리)스프링 제공 - 예외 처리, 반복

**레퍼지토리의 유연성을 높이기 위해 "인터페이스"로 구현을 해주고,**

**레퍼지토리에 JdbcTemplate 을 사용하면 끝! -> 우리는 JDBC를 사용중이니까 이것을 사용**

- 커넥션 조회, 커넥션 동기화
- `PreparedStatement` 생성 및 파라미터 바인딩
- 쿼리실행, 결과 바인딩
- 예외 발생시 스프링 예외 변환기 실행
- 리소스 종료(close)
- **이 모든것을 JdbcTemplate 하나로 해결가능**

다른 복잡한 중간내용은 생략하겠다.

```java
/**
 * JdbcTemplate 사용
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?, ?)";
        template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
```

<br>

```java
/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * MemberRepository 인터페이스 의존
 */
@Slf4j
@SpringBootTest // 스프링 빈 사용을 위해 필수
class MemberServiceV4Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired // 아래 스프링 빈에 등록한 레포, 서비스를 주입
    private MemberRepository memberRepository;
    @Autowired
    private MemberServiceV4 memberService;

    @TestConfiguration // 테스트에서 스프링빈 등록을 지원하는 어노테이션
    static class TestConfig {

        // URL,ID,PW가 없는건 application.properties에서 읽었기에 생략가능
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepository memberRepository() {
//            return new MemberRepositoryV4_1(dataSource);
//            return new MemberRepositoryV4_2(dataSource);
            return new MemberRepositoryV5(dataSource);
        }

        @Bean
        MemberServiceV4 memberServiceV4() {
            return new MemberServiceV4(memberRepository());
        }
    }

    @AfterEach
    void after() {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    void AopCheck() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberRepository class={}", memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //when
        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberEx.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}
```

<br>

예외커스텀, 예외복구용(중복키 예외) : `MyDbException.java, MyDuplicateKeyException.java`

예외누수 해결 : `MemberRepositoryV4_1` -> 런타임 예외로 변경 + 레포지토리 인터페이스 추가하여 사용 + throws SQLException 제거(체크예외때 추가했던)

예외누수 해결+예외변환기 추가 : `MemberRepositoryV4_2` 는 SQLExceptionTranslator(예외변환기)를 추가 -> errorCode조건문까지 자동적용하여 스프링이 제공하는 예외 클래스로 자동 반환

JdbcTemplate(끝판왕) : `MemberRepositoryV5` 말도안되게 코드가 줄어들었고, 필요시 db데이터와 객체의 매핑만 추가 구현하면 된다.

레포지토리 인터페이스에 의존 : `MemberServiceV4`

참고) test 패키지에 translator 패키지하위는 **"예외복구" 내용과 "예외변환기" 사용 방법**을 확인가능  
-> 이 둘을 합치진 않았는데 합치면 훨씬 코드 단축이 가능하다는걸 이해(예외복구+예외변환기)

- 물론 예외변환기 조차도 JdbcTemplate 은 자동으로 제공한다.
- **즉, 예외복구는 비지니스로직이니까 "서비스"에 작성되는데 "레퍼지토리"에 JdbcTemplate을 사용하면 예외변환기조차 생략하고 비지니스로직(예외복구)만 생각하면 되는것이다.**

<br><br>

# Folder Structure

생략

