## Intro..

**스프링 입문 - 코드로배우는 스프링부트, 웹 MVC, DB 접근 기술**

* 인프런 강의듣고 공부한 내용입니다.



해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* 하다보니 좀 많이 정리해버렸다..

<br>

## 프로젝트 환경설정 & 생성

**준비물**

* Java 11
* IDE: IntelliJ (이클립스도 가능합니다)



**스프링 프로젝트 생성**

* [프로젝트 생성하는 곳](https://start.spring.io)
  * Project: Gradle - Groovy Project 
  * Spring Boot: 2.x.x
  * Language: Java
  * Packaging: Jar
  * Java: 11
* 참고로  `2.7.1 (SNAPSHOT)` 이런 형태가 아닌 `2.7.0` 처럼 영어가 안 붙은걸 선택 권장
* 이후에 IntelliJ로 폴더 오픈



**추가 설정**

* IntelliJ Gradle 대신에 자바직접실행
* 최근 IntelliJ 버전은 Gradle을통해서실행 하는것이기본설정이다. 이렇게 하면실행속도가느리다. 
* 다음과같이 변경하면 자바로바로실행해서 실행속도가더빠르다.
* Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle 
  * Build and run using: Gradle -> IntelliJ IDEA
  * Run tests using: Gradle -> IntelliJ IDEA
  * 참고로 File -> Setting에서 검색해서 바로 찾아도 됨
* 그리고 설치한 `jdk11` 로 프로젝트, gradle 설정 해줘야 한다.
  * 위에서 접근한 Build Tools -> Gradle 에서 jdk11로 설정(java11)
  * File -> Setting에서 바로 Project Setting -> Project 검색해서 이곳도 jdk11로 설정(java11)



**단축키 확인 법**

* File -> Settings -> keymap 에서 검색해서 확인

<br>

## 빌드하고 실행법

**서버를 실제로 실행할 때는 아래 방법으로 빌드하고 얻은 `jar` 파일로 서버에서 구동 시킨다.**

**참고로 과거에는 서버에 톰캣도 설치하고 등등 해야했는데 요즘은 이걸로 끝!!**

* `콘솔로 이동(EX : cmd)`

* `./gradlew build`  -> `cd libs` -> `java -jar hello-spring-0.0.1-SNAPSHOT.jar`

<br>

## 라이브러리 이해

* spring-boot-starter-web
* spring-boot-devtools
  * 서버 재구동 없이 `html` 파일만 컴파일해서 웹에 바로 적용 가능
* spring-boot-starter-tomcat: 톰캣 (웹서버) 
  * 톰캣을 따로 설치해서 실행할 필요없이 이렇게 간편하게 `스프링 부트` 가 실행도 해준다는 점!!
* spring-webmvc: 스프링 웹 MVC
* spring-boot-starter-thymeleaf: 타임리프 템플릿엔진(View) 
* spring-boot-starter(공통): 스프링 부트 + 스프링코어 + 로깅
* spring-boot 
* spring-core
* spring-boot-starter-logging 
* logback, slf4j
* **테스트 라이브러리**
  * spring-boot-starter-test
    * junit: 테스트 프레임워크 
    * mockito: 목라이브러리
    * assertj: 테스트 코드를좀더 편하게작성하게도와주는라이브러리 
    * spring-test: 스프링통합테스트지원

<br>

## Main

아래 코드를 보면 `SpringApplication.run` 으로 현재 메인 클래스를 실행

* `@SpringBootApplication` 이 실행(톰캣 내장)
* http://localhost:8080 으로 접속

```java
@SpringBootApplication // 톰캣 내장(서버)
public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}

}
```

<br>

## Welcom Page

스프링 부트가 제공하는 `Welcome Page`



### MVC(Model View Controler)

**모델, 화면, 컨트롤러 구조를 사용하는 MVC 패턴**

* M : 아래 코드 맨 아래에 inner class로 생성 하였다.
* V : html인데, 각각 컨트롤러 리턴값에 맞게 이름을 설정해서 작성한다.
* C : Controller 부분이며 이것이 아래 예시 코드이다.
  * @Controller 꼭 필요!
  * **나중에 스프링 빈에 등록할 때도 꼭 얘는 이방법으로 등록해야한다는 특징 기억!**

```java
@Controller // controller 라고 알려줌
public class HelloController {

    // default
    @GetMapping("hello") // URL의 hello로 라우터 매핑(GET방식)
    public String hello(Model model) {
        model.addAttribute("data", "hello!!"); // model에 담아서 데이터 html에
        return "hello"; // 약속(라우터)
    }

    // static
    // 그냥 바로 html 만 사용

    // parameter
    @GetMapping("hello-mvc") // URL 매핑(GET)
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template"; // 해당 파일명을 실행(반환)
    }

    // API => 문자 반환
    // 이전엔 html 화면 구성이였다면, 여기는 그대로 입력 데이터 값을 반환
    @GetMapping("hello-string")
    @ResponseBody // viewResolver 사용 X
    public String helloString(@RequestParam("name") String name) { // ?name=이름
        return "hello " + name;
    }

    // API => 객체 반환
    @GetMapping("hello-api")
    @ResponseBody // viewResolver 사용 X
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; // => 자동 json으로 처리
    }

    // inner class (static으로 선언)
     static class Hello{ // Hello 객체 구조
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
```



### 동작 환경

* **스프링 부트는 컨트롤러가 있는지 우선 확인 => 즉, 정적보다 컨트롤러가 우선순위 높음**
  * 컨트롤러가 없으면 `static`안에 `html` 이 맞는게 있는지 확인 후 있으면 출력
  * 컨트롤러가 있었다면 해당 `return`값의 파일명과 같은 `html`을 출력 => 이건 `viewResolver`가!
    * `@ResponseBody`가 있으면 `viewResolver`가 아닌 `HttpMessageConverter`가 동작!
      * `JsonConverter or StringConverter` 를 동작 시키게 됨



#### thymeleaf 템플릿엔진 동작

**템플릿 엔진은 과거의 jsp, php 같은것이고 이는 서버에서 동적으로 좀 바꿔서 html을 넘기는 특징**

* **이를 위해 MVC 패턴을 많이 이용**

* 웹 브라우저 -> 톰캣 -> 스프링 컨테이너
  * 우선 Controller 를 찾고, 있으면 viewResolver 가 활동해서 화면 출력

<img src=".\images\image-20230121222919782.png" alt="image-20230121222919782"  />



#### 정적 컨텐츠 동작

**정적 컨텐츠는 서버에서 뭐 하는것 없이 바로 웹에 html을 보여주는 형식**

* 스프링 부트는 컨트롤러를 먼저 찾고, 해당 컨트롤러가 없으면 정적(static) 에서 찾는다.

<img src=".\images\image-20230121224130633.png" alt="image-20230121224130633"  />



### API 동작

**json 같은 데이터 구조 포맷으로 클라이언트 한테 전달 및 서버끼리 전달하는 요즘 개발 패턴**

* 스프링 부트가 컨트롤러를 찾고, 컨트롤러가 viewResolver 대신 HttpMessageConverter를 동작시킴
  * 기본문자처리: StringHttpMessageConverter
  * 기본객체처리: MappingJackson2HttpMessageConverter
  * byte 처리 등등기타여러 HttpMessageConverter가 기본으로등록되어있음
* 참고: 클라이언트의 HTTP Accept 해더와 서버의 컨트롤러 반환타입 정보 둘을 조합해서  HttpMessageConverter 가 선택된다.

<img src=".\images\image-20230121224502957.png" alt="image-20230121224502957"  />

<br>

## 일반적인 웹 애플리케이션 계층 구조

<img src=".\images\image-20230122182221632.png" alt="image-20230122182221632"  />

* **컨트롤러: 웹 MVC의 컨트롤러역할**
* 서비스: 핵심 비즈니스 로직구현
* 리포지토리: 데이터베이스에접근, 도메인 객체를 DB에 저장하고 관리
* 도메인: 비즈니스도메인객체, 예) 회원, 주문, 쿠폰등등 주로데이터베이스에저장하고 관리됨

<br>

## 테스트 케이스

테스트 코드를 작성하는 것은 실무에서 매우 중요하다.

* main 메서드나, 웹 애플리케이션의 컨트롤러를 통해서 실행을 하므로, 실행하는데 오래걸리고 반복 실행하기나 여러 테스트를 한번에 실행하기 어렵다.
* 따라서 `JUnit` 이라는 프레임워크를 활용해서 테스트 코드를 작성한다.
* `Ctrl + R` 을 누르면 마지막에 테스트한 코드를 재실행(매우 간편!)



### 기본

`@Test` 만 적으면 `JUnit` 라이브러리를 알아서 import 한다.

* 이후에는 진짜로 테스트할 코드들을 적어주면 된다.

**테스트는 `given, when, then` 을 주석 달아서 나눠서 코드 짜는걸 추천한다.**

* given에 멤버 이름 설정
* when에 서비스의 join함수 사용(회원가입 되는지 확인하는 것)
* then에 결과를 보는것. 멤버이름이 잘 생겼는지 등등..(앞서 테스트코드했을때처럼 assert보통 씀!)

중요한점은 테스트 코드 실행할 때 함수 순서는 자기 맘대로 실행하기 때문에, 이전 테스트한 내용을 지워주는 형식이 필요하다.

* `@AfterEach` 는 테스트 끝나면 실행되는 것인데, 이를 활용한다.
* `@BeforeEach` 는 테스트 시작전 실행되는 것인데, 이것도 활용할 수 있다.

또한 보통 print로 값이 잘나왔다던지 비교를 한다던지 할텐데, 이렇게 하기 싫으면 ?

* `Assertions`을 이용하자. 이걸 사용해서 `assertEquals`함수 사용시 두개 인자가 동일한지 봐준다
  * 안동일하면 오류, 동일하면 아무일도 없음
* `Assertions.assertThat(member).isEqualeTo(result);` 이것도 위처럼 사용된다.

예외 테스트의 경우?

* try, catch보다 간편하게 `assertThrows`를 사용해서 일부러 예외를 터트려서 테스트 하는것이 있다.



### 스프링 통합 테스트 - 중요

스프링 컨테이너와 DB까지 연결한 통합 테스트를 진행해보자.

* @SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.
* @Transactional : 테스트 케이스에 이 애노테이션이 있으면, 테스트시작 전에 트랜잭션을 시작하고, 
  테스트완료 후에 항상 롤백한다. 이렇게하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지 
  않는다.



**참고 코드**

```java
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("hello");
        //When
        Long saveId = memberService.join(member);
        //Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }
    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("spring1");
        Member member2 = new Member();
        member2.setName("spring1");
        //When
        memberService.join(member1); // 처음 회원가입은 당연히 중복 아닐테고, 이 아래 join은 중복일테니 예외처리 되는지 검사하는 코드
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
```

<br>

## 회원 관리 예제 - 백엔드 개발

**들어가기전에, 구조 다시 리마인드**

* 스프링 부트는 컨트롤러가 있는지 우선 확인 => 즉 정적보다 컨트롤러가 우선순위 높음
* 없으면 static에 html 에 맞는게 있는지 확인 후 있으면 출력
* 컨트롤러가 있었다면 해당 return값의 파일명과 같은 html을 출력 => 이건 viewResolver가!
* @ResponseBody가 있으면 viewResolver가 아닌 HttpMessageConverter가 동작!



### 비지니스 요구사항 정리

* 데이터: 회원ID, 이름 
* 기능: 회원등록, 조회
* 아직데이터저장소가 선정되지않음(가상의시나리오)

<img src=".\images\image-20230122182414655.png" alt="image-20230122182414655"  />



### 자세한건 해당 코드들 확인

* **컨트롤러**
  * `MemberController.java`
* **리포지토리**
  * `MemberRepository.java` - 인터페이스
  * `MemoryMemberRepository.java` - 메모리 저장소 사용
* **서비스**
  * `MemberService.java`

* **테스트 코드**
  * `MemoryMemberRepositoryTest.java`
  * `MemberServiceTest.java`

<br>

## 컴포넌트 스캔과 자동 의존관계 설정

**아래그림의 구조를 만들기 위해 스프링 빈에 등록을 해야한다.**

* 참고: 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록한다(유일하게하나만 
  등록해서 공유한다) 
* 따라서 같은 스프링 빈이면 모두 같은 인스턴스다. 
  * 설정으로 싱글톤이 아니게 설정할 수 있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용한다.

<img src=".\images\image-20230122200406422.png" alt="image-20230122200406422"  />



**스프링 빈을 등록하는 2가지 방법**

* 컴포넌트 스캔과 자동 의존관계 설정 
* 자바 코드로 직접 스프링 빈 등록하기



**여기서는 향후 메모리 리포지토리를 다른 리포지토리로 변경할 예정이므로, 컴포넌트 스캔방식 대신에** 
**자바 코드로 스프링 빈을 설정하겠다.**



### 컴포넌트 스캔 원리

* @Component 애노테이션이 있으면 스프링 빈으로 자동 등록된다.
* @Controller 컨트롤러가 스프링 빈으로 자동 등록된 이유도 컴포넌트 스캔 때문이다. 
* @Component 를포함하는다음 애노테이션도 스프링 빈으로 자동 등록된다.
  * @Controller 
  * @Service 
  * @Repository

* 참고: 생성자에 @Autowired 를 사용하면 객체 생성시점에 스프링컨테이너에서 해당스프링 빈을 찾아서 
  주입한다. 
  * 생성자가 1개만있으면 @Autowired 는 생략할수 있다.
  * 주의: @Autowired 를 통한 DI는 helloController , memberService 등과 같이 스프링이 관리하는 
    객체에서만 동작한다. 스프링빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않는다.



### 자바 코드로직접 스프링빈 등록하기
회원 서비스와 회원 리포지토리의 @Service, @Repository, @Autowired 애노테이션을 제거하고 진행한다.

* **컨트롤러는 @Controller로 등록을 해야하기 때문에 제거하지 않는것**

- `service`폴더에 딱 `config` 파일 하나 만들어서 위그림의 구조로 코드만 다 적어주면 스프링 빈 등록 완료
  * 참고로 `@Configuration` 을 적어줘야 스프링 부트가 이해함.
  * 또한 `@Bean` 을 적어서 스프링 빈에 등록한다는 것도 알려줘야함.

<br>

## 회원 관리 예제 - 웹 MVC 개발

참고로 MVC는 앞에서 모델, 뷰, 컨트롤러 패턴으로 개발하는 방식이라고 했다.

**만약 홈 화면 추가를 한다면?**

* 스프링부트는 정적파일보다 컨트롤러를 먼저 찾고, 그럼 만들어둔 Home컨트롤러 먼저찾음. 여기선 "/"로 등록해둠.
* 따라서 `home.html`이 출력됨**(index.html무시되고)**



**또한 등록화면을 따로 createMemberForm.html을 만들어서 구현 한다면?**

* 처음에 만들었던 `MemberController.java` 에서 등록화면 뷰를 매핑한다.
  * `@GetMapping`



**여기서 만약 회원 검색을 위해 뷰에서 form 태그를 이용해 post 요청을 보내는 경우라면?**

* `@PostMapping` 도 추가해주면 된다.(post로 요청했으니 이걸로 매핑)
  * 여기서 입력받은 회원 이름을 `MemberService` 에 만든 `join` 메소드로 `멤버 리포지토리` 를 이용해 DB(여기선 메모리)에 저장
  * 참고로 여기서 인자로 form 데이터 받을때 미리 `Model` 을 만들어둬서 이걸 타입으로 사용했다면?
    * `form태그`의 속성이름과 `Model`에서의 변수명을 `name`으로 똑같이 설정했으므로 spring에서 자동으로 값 적용해줌
    * 참고로 만든 `Model` 은 `MemberForm.java` 이다.



**이번엔 메모리에 저장한 이 회원이름을 조회하는 경우?**

* `MemberController` 에서 회원 조회하는 뷰를 매핑하기위해 `@GetMapping` 을 먼저한다.
  * 이후에는 `Model` 클래스라고 뷰에 보여주기위해 사용할 수 있는 클래스가 있다. 이를 활용!
  * `MemberService` 에 있는 메소드인 `findMebers()` 를 이용해서 저장되어 있는 회원 이름들을 가져오고,
  * 위에서 언급한 `Model` 타입으로 `addAttribute(속성, 데이터)` 형태로 저장하면 된다.

* 마지막은 회원 조회하는 뷰에서 `thymleaf` 템플릿의 문법을 이용해서 해당 데이터를 출력한다.

<br>

## 스프링 DB 접근 기술

* **순수 Jdbc**
  * 옛날 방식
* **스프링 JdbcTemplate** 
  * 순수 Jdbc를 발전시킨 방식(반복 코드를 대부분 제거, SQL은 그대로 직접 작성)
* **JPA**
  * 더 발전된 방식(객체를 쿼리없이 바로 DB에 저장)
* **스프링 데이터 JPA**
  * JPA에서 더 발전된 방식으로 더욱 쿼리없이 사용가능



### H2 데이터베이스

개발이나 테스트 용도로 가볍고 편리한 DB를 활용, 해당 DB는 웹 화면으로 제공

* **https://www.h2database.com/html/download-archive.html**
* **해당 사이트에서 1.4.200 버전을 설치 권장**



<img src=".\images\image-20230122203919618.png" alt="image-20230122203919618"  />

* `h2.sh` 존재 꼭 확인
  * 윈도우 사용자는 `./h2.bat` 으로 실행
  * 아닌 사용자는 `./h2.sh` 로 실행



**최초 DB 생성?**

* 이후에 `jdbc:h2:~/test` 로 JDBC URL: 에 적고 연결한다(비밀번호 없어도 됨)
* 다시 터미널에서 DB 생성되었나 확인을 위해 `cd ~` 경로로 접근해서 `ls` 명령어로 파일들을 확인
* `test.mv.db` 파일이 있다면 생성 성공!
  * 참고로 DB 생성 확인 이후부터는 소켓별로 접속 전부 할 수 있게끔
  * JDBC URL:에 `jdbc:h2:tcp://localhost/~/test` 이렇게 접속한다.
* 문제가 있다면?? 터미널에서 `rm 파일명..` 으로 삭제했다가 다시!



**간단히 테이블 생성 해보기**

* 아래 코드를 `h2` db에서 실행해보고, insert랑 select를 이용해서 확인!
* 참고로 이러한 sql문을 관리할때는 프로젝트 루트에 `sql/ddl.sql` 파일을 생성해서 관리하는 편이라 함.

```sql
drop table if exists member CASCADE; 
create table member
(
    id  bigint generated by default as identity, 
    name varchar(255),
primary key (id) 
);
```



### 순수 JDBC

**build.gradle 파일에 jdbc, h2 데이터베이스관련라이브러리추가(build.gradle)**

```
implementation 'org.springframework.boot:spring-boot-starter-jdbc' 
runtimeOnly 'com.h2database:h2'
```



**스프링부트 데이터베이스연결 설정추가(resources/application.properties)**

* 참고로 자신의 `프로젝트/src/main/resources/` 에 있는 파일을 의미하는것.
* 다른경로에도 `application.properties` 파일이 있기때문에 꼭 햇갈리지 말것..!

```
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
```



**참고로 순수 JDBC API로 직접 코딩하는 것은 옛날 이야기 이므로 참고만하고 넘어갈 것.**

* 참고로 해보면 기존 옛날 방식으로 자바로 DB 연동할 때 쓰는 방식과 매우 유사한 방식!



**참고 코드(save)**

```java
public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource;
    public JdbcMemberRepository(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }
    @Override
    public Member save(Member member) {
        // sql문 ? 에 변수 넣을거임
        String sql = "insert into member(name) values(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // RETURN_GENERATED_KEYS 는 insert하면 자동으로 db에 id값 주는 그 id 값을 가져옴
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            // setString에서 위에 ?변수에 값을 적용하는 것
            pstmt.setString(1, member.getName());
            // DB에 쿼리를 실질적으로 날리는 함수
            pstmt.executeUpdate(); 
            // getGeneratedKeys 함수로 위의 id 값을 매핑시켜서 값을 가져옴
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            // 자원 할당 해제는 반드시 필요
            close(conn, pstmt, rs);
        }
    }
}
```



* 반드시 `SpringConfig.java` 에서 스프링 빈 설정 바꿔줘야함 (아래처럼 구조로 바꾸기위해)



<img src=".\images\image-20230123015715763.png" alt="image-20230123015715763"  />



<img src=".\images\image-20230123015847813.png" alt="image-20230123015847813"  />



### **스프링 JdbcTemplate** 

**설정은 순수 JDBC에서와 동일**



**간추려진 코드 확인(save)**

* `SimpleJdbcInsert` 같은 더 편리한 기능들이 많음

```java
public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    // constructor
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        // 데이터소스로 DB에 접근해라
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate); 
        // 'member' 테이블에 삽입, 'id' 컬럼의 값을 key로 반환하라
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id"); 
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        // 데이터 execute 및 primary key 얻어냄
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)); 
        member.setId(key.longValue());
        return member;
    }
}
```



* 마지막은 또 `SpringConfig.java` 스프링 빈 등록 바꾸기



### JPA

**더욱 발전된 버전이며, 기존의 반복 코드는 당연히 생략하고 기본적인 SQL도 JPA가 직접 만들어서 실행!**

* JPA를 사용하면, SQL과 데이터중심의 설계에서객체중심의 설계로 패러다임을 전환을 할 수있다.
* JPA를 사용하면 개발생산성을 크게 높일 수있다.



**처음 JDBC에서 설정한것 처럼 JPA도 약간의 추가 설정이 필요하다.**

**build.gradle 파일에 JPA, h2 데이터베이스 관련라이브러리 추가**

* jpa 라이브러리에 jdbc 관련 라이브러리가 포함되기 때문에 제거해도 되는것(주석)

```
dependencies {
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf' 
implementation 'org.springframework.boot:spring-boot-starter-web'
//implementation 'org.springframework.boot:spring-boot-starter-jdbc' 
implementation 'org.springframework.boot:spring-boot-starter-data-jpa' 
runtimeOnly 'com.h2database:h2'
testImplementation('org.springframework.boot:spring-boot-starter-test') {
exclude group: 'org.junit.vintage', module: 'junit-vintage-engine' 
}
}
```



**스프링부트에 JPA 설정추가(resources/application.properties)**

* 참고로 자신의 `프로젝트/src/main/resources/` 에 있는 파일을 의미하는것.
* 다른경로에도 `application.properties` 파일이 있기때문에 꼭 햇갈리지 말것..!
* **새로 추가된것 2가지**
  * `show-sql` : JPA가 생성하는 SQL을 출력한다.
  * `ddl-auto` : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 none 를 사용하면 해당 기능을 끈다. 
    * ​	create 를 사용하면 엔티티 정보를 바탕으로 테이블도 직접 생성 해준다.

```
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```



**JPA는 ORM이란 기술(O:객체, R:관계, M:매핑)**

* `@Entity` 붙이면 jpa가 관리하는 엔티티가 된다고 판단
* `@ID` 즉, PK(기본키) 연결해야할텐데 `@GeneratedValue`를 통해 `IDENTITY`로 설정함.
  - 아이덴티티가 뭔가? 
  - 우리가 직접 DB에 id로 키값을 삽입한게 아니라 DB에서 자동으로 키값을 1,2,3... 순으로 적용시킨걸 아이덴티티라 함.

```java
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    ...
}
```



**JPA 회원 리포지토리**

* `EntityManager` 를 통해 jpa는 `Entity` 를 동작한다
  * data-jpa 라이브러리 받았을때 스프링 부트가 이걸 만들어 줌
* 아래 `save` 코드를 보면 정말 줄었다는것을 알 수 있다.
* **참고로 save나 findById 들은 PK 기반인데, PK 기반 아닌것들은 findByName, findAll 이며 이건 JPQL로 작성을 해줘야함.**
  * **스프링 데이터 JPA의 경우 이 조차도 쿼리 안짜도 된다는 것**

```java
public class JpaMemberRepository implements MemberRepository {
    private final EntityManager em;
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    public Member save(Member member) {
        em.persist(member);
        return member;
    }
    ...
}
```



**서비스 계층에 트랜잭션 추가**

* 스프링은 해당클래스의 메서드를 실행할 때 트랜잭션을 시작하고, 메서드가 정상 종료되면 트랜잭션을 
  커밋한다. 
* 만약 런타임예외가 발생하면 롤백한다.
* JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행 해야한다.

```java
import org.springframework.transaction.annotation.Transactional 
    
@Transactional
public class MemberService {...}
```



**또한 마지막은? 스프링 설정 변경(SpringConfig.java)**

* 참고로 `dataSource` 가져올 때 처럼 `EntityManager (em을 의미)` 도 똑같이 바로 가져와 사용 가능

```java
    @Bean
    public MemberRepository memberRepository() throws SQLException {
//    return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }
```



### 스프링 데이터 JPA

제일 최신 기술이니 `스프링 데이터 JPA` 이다.

물론 이 기술만 배우면 실무에서는 문제가 생기면 해결하기 어렵다고 한다.

따라서 공부하는 권장 순서는 `JPA -> 스프링 데이터 JPA` 를 권장한다.



**설정 관련해서는 앞의 JPA 설정을 그대로 사용!**



**스프링 데이터 JPA 회원 리포지토리**

* **아래코드가 끝....** 
  * `JpaRepository` 클래스를 상속받게만 해놓으면, 스프링 데이터 JPA가 스스로 이 **인터페이스**의 구현체를 만들어서 스프링에 등록을 한다고함.
  * **그래서 여기서 코드를 인터페이스로만 작성 한 것!**
    * 참고로 인터페이스는 다중상속이 가능하므로 우리가 처음에 만들었던 `MemberRepository` 인터페이스도 같이 상속한것!
* 나머지는 인터페이스를 통한 기본적인 CRUD 등을 스프링 데이터 JPA가 그냥 제공
* `findByName() , findByEmail()` 처럼 메서드 이름 만으로도 조회 기능 자동 제공
* 페이징 기능 자동 제공

```java
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,
        Long>, MemberRepository {
    Optional<Member> findByName(String name);
}
```



**스프링 설정 변경도 더욱 간단**

* 위에 설명한것 처럼 자동으로 구현체 만들어주기 때문에 `memberRepository` 도 바로 사용이 가능한 것
* 코드보면 바로 서비스에 사용시킨것을 볼 수 있다.

```java
@Configuration
public class SpringConfig {
    private final MemberRepository memberRepository;
    
    public SpringConfig(MemberRepository memberRepository) {
    	this.memberRepository = memberRepository; 
    }
    
    @Bean
    public MemberService memberService() {
    	return new MemberService(memberRepository); 
    }
}
```



**상속받은 JpaRepository 인터페이스 구조**

<img src=".\images\image-20230124165121701.png" alt="image-20230124165121701"  />

* 앞서 얘기했듯이 CRUD나 페이징 기능 등등을 이렇게 제공하고 있는 것



**참고**

* 실무에서는 JPA와 스프링 데이터 JPA를 기본으로 사용하고, 복잡한 동적쿼리는 Querydsl이라는 
  라이브러리를 사용하면 된다. 
* Querydsl을 사용하면 쿼리도 자바코드로 안전하게 작성할 수 있고, 동적 쿼리도 편리하게 작성할 수 있다. 
* 이 조합으로 해결하기 어려운 쿼리는 JPA가 제공하는 네이티브 쿼리를 사용하거나, 앞서 학습한 스프링 JdbcTemplate를 사용하면 된다.

<br>

## AOP: Aspect Oriented Programming



### AOP가필요한 상황

* 모든 메소드의 호출 시간을 측정하고 싶다면?
  * 예로 회원 조회 시간이 궁금해서 간단히 `timemills` 같은 함수를 이용해서 시간을 구했다고 하자.
  * 그런데 회원 조회 함수는 회원 조회하는게 핵심 관심 사항이다.
  * 시간을 구하는 로직은 공통 관심 사항일 뿐이다.
* 시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 유지보수가 어렵다. 
* 시간을 측정하는 로직을 별도의 공통 로직으로 만들기 매우 어렵다. 
* 시간을 측정하는 로직을 변경할 때 모든 로직을 찾아가면서 변경해야 한다.



### AOP 적용

**공통관심사항(cross-cutting concern) vs 핵심관심 사항(core concern) 분리하는 것을 의미한다.**

* **원하는 곳에 타겟팅을 해서 적용할 수 있다!!**

<img src=".\images\image-20230124172613638.png" alt="image-20230124172613638"  />



```java
@Aspect // AOP 사용!
@Component // 스프링 빈 등록
public class TimeTraceAop {

    // @Around 로 이 공통관심사항을 적용할 곳에 타겟팅을 할 수 있음
    // "execution(* hello.hellospring..*(..))" 은 해당 패키지 하위 전부 의미
    // "execution(* hello.hellospring.service..*(..))" 은 해당 서비스 하위 전부 의미
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
```



### 동작 구조



**AOP 적용 전 전체 그림**

<img src=".\images\image-20230124172739017.png" alt="image-20230124172739017"  />



**AOP 적용 후 전체 그림**

* 예로 memberService의 경우 만약 AOP적용 한다고 스프링이 판단하면, 
* 프록시라고 실제가 아닌 가상으로 실행해서 joinPoint.proceed() 를 만나면 실제 memberService를 실행하는 것이다.

<img src=".\images\image-20230124172756957.png" alt="image-20230124172756957"  />



**실제 Proxy가 주입되는지 콘솔로 확인**

* MemberService뒤에 Enhancer... 라면서 뒤에 덧 붙여있는게 실행되었다는걸 알 수 있다.

<img src=".\images\image-20230124172905478.png" alt="image-20230124172905478"  />

<br>

## Folder Structure

생략..
