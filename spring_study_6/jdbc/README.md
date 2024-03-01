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
  - **이 모든것을 한번에 해결? @Transactional 사용**
- **예외 누수 문제**
  - **스프링**은 데이터 접근 계층에 대한 **"일관된 예외 추상화"**를 제공 + **"예외 변환기"** 제공
    - 스프링이 제공하는 데이터 접근 예외를 사용하기만 하면 됨
- **JDBC 코드 반복 문제** -> 레퍼지토리 계층
  - JdbcTemplate 사용

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

JDBC 적용 : `MemberRepositoryV0.java` 와 테스트코드 참고 -> 옛날 방식 그대로

<br>

## 커넥션 풀, DataSource

**커넥션 풀**은 **실무**에서 무조건 사용, **성능테스트**를 통해 결정하는편, `hikariCP` 를 **기본으로 스프링 부트가 제공**(커넥션풀 오픈소스)

- **별도의 스레드를 사용**해서 커넥션 풀에 커넥션을 채움 -> 오래걸리기 때문
- 10개 제한인데 11개 커넥션시 **30초 후 예외터짐(기본값)**
- `MyPool - Pool stats (total=8, active=2, idle=6, waiting=0)` 로그 : 총개수, 사용개수, 풀에서 대기개수, 대기개수

**DataSource**는 다양한 커넥션 풀을 구현체로 둬서 쉽게 변경하기위한 **인터페이스**

- 기존 DriverManager 사용시 매번 "신규 커넥션 생성" -> 커넥션 풀로 변경하자! (기본 제공중)
  - 단, DriverManager는 DataSource를 사용하지 않음 -> 사용하는 클래스 따로 제공 `DriverManagerDataSource`

<img src="https://github.com/BH946/spring-first-roadmap/assets/80165014/97b244c2-5445-4361-8a6b-c3a1fd8f4b5c" alt="image" style="zoom: 80%;" /> 

<br>

`ConnectionTest.java, MemberRepositoryV1.java` 코드(+테스트코드)를 참고 -> Datasource 사용과 커넥션 풀 사용모습

- 참고로 커넥션 풀 로그보려면 **로그레벨 DEBUG** 로 해서 봐야함 -> 스프링부트 3.1부터 바뀜

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

예외 개념만 여기서먼저 정리해두자. -> 간략하게만.





<br><br>

## 스프링 제공 - 트랜잭션





<br><br>

## 스프링 제공 - 예외 처리, 반복





<br><br>

# Folder Structure

생략

