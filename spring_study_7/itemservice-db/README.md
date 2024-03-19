# Intro

**스프링 DB 2편 - 데이터 접근 활용 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

소스 코드는 커밋 위주로 보는게 좋습니다.

* **프로젝트는 2개를 진행**
  * **itemservice-db** -> 데이터 접근 기술
  * **springtx** -> 트랜잭션 전파 => 코드생략

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

**그래서 데이터접근 결론으로 무엇을 추천하는가??**

- **JPA, 스프링 데이터 JPA, Querydsl 을 기본으로 사용**
  - 어댑터 추가ver, 단순ver 은 상황에 맞게 선택!
  - 상관없다면?? **단순ver을 사용**하자 -> **스프링데이터JPA + QueryDSL** 를 기본으로 잡자!
- **복잡한 쿼리가 잘 해결이 안될때 해당 부분은 JdbcTemplate이나 MyBatis를 함께 사용**
  - JPA랑 JDBC는 트랜잭션 매니저가 다를텐데 어떡하나??
  - `JpaTransactionManager` 가 대부분 기능들을 제공해서 괜찮다고 한다.
  - 단, JPA 플러시 타이밍에 주의

<br>

**트랜잭션 AOP 주의 사항**

- **트랜잭션 AOP 주의 사항 - 프록시 내부 호출**

  - "클라요청" -> "프록시 호출" -> "실제 트랜잭션 호출" 순서가 일반적인데,
  - "클라요청" -> "실제 트랜잭션 호출" 인 경우를 마주칠 수 있다. **=> 트랜잭션 적용X**
    - 자바 문법상 `internal();` 은 `this.internal();` 로 호출하므로 이런 경우가 생긴다.
  - 가장 간단한 해결방안은 **"internal 메소드를 별도의 클래스로 분리"**

  **트랜잭션 AOP 주의 사항 - 트랜잭션 적용 범위**

  - 클래스 레벨에 "트랜잭션 적용"시 스프링은 **public 메서드**만 트랜잭션 적용하도록 기본 설정
    - 물론, 설정 변경 가능하다.

  **트랜잭션 AOP 주의 사항 - 초기화 시점**

  - @PostConstruct로 선언된곳에 @Transactional 선언시 트랜잭션은 미적용
    - WHY?? **초기화코드(@PostConstruct)가 먼저 호출되고 그 다음 트랜잭션 AOP가 적용되기 때문이다. 따라서 초기화 시점에 해당 메서드는 트랜잭션 흭득 불가.**
    - 해결법?? **@EventListener(ApplicationReadyEvent.class) 사용**

<br>

**스프링 트랜잭션 전파 - 트랜잭션 두 번 사용**

- **(1) 순차적으로 2번 사용은?**
  - **"같은 커넥션(=conn0)"**를 사용, **"다른 프록시 커넥션"**을 사용.
- **(2) 트랜잭션이 순차가 아닌 중복되게 사용하면? (커밋)**  
  아래 코드처럼 "외부 시작 -> 내부 시작,커밋 -> 외부 커밋" 라면?
  - 신규 트랜잭션 이라면 -> 커밋O
  - 신규 트랜잭션 아니라면 -> 커밋X
- **(3) 트랜잭션이 순차가 아닌 중복되게 사용하면? (롤백)**  
  - 내부 롤백은 내부적으로 따로 롤백 마킹
    - 외부 커밋 시점에 이를 확인해서 롤백 -> **rollbackOnly 설정을 확인**
  - 이때, **UnexpectedRollbackException** 런타임 예외를 추가로 던진다.
    - **롤백은 중요한 일**이므로 이런 예외를 추가로 던진다.
- **(4) 트랜잭션이 순차가 아닌 중복되게 사용하면? (REQUIRES_NEW)**  
  - REQUIRES_NEW 라는 옵션을 사용하면 **"외부 트랜잭션과 내부 트랜잭션을 분리 가능"**
  - 즉, **독립적인** 트랜잭션이 2개 생기는 것이며 2개가 **동시에 사용**되는 것 -> 정확히는 1개씩 커넥션 순차적으로 처리 된다.
  - 참고로 REQUIRED 옵션이 기본값! (트랜잭션 참여)

<br>

**아래부터는 "데이터 접근 기술들 소개(간략히), 데이터 접근 기술 활용 방안, 트랜잭션 전파" 순으로 정리**

<br><br>

## 데이터 접근 기술 소개

**스프링 JdbcTemplate, MyBatis, JPA, 스프링 데이터 JPA, Querydsl** 순서로 살펴보자.

데이터 접근 기술을 **크게 2가지**로 분류 -> **SQLMapper, ORM 관련 기술**

- SQLMapper -> JdbcTemplate, MyBatis
  - SQL 만 작성하면 객체로 편리하게 매핑

- ORM 관련 기술 -> JPA, Hibernate, 스프링 데이터 JPA, Querydsl
  - SQL 조차 작성하지 않음

<br>

**해당 프로젝트에 이미 구현된 내용을 잠깐 살펴보자**

- **레포지토리** 속 검색 함수에 사용된 Map클래스 문법 ->  `map.values().stream().filter(람다함수).collect(Collectors.toList());`
  - 특히 검색으로 들어온 값이 null 이면 전체를 검색한것처럼 처리!
- **서비스**는 인터페이스 도입 -> 보통 인터페이스 사용X (테스트 때문에 사용했다고 함)
- **Dto**는 제일 하위에서 가지는 계층에 두자
- `@EventListenr(ApplicationReadyEvent.class)` 가 타이밍 관련해서는 `@PostConstruct` 보다 문제 없다. -> 단, 사용시 **"스프링 빈" 등록** 필수
  - 동작시점 : AOP를 포함한 **스프링 컨테이너가 완전히 초기화 된 이후**에 호출
- ItemRepository의 구현체가 아니라 **인터페이스를 테스트하는게 좋다**
- 테이블의 **기본 키를 선택하는 전략**은 크게 2가지 -> 자연 키(ex:주민번호), 대리 키(ex:auto_increment)
  - **대리 키를 권장** -> 변화하는 비지니스 환경 때문

<br>

아래 그림은 **프로젝트의 실행 모습**이다. 해당 프로젝트에 **데이터 접근 기술들을 다양하게 적용**할 예정이다.

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/32ffa831-5db0-440f-999b-e932b293d3b4) 

<br><br>

### JdbcTemplate

**참고) jdbc 로그를 보려면 따로 properties에 설정 추가**

**라이브러리 추가하여 사용**

- 커넥션 흭득
- `statement` 를 준비하고 실행
- 결과를 반복하도록 루프를 실행
- 커넥션 종료, `statement, resultset` 종료
- 트랜잭션 다루기 위한 커넥션 동기화
- 예외 발생시 스프링 예외 변환기 실행

<br>

**주요 동작방식**

- JdbcTemplate -> 순서 기반 파라미터 바인딩을 지원한다. 
- NamedParameterJdbcTemplate -> 이름 기반 파라미터 바인딩을 지원한다. (권장) 
- SimpleJdbcInsert -> INSERT SQL을 편리하게 사용할 수 있다. 
- SimpleJdbcCall -> 스토어드 프로시저를 편리하게 호출할 수 있다.
- **참고 : [사용법 공식 문서](https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html#jdbc-JdbcTemplate)**

<br>

**동적쿼리 문제란?? 예시**

- 검색 조건이 없을 때, 상품명으로 검색 때, 최대가격 검색 때, 상품명과 최대가격 둘다 검색 때
- 위 총 4가지를 전부 동적으로 SQL 생성해야하는데 코드가 복잡해진다. -> MyBatis 가 훨씬 간단

<br>

**간단한 문법 소개**

**(1) 조회**

- 단건 조회 - 숫자 조회

  - ```java
    int rowCount = jdbcTemplate.queryForObject("select count(*) from t_actor", 
    Integer.class);
    ```

- 단건 조회 - 숫자 조회, 파라미터 바인딩

  - ```java
    int countOfActorsNamedJoe = jdbcTemplate.queryForObject(
    "select count(*) from t_actor where first_name = ?", Integer.class,
    "Joe");
    ```

- 단건 조회 - 문자 조회, 파라미터 바인딩

  - ```java
    String lastName = jdbcTemplate.queryForObject(
    "select last_name from t_actor where id = ?", 
    String.class, 1212L);
    ```

- 단건 조회 - 객체 조회

  - ```java
    // RowMapper 를 사용해야 하며 여기선 "람다" 사용
    Actor actor = jdbcTemplate.queryForObject(
    "select first_name, last_name from t_actor where id = ?",
            (resultSet, rowNum) -> {
    Actor newActor = new Actor();
                newActor.setFirstName(resultSet.getString("first_name")); 
                newActor.setLastName(resultSet.getString("last_name"));
    return newActor;
            },
    1212L);
    ```

- 목록 조회 - 객체 조회

  - ```java
    // RowMapper 를 사용해야 하며 여기선 "람다" 사용
    List<Actor> actors = jdbcTemplate.query(
    "select first_name, last_name from t_actor",
            (resultSet, rowNum) -> {
    Actor actor = new Actor();
                actor.setFirstName(resultSet.getString("first_name")); 
                actor.setLastName(resultSet.getString("last_name"));
    return actor;
            });
    
    // 또는 아래처럼 따로 RowMapper 를 구현해도 된다.
    private final RowMapper<Actor> actorRowMapper = (resultSet, rowNum) -> { 
    Actor actor = new Actor();
        actor.setFirstName(resultSet.getString("first_name")); 
        actor.setLastName(resultSet.getString("last_name"));
    return actor; 
    };
    ```

<br>

**(2) 변경(insert, update, delete)**

- 등록

  - ```java
    jdbcTemplate.update(
    "insert into t_actor (first_name, last_name) values (?, ?)", 
    "Leonor", "Watling");
    ```

- 수정

  - ```java
    jdbcTemplate.update(
    "update t_actor set last_name = ? where id = ?", 
    "Banjo", 5276L);
    ```

- 삭제

  - ```java
    jdbcTemplate.update(
    "delete from t_actor where id = ?", 
    Long.valueOf(actorId));
    ```

<br>

**(3) 기타 기능**

- DDL

  - ```java
    jdbcTemplate.execute("create table mytable (id integer, name varchar(100))");
    ```

- 스토어드 프로시저 호출

  - ```java
    jdbcTemplate.update(
    "call SUPPORT.REFRESH_ACTORS_SUMMARY(?)", 
    Long.valueOf(unionId));
    ```

<br>

**소스파일 참고**

- JdbcTemplateItemRepositoryV1 -> JdbcTemplate 기본 사용 문법
- JdbcTemplateV1Config -> 레포지토리, 서비스 "빈 등록" -> V2, V3 은 생략 (유사해서)
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
- JdbcTemplateItemRepositoryV3 -> SimpleJdbcInsert 추가로 자동증가 키 설정이 간단
  - 기존 V2에서 save 함수 부분(insert) 을 SimpleJdbcInsert 로 대체하는 형태

<br><br>

### MyBatis

**기본적으로 JdbcTemplate이 제공하는 대부분의 기능을 제공**

**MyBatis는 SQL을 XML에 편리하게 작성 가능! (동적 쿼리도 편리한 기능 제공!)**

**라이브러리 추가하여 사용**

- properties (main, test 둘다) 파일에도 설정 코드 추가 -> 소스 참고
- Mybatis 매핑 XML을 호출해주는 "매퍼 인터페이스" 필요 -> @Mapper 사용
- `src/main/resources` 하위에 XML 파일 생성 -> 문법 잘 참고
- "매퍼 인터페이스"를 활용해서 레포지토리를 구현 -> 정확히는 "프록시 매퍼 구현체"를 활용

<br>

**주요 동작방식**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/936dfbda-8640-467b-99b4-17189ef094fb) 

**스프링 예외 추상화 (예외변환)도 JdbcTemplate처럼 자동으로 제공**

<br>

**간단한 문법 소개**

**동적 SQL** -> `if, choose (when, otherwise) , trim (where, set), foreach`

- **if**

  - ```xml
    <select id="findActiveBlogWithTitleLike" 
    resultType="Blog">
      SELECT * FROM BLOG
      WHERE state = ‘ACTIVE’ 
      <if test="title != null"> 
        AND title like #{title} 
      </if>
    </select>
    ```

- **choose, when, otherwise** -> switch 구문과 유사

  - ```xml
    <select id="findActiveBlogLike" 
    resultType="Blog">
      SELECT * FROM BLOG WHERE state = ‘ACTIVE’ 
      <choose>
        <when test="title != null"> 
          AND title like #{title} 
        </when>
        <when test="author != null and author.name != null"> 
          AND author_name like #{author.name}
        </when>
        <otherwise> 
          AND featured = 1 
        </otherwise>
      </choose> 
    </select>
    ```

- **trim, where, set**

  - ```xml
    기존 문제점은 아래 if 문 전부 만족 안할시 "where" 만 남는 문제점.
    
    <select id="findActiveBlogLike" 
    resultType="Blog">
      SELECT * FROM BLOG 
      WHERE
      <if test="state != null"> 
        state = #{state}
      </if>
      <if test="title != null"> 
        AND title like #{title} 
      </if>
      <if test="author != null and author.name != null"> 
        AND author_name like #{author.name}
      </if> 
    </select>
    ```

  - ```xml
    그러나 <where></where> 사용시 이러한 문제를 해결 (자동으로 where 없애거나 and 없애거나 해줌)
    
    <select id="findActiveBlogLike" 
    resultType="Blog">
      SELECT * FROM BLOG 
      <where>
        <if test="state != null"> 
             state = #{state} 
        </if>
        <if test="title != null"> 
            AND title like #{title} 
        </if>
        <if test="author != null and author.name != null"> 
            AND author_name like #{author.name}
        </if> 
      </where> 
    </select>
    ```

  - ```xml
    trim 을 사용해도 가능
    
    <trim prefix="WHERE" prefixOverrides="AND |OR ">
      ...
    </trim>
    ```

- **foreach** -> 반복문

  - ```xml
    <select id="selectPostIn" resultType="domain.blog.Post"> 
      SELECT *
      FROM POST P 
      <where>
        <foreach item="item" index="index" collection="list"
    open="ID in (" separator="," close=")" nullable="true">
              #{item} 
        </foreach> 
      </where> 
    </select>
    ```

<br>

**기타 기능**

- **애노테이션으로 SQL 작성** -> xml 대신 애노테이션에 SQL 작성

  - ```java
    @Select("select id, item_name, price, quantity from item where id=#{id}") 
    Optional<Item> findById(Long id);
    ```

- **문자열 대체** -> SQL 인젝션 공격을 당할 수 있어서 권장하지 않음

  - ```java
    @Select("select * from user where ${column} = #{value}")
    User findByColumn(@Param("column") String column, @Param("value") String value);
    ```

- **재사용 가능한 SQL 조각**

  - ```xml
    <sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql> 
    <select id="selectUsers" resultType="map">
      select
        <include refid="userColumns"><property name="alias" value="t1"/></include>, 
        <include refid="userColumns"><property name="alias" value="t2"/></include> 
      from some_table t1
        cross join some_table t2 
    </select>
    ```

- **Result Maps** -> as 별칭을 대체 가능

  - ```xml
    as 별칭 사용 모습
    
    <select id="selectUsers" resultType="User"> 
      select
        user_id             as "id",
        user_name           as "userName", 
        hashed_password     as "hashedPassword" 
      from some_table
      where id = #{id} 
    </select>
    ```

  - ```xml
    resultmap 사용 모습
    
    <resultMap id="userResultMap" type="User"> 
      <id property="id" column="user_id" />
      <result property="username" column="user_name"/>
      <result property="password" column="hashed_password"/> 
    </resultMap>
    <select id="selectUsers" resultMap="userResultMap"> 
      select user_id, user_name, hashed_password
      from some_table 
      where id = #{id} 
    </select>
    ```

<br>

**소스파일 참고**

- ItemMapper 인터페이스 -> Mybatis 매핑 XML을 호출해주는 "매퍼 인터페이스" (@Mapper 사용)

- MyBatisItemRepository -> 프록시 ItemMapper 구현체로 레포지토리 로직 구현
- MyBatisConfig -> 스프링 빈 등록
- resources/hello/itemservice/repository/mybatis/ItemMapper.xml -> SQL 작성!

<br><br>

### JPA

**참고 : [자바 ORM 표준 JPA 프로그래밍 - 기본편](https://github.com/BH946/spring-second-roadmap/tree/main/spring_study_3)**

<br><br>

### Spring Data JPA

JPA를 사용하며 기본적인 기능들을 이미 만들어서 제공해주는 좋은 **도구로 볼 수 있다.**

**라이브러리 추가하여 사용**

- 해당 라이브러리에 **JDBC, 하이버네이트, JPA 도 전부 포함**된다.
- **동적 프록시 기술**이 구현체를 자동으로 만들기 때문에 `JpaRepository` 인터페이스를 상속해서 **인터페이스를 만들기**만 하면 바로 사용가능!

<br>

**주요기능** -> 간단한 건 쿼리메서드, 복잡한 건 @Query 권장

- **기본 공통 기능들** (ex:CRUD) 제공 + 여러 조회들 등등
  - findAll() -> CRUD 로 기본 제공 메소드
    - 변환 JQPL : `select i from Item i`

- **쿼리 메서드 기능 제공** -> 메소드 이름을 문법에 맞게 작성시 자동 JPQL 제공
  - findByItemNameLike()
    - 변환 JQPL : `select i from Item i where i.name like ?`
    - 사용 예시 : `repository.findByItemNameLike("%" + itemName + "%");`
  - findByPriceLessThanEqual()
    - 변환 JQPL : `select i from Item i where i.price <= ?`
  - findByItemNameLikeAndPriceLessThanEqual()
    - 변환 JQPL : `select i from Item i where i.itemName like ? and i.price <= ?`

- **쿼리 직접 실행** -> @Query("...") 사용시 수동 JPQL 작성 가능

  - ```java
    @Query("select i from Item i where i.itemName like :itemName and i.price 
    <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") 
    Integer price);
    ```

<br>

**클래스 의존 관계와 런타임 객체 의존 관계** -> 중간에 어댑터 역할 ver

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/8bb3a73d-6a32-4af8-affa-903268f4363d) 

<br>

**소스파일 참고**

- SpringDataJpaConfig -> 빈 등록하는 설정 파일
- SpringDataJpaItemRepository 인터페이스 -> JpaRepository 인터페이스를 상속하여 생성한 인터페이스
  - **사용법1)** 이녀석을 바로 "서비스" 에서 사용해도 된다.
- JpaItemRepositoryV2 -> 기존에 만들어둔 ItemRepository 인터페이스를 구현하는데 SpringDataJpaItemRepository 까지 활용한 구현체
  - **사용법2)** 이렇게 SpringDataJpaItemRepository 를 중간에 어댑터 역할을 하게 하고 ItemRepository 구현체로써 "서비스" 에서 사용해도 된다.
- **무엇이 더 좋은가? 이런 의문의 해결은 뒤에서 설명!**

<br><br>

### QueryDSL

**스프링데이터JPA의 동적쿼리 작성 문제를 해결해주는 좋은 도구이다.**

**그리고 쿼리는 타입체크가 없어서 에러 잡기가 너무 힘든데 이를 해결해준다.**

**QureyDSL -> JPQL -> SQL 변환**

**라이브러리 추가**

- gradle 에 필요한 것들 추가
  - 라이브러리 추가, 자동 생성 Q클래스를 gradle clean으로 제거
- (참고) 빌드할때 방식에 따라 Q클래스 확인법이 다르다.
  - gradle 빌드방식
  - intelliJ idea 빌드방식

<br>

**동작방식**

- Querydsl을 사용하려면 `JPAQueryFactory` 가 필요 
- JPAQueryFactory 는 JPA 쿼리인 JPQL을 만들기 때문에 `EntityManager` 가 필요
- 설정방식은 `JdbcTemplate` 와 유사

<br>

**findAllOld -> findAll 리팩토링 부분 참고**

- Querydsl을 사용해서 동적 쿼리 문제를 해결

- `BooleanBuilder` 를 사용해서 원하는 where 조건들을 삽입

- 이 모든 것을 자바 코드로 작성!

  - ```java
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
    
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
    
        return query
            .select(item)
            .from(item)
            .where(likeItemName(itemName), maxPrice(maxPrice))
            .fetch();
    }
    
    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }
    
    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }
    ```

<br>

**소스파일 참고**

- QuerydslConfig -> 스프링 빈 등록 설정파일
- JpaItemRepositoryV3 -> QueryDSL 과 JPA 활용
- 그럼 스프링데이터JPA는 사용안하나?? -> 아래에서 설명하겠다.

<br><br>

## 데이터 접근 기술 활용 방안

**어댑터 추가ver** -> 기존 레포지토리 인터페이스를 구현하는데 스프링데이터JPA 까지 추가 사용

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/8bb3a73d-6a32-4af8-affa-903268f4363d) 

<br>

**단순ver** -> 스프링데이터JPA 를 바로 사용
![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/75a7eb76-ad93-4b35-8efe-b674a777dec3)
![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/ea89fc3f-82d0-4721-9987-3c996b1767a0) 

<br>

**트레이드 오프 -> 구조의 안정성 vs 단순한 구조와 개발 편리성**

- **어댑터 추가ver** : DI, OCP를 지키기 위해 어댑터를 도입하고, 더 많은 코드를 유지한다.
- **단순ver** : 어댑터를 제거하고 구조를 단순하게 가져가지만, DI, OCP를 포기하고, ItemService 코드를 직접 변경한다.

<br>

무엇을 선택해야할까? **"상황에 맞게 선택하는게 옳다."**

- 추상화도 비용이 들기 때문에 어설픈 추상화는 오히려 독이 되기도 하기 때문이다.
- 이 추상화 비용을 넘어설 효과가 있을때 추상화를 도입하자.

<br>

**스프링데이터JPA 에 QueryDSL 은 사용안하는가?? -> 실무에선 같이 사용한다.**

아래 그림처럼 해당 구조로 보통 사용

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/84848291-8be9-4f6d-a9df-787527bcab90) 

<br><br>

**그래서 결론은 무엇을 추천하는가??**

- **JPA, 스프링 데이터 JPA, Querydsl 을 기본으로 사용**
  - 어댑터 추가ver, 단순ver 은 상황에 맞게 선택!
  - 상관없다면?? **단순ver을 사용**하자 -> **스프링데이터JPA + QueryDSL** 를 기본으로 잡자!
- **복잡한 쿼리가 잘 해결이 안될때 해당 부분은 JdbcTemplate이나 MyBatis를 함께 사용**
  - JPA랑 JDBC는 트랜잭션 매니저가 다를텐데 어떡하나??
  - `JpaTransactionManager` 가 대부분 기능들을 제공해서 괜찮다고 한다.
  - 단, JPA 플러시 타이밍에 주의

<br>

**소스파일 참고**

- ItemRepositoryV2 인터페이스 -> JPaRepository 인터페이스를 상속받아 생성한 인터페이스
  - 스프링데이터JPA 사용
- ItemQueryRepositoryV2 -> JPAQueryFactory, EntityManager 사용 + BooleanExpression
  - QueryDSL 사용
- ItemServiceV2 -> 위 2개 레포지토리를 사용한 서비스로직

<br><br>

## 트랜잭션 전파

스프링 트랜잭션 추상화는 `PlatformTransactionManager` 인터페이스로 구성되어 있고, 이미 만들어진 JDBC, JPA 등등 다양한 트랜잭션 매니저 구현체를 제공한다고 했었다.

그리고 "실제 트랜잭션 매니저"가 아닌 "프록시" 가 스프링 빈에 등록 된다고 했었다.

**실무에서 트랜잭션을 사용하는데 많은 문제들을 직면하게 될텐데 하나하나 해결해보자.**

<br>

**트랜잭션 AOP 주의 사항 - 프록시 내부 호출**

![image](https://github.com/BH946/bh946.github.io/assets/80165014/f93ed324-fc99-4f22-90d5-0cbd5ee30780) 

![image](https://github.com/BH946/bh946.github.io/assets/80165014/4bcf028a-4ab5-4922-b1a1-3c9da64bbf6a) 

- "클라요청" -> "프록시 호출" -> "실제 트랜잭션 호출" 순서가 일반적인데,
- "클라요청" -> "실제 트랜잭션 호출" 인 경우를 마주칠 수 있다. **=> 트랜잭션 적용X**
  - 자바 문법상 `internal();` 은 `this.internal();` 로 호출하므로 이런 경우가 생긴다.
  - <img src="https://github.com/BH946/bh946.github.io/assets/80165014/7f21cc44-39de-4b33-9c1c-11cb04c155ad" alt="image" style="zoom:50%;" /> 
- 가장 간단한 해결방안은 **"internal 메소드를 별도의 클래스로 분리"**
  - 예로 `static class` 로 `InternalService` 를 하나 만들어서 `internal()` 메소드 구현
  - 사용할때 `InternalService.internal();` 형태로 사용함으로써 `this` 문제 해결 완!
  - ![image](https://github.com/BH946/bh946.github.io/assets/80165014/1ca0f458-d8a2-4876-9322-f6c608b81df7) 

<br>

**트랜잭션 AOP 주의 사항 - 트랜잭션 적용 범위**

- 클래스 레벨에 "트랜잭션 적용"시 스프링은 **public 메서드**만 트랜잭션 적용하도록 기본 설정
  - 물론, 설정 변경 가능하다.
- WHY?? 트랜잭션은 주로 비지니스 로직의 시작점에 걸기 때문에 외부에 열어준 곳을 시작점

<br>

**트랜잭션 AOP 주의 사항 - 초기화 시점**

- @PostConstruct로 선언된곳에 @Transactional 선언시 트랜잭션은 미적용
  - WHY?? **초기화코드(@PostConstruct)가 먼저 호출되고 그 다음 트랜잭션 AOP가 적용되기 때문이다. 따라서 초기화 시점에 해당 메서드는 트랜잭션 흭득 불가.**
  - 해결법?? **@EventListener(ApplicationReadyEvent.class) 사용**
    - 단순히 스프링빈 등록 끝난 시점 이런게 아니라, "스프링 컨테이너가 완전히 초기화 되었을 시점" 을 의미한다. 즉, 스프링빈, 스프링AOP 트랜잭션 등 이런 모든게 다 동작하고 나서 호출되는 시점
    - 그래서 EventListener 방법이 더 안전하다고들 함.

 <br>

**스프링 트랜잭션 전파**

- 트랜잭션이 이미 진행중인데 추가로 수행한다면?? -> 이 경우 어떻게 동작할지 결정하는 것을 **"트랜잭션 전파(propagation)"**
- 여러 트랜잭션들이 중복되어 사용된다면 이들을 구분짓기 위해 "물리,논리 트랜잭션" 개념 사용
  - **모든 트랜잭션 매니저(물리,논리=외부,내부)를 커밋해야 물리 트랜잭션이 커밋된다. 하나의 트랜잭션 매니저라도 롤백하면 물리 트랜잭션은 롤백된다.**
  - **트랜잭션 참여 : 외부 트랜잭션과 내부 트랜잭션이 하나의 물리 트랜잭션으로 묶이는 것**
  - **같은 물리 트랜잭션 사용 == 같은 동기화 커넥션 사용**
  - <img src="https://github.com/BH946/bh946.github.io/assets/80165014/1bd94b66-7022-47a1-a4a3-eaa68bfb6e90" alt="image" style="zoom: 80%;" /> 

<br>

**스프링 트랜잭션 전파 - 트랜잭션 두 번 사용**

- **(1) 순차적으로 2번 사용은?**
  - 그림처럼 **"같은 커넥션(=conn0)"**를 사용, **"다른 프록시 커넥션"**을 사용.
  - ![image](https://github.com/BH946/bh946.github.io/assets/80165014/fc7c6efa-59b2-4f23-938e-598b33fc1374) 
  - <img src="https://github.com/BH946/bh946.github.io/assets/80165014/404db731-4946-4c9b-9f3f-fcfa176de46a" alt="image" style="zoom:80%;" />  
- **(2) 트랜잭션이 순차가 아닌 중복되게 사용하면? (커밋)**  
  아래 코드처럼 "외부 시작 -> 내부 시작,커밋 -> 외부 커밋" 라면?
  - 신규 트랜잭션 이라면 -> 커밋O
  - 신규 트랜잭션 아니라면 -> 커밋X
  - ![image](https://github.com/BH946/bh946.github.io/assets/80165014/c618cb03-7222-41dd-a621-f64adc8193c5) 
  - ![image](https://github.com/BH946/bh946.github.io/assets/80165014/6ddcfaa9-c92a-45bd-ba08-f7c90895cb9f)
  - ![image](https://github.com/BH946/bh946.github.io/assets/80165014/11c5095f-7529-4c17-8ddb-94b8704c3e6c)
  - ![image](https://github.com/BH946/bh946.github.io/assets/80165014/f3953407-7b50-4f84-9425-140e4a00c047) 

- **(3) 트랜잭션이 순차가 아닌 중복되게 사용하면? (롤백)**  
  - 내부 롤백은 내부적으로 따로 롤백 마킹
    - 외부 커밋 시점에 이를 확인해서 롤백 -> **rollbackOnly 설정을 확인**
  - 이때, **UnexpectedRollbackException** 런타임 예외를 추가로 던진다.
    - **롤백은 중요한 일**이므로 이런 예외를 추가로 던진다.
- **(4) 트랜잭션이 순차가 아닌 중복되게 사용하면? (REQUIRES_NEW)**  
  - REQUIRES_NEW 라는 옵션을 사용하면 **"외부 트랜잭션과 내부 트랜잭션을 분리 가능"**
  - 즉, **독립적인** 트랜잭션이 2개 생기는 것이며 2개가 **동시에 사용**되는 것 -> 정확히는 1개씩 커넥션 순차적으로 처리 된다.
  - <img src="https://github.com/BH946/bh946.github.io/assets/80165014/d09cac77-80eb-4ec8-b731-d3a62fac9ecd" alt="image" style="zoom:80%;" /> 

<br>

스프링 트랜잭션 전파에 다양한 옵션들이 있지만 주로 **"REQUIRED, REQUIRES_NEW" 옵션**만 사용해서 2개만 기억해두자.

- 참고로 REQUIRED 는 기본값!

<br><br>

# Folder Structure

생략

