# Intro..

**스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **프로젝트 총 3가지** -> 현재 포스팅은 springmvc 프로젝트
  * servlet : 서블릿, JSP, MVC 패턴, MVC 프레임워크, 스프링 MVC 이해
  * **springmvc : 스프링 mvc 기능 알아보기**
  * item-service : 스프링 mvc 로 프로젝트 해보기(웹페이지 만들기)


<br>

**아래 순서로 구현**

- 로그 출력
  - [로그 테스트](http://localhost:8080/log-test)
- 요청 매핑
  - [hello-basic](http://localhost:8080/hello-basic)
  - [HTTP 메서드 매핑](http://localhost:8080/mapping-get-v1)
  - [HTTP 메서드 매핑 축약](http://localhost:8080/mapping-get-v2)
  - [경로 변수](http://localhost:8080/mapping/userA)
  - [경로 변수 다중](http://localhost:8080/mapping/users/userA/orders/100)
  - [특정 파라미터 조건 매핑](http://localhost:8080/mapping-param?mode=debug)
  - [특정 헤더 조건 매핑(POST MAN 필요)](http://localhost:8080/mapping-header)
  - [미디어 타입 조건 매핑 Content-Type(POST MAN 필요)](http://localhost:8080/mapping-consume)
  - [미디어 타입 조건 매핑 Accept(POST MAN 필요)](http://localhost:8080/mapping-produce)
- 요청 매핑 - API 예시
  - POST MAN 필요
- HTTP 요청 기본
  - [기본, 헤더 조회](http://localhost:8080/headers)
- HTTP 요청 파라미터
  - [요청 파라미터 v1](http://localhost:8080/request-param-v1?username=hello&age=20)
  - [요청 파라미터 v2](http://localhost:8080/request-param-v2?username=hello&age=20)
  - [요청 파라미터 v3](http://localhost:8080/request-param-v3?username=hello&age=20)
  - [요청 파라미터 v4](http://localhost:8080/request-param-v4?username=hello&age=20)
  - [요청 파라미터 필수](http://localhost:8080/request-param-required?username=hello&age=20)
  - [요청 파라미터 기본 값](http://localhost:8080/request-param-default?username=hello&age=20)
  - [요청 파라미터 MAP](http://localhost:8080/request-param-map?username=hello&age=20)
  - [요청 파라미터 @ModelAttribute v1](http://localhost:8080/model-attribute-v1?username=hello&age=20)
  - [요청 파라미터 @ModelAttribute v2](http://localhost:8080/model-attribute-v2?username=hello&age=20)
- HTTP 요청 메시지
  - POST MAN
- HTTP 응답 - 정적 리소스, 뷰 템플릿
  - [정적 리소스](http://localhost:8080/basic/hello-form.html)
  - [뷰 템플릿 v1](http://localhost:8080/response-view-v1)
  - [뷰 템플릿 v2](http://localhost:8080/response-view-v2)
- HTTP 응답 - HTTP API, 메시지 바디에 직접 입력
  - [HTTP API String v1](http://localhost:8080/response-body-string-v1)
  - [HTTP API String v2](http://localhost:8080/response-body-string-v2)
  - [HTTP API String v3](http://localhost:8080/response-body-string-v3)
  - [HTTP API Json v1](http://localhost:8080/response-body-json-v1)
  - [HTTP API Json v2](http://localhost:8080/response-body-json-v2)

<br><br>

# 프로젝트 환경설정 & 생성

**준비물**

* Java 11
* IDE: IntelliJ (이클립스도 가능합니다)

<br>

**스프링 프로젝트 생성**

* [프로젝트 생성하는 곳](https://start.spring.io)
  * Project: Gradle - Groovy Project 
  * Spring Boot: 2.x.x
  * Language: Java
  * **Packaging: Jar**
  * Java: 11
  * Dependencies : Spring Web, Thymeleaf, Lombok
* 참고로  `2.7.1 (SNAPSHOT)` 이런 형태가 아닌 `2.7.0` 처럼 영어가 안 붙은걸 선택 권장
* 이후에 IntelliJ로 폴더 오픈

<br>

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
* **주의!!**
  * 단, Intellij IDEA 로 바꾸고 실행이 안되는 경우가 있는데 그런 경우에는 다시 Gradle로 돌려둘 것


<br>

**단축키 확인 법**

* File -> Settings -> keymap 에서 검색해서 확인

<br><br>

# 초기 세팅, 로깅

**index.html은 `/resources/static/` 위치에 두기 -> Welcome 페이지!**

* Jar 이기에 webapp 경로 사용X 

<br>

**`@Slf4j` 로 간단히 log 사용**

* print 출력보다 훨씬 간편하고, 성능도 좋음

<br><br>

# 요청 매핑

**@GET, @POST... 이런 형식으로 사용을 권장**

* `@RequestMapping` 사용시 모든(GET,POST...) 요청을 허용

* `@RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)` 로 사용시 GET 요청만 허용

<br>

**URL 파라미터, 헤더, 요청 Content(타입), 응답 Content(타입) 를 제약조건 지정할 수 있음**

```java
// 순서대로..
@GetMapping(value = "/mapping-param", params = "mode=debug")
@GetMapping(value = "/mapping-header", headers = "mode=debug")
@PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
@PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
```

<br>

**URL 경로 특정부분 읽어올때는 @PathVariable가 존재**

<br><br>

## HTTP 요청

### 1. 파라미터

파라미터 매핑에는 **`@RequestParam, @ModelAttribute` 를 주로 사용**

**아래는 @Controller 있고, @ResponseBody 는 없어서 View 반환!**

* 단, void의 경우 HTTP 메시지 처리 따로 없으면 그때 요청 URL 참고해서 반환
* 여기선 HTTP 메시지 처리하니까(response) 해당 서블릿이 가진 url로 바로 반환
* **참고로 @ResponseBody 가 있을때는 View 반환이 아님!**

```java
@Slf4j
@Controller
public class RequestParamController {
    /**
     * 1. hello-from.html 에서 넘어온 form 데이터 -> 이 역시 파라미터 형식으로 넘어옴 (body에 담기지만!)
     * 2. 그냥 URL로 파라미터 형식으로 넘어온 데이터
     * 결론은 둘다 getParameter() 가 사용 가능
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        response.getWriter().write("ok");
    }
}
```

<br>

**getParameter 대신 @RequestParam() 사용 가능**

**@RequestParam("age") -> @RequestParam 로 생략 가능**

**@RequestParam 가 생략 -> 단, 오히려 햇갈릴 수도 있어서 본인은 넣는걸 권장**

* 단, 완전 생략때는 required = false 가 기본값 (생략 아닐때는 true)
* defaultValue - 기본값 설정도 가능!
  * 파라미터가 username= 이런식으로 "" 빈값으로 넘어온 경우도 기본값 설정해줌!
* Map, MultiValueMap 형태로 받을 수도 있음!

**@ModelAttribute 사용으로 객체에 자동 할당 가능**

**@ModelAttribute 생략해도 동일한 효과 -> 본인은 이때 생략을 주로 사용**

```java
// 몇가지 예시
public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username) {...}
public String modelAttributeV1(@ModelAttribute HelloData helloData) {...}
public String modelAttributeV2(HelloData helloData) {...}
```

<br>

### 2. body

message body 에는 **`InputStream` 으로 직접 읽을 수 있고 `HttpEntity, @RequestBody` 를 주로 사용**

**@RequestBody로 body 읽고 ObjectMapper 로 Json을 객체로 변환법**

```java
@ResponseBody
@PostMapping("/request-body-json-v2")
public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
    return "ok";
}
```

<br>

**@RequestBody로 body 읽는데 객체로도 한번에 변환법 (생략불가)**

```java
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
    log.info("username={}, age={}", data.getUsername(), data.getAge());
    return data; // 반환은 @ResponseBody 덕분에 바로 객체로 반환 가능
} // 애초에 @RequestBody 가 해당 기능을 제공
```

<br><br>

## HTTP 응답

**3가지로 구분**

* 정적 리소스
* 뷰 템플릿(동적)
* API

<br>

**JSON 하나만 소개**

```java
// @RestController 선언 가정 -> @ResponseBody 를 가짐
@ResponseStatus(HttpStatus.OK)
@GetMapping("/response-body-json-v2")
public HelloData responseBodyJsonV2() {
    HelloData helloData = new HelloData();
    helloData.setUsername("userA");
    helloData.setAge(20);
    return helloData; // @ResponseBody 때문에 객체로 반환 가능
}
```

<br><br>

# 보충 개념

## HTTP 메시지 컨버터

**HTTP 메시지 컨버터가 언제 사용되는지 보자**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/dd2c0458-0f0f-4ef7-be3c-d52f56aed5bf) 

<br>

**스프링 MVC는 다음의 요청, 응답 경우에 HTTP 메시지컨버터를 적용한다.**

* HTTP 요청: @RequestBody , HttpEntity(RequestEntity)
* HTTP 응답: @ResponseBody , HttpEntity(ResponseEntity)

<br>

**HTTP 메시지컨버터 동작 함수**

* canRead() , canWrite() : 메시지 컨버터가 해당 클래스, 미디어타입을 지원하는지 체크 

* read() , write() : 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능

<br>

**우선순위**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/e845bf62-0475-4dbc-a702-971a15b021c8) 

<br>

**예를 들어 아래의 ? 부분을 해석해보자.**

* 먼저 클래스 타입을 확인하는데 Byte->String->HelloData 순으로 확인되고 현재까지는 MappingJac....컨버터이다.
* 이후 미디어 타입을 확인하는데 content타입이 json이 아니고 text/html 이기 때문에 MappingJac...컨버터가 될 수 없다.
* 탈락. 동작 안되는 CASE

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/31dd3228-91ad-4351-b3c8-be049f760253) 

<br><br>

## 요청 매핑 헨들러 어뎁터 구조

**HTTP 메시지 컨버터는 어디서 사용하는지 추적해보자**

**기존 스프링 MVC에서 Front Controller 부분인 Dispatcher Servlet 은 핸들러 어댑터로 동작하는 과정이 있는데, 이때 `RequestMappingHandlerAdapter` 어댑터를 한번 살펴보자.**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/183afe1d-6219-49a0-9243-47acd3725b7d) 

<br>

**`ArgumentResolver` 가 이때 등장하며 이녀석은 `@RequestParam, @ModelAttribute` 등등 다양한 파라미터, 애노테이션을 기반으로 데이터 생성하게끔 30개 이상이 구현되어 있다.**

* 예로 @RequestParam 을 쓰게되면 @RequestParam 전용으로 개발된 ArguemntResolver 가 해당 파라미터를 매핑해와서 값을 주는 것이다.
* `ArgumentResolver` 관련 구현된 인터페이스를 보면 `supportsParameter() 와 resolveArgument()` 로 구성되어 있다.

<br>

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/1acf49cb-d716-4803-bf84-8cdc4aaaa687) 

**다양한 ArgumentResolver 중에서 `@RequestBody, HttpEntity` 같은 것들은 HTTP 메시지 컨버터까지 사용을 해야 데이터를 처리할 수 있어서 이녀석들은 위와 같은 구조로 동작을 한다.**

<br>

**만약 기능 확장의 경우는 `WebMvcConfigurer` 를 상속받아서 스프링 빈으로 등록하면 된다.**

* @Bean으로 등록하거나 @Configuration 으로 설정파일임을 알리면서 빈으로 등록해도 된다.

* 예로 로그인 관련 ArgumentResolver 확장과 인증관련 인터셉터 확장은 아래처럼 진행한다.

  ```java
  @Configuration // 설정 파일임을 알림
  @Slf4j
  public class ApiConfig implements WebMvcConfigurer {
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
          resolvers.add(new LoginMemberArgumentResolver());
      }
  
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new MemberCheckInterceptor())
                  .order(2)
                  .addPathPatterns("/**") // 모든 경로 접근
                  .excludePathPatterns("/", "/api/v1/members/login", "/api/v1/members/register",
                          "/api/v1/members/logout","/css/**","/*.ico","/error"); // 제외 경로!
  
      }
  }
  ```

* `LoginMemberArgumentResolver(), MemberCheckInterceptor()` 이 두가지를 새로 만들어서 등록함으로써 기능 확장을 진행한 것이다.

* 이 두내용은 참고용으로 코드 남겨두겠다.

  ```java
  /**
  * LoginMemberArgumentResolver()
  */
  @Slf4j
  public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
  
      @Override
      public boolean supportsParameter(MethodParameter parameter) {
          log.info("supportsParameter 실행");
  
          boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
          boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
          // 어노테이션이 @Login 이고, 해당 파라미터 타입이 Long 라면 true 반환
          return hasLoginAnnotation && hasLongType;
      }
  
      // 위 supportsParameter 가 true 면 아래 함수가 실행
      @Override
      public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
          log.info("resolveArgument 실행");
  
          HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
          HttpSession session = request.getSession(false); // false : 없으면 null
          if(session == null) {
              return null;
          }
  
          Long memberId = Long.valueOf(session.getAttribute("login_member").toString());
          return memberId;
      }
  }
  ```

  ```java
  /**
  * MemberCheckInterceptor()
  */
  @Slf4j // log
  public class MemberCheckInterceptor implements HandlerInterceptor {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
  
          String requestURI = request.getRequestURI();
          log.info("인증 체크 인터셉터 실행 {}", requestURI);
  
          HttpSession session = request.getSession();
          if(session == null || session.getAttribute("login_member")==null) {
              log.info("미인증 사용자 요청");
              // 회원 아님을 알림
              response.setStatus(HttpStatus.UNAUTHORIZED.value()); // status code : 401
              return false;
          }
          return true;
      }
  }
  ```

<br><br>

# Folder Structure

생략..
