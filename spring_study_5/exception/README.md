# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **예외 처리 - 오류 페이지, API 예외 처리** - exception

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
  * Dependencies : Spring Web, Thymeleaf, Lombok, Validation
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

# 1. 서블릿 예외 처리

**서블릿은 2가지 방식으로 예외 처리 지원**

* Exception (예외)
* response.sendError(HTTP 상태 코드, 오류 메시지)

<br>

**"스프링 부트 기본 제공 예외처리 페이지 주석" 하고 시작하자(이건 마지막에 사용할것이기 때문)**

`server.error.whitelabel.enabled=false`

<br>

### 1. 컨트롤러 에러 발생시켜보기 (Exception, response.sendError)

* Exception : `WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)` 

* response.sendError : `WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러 (response.sendError())`
  * 톰캣(WAS)이 제공하는 HTTP 상태코드 500 에러화면 반환
  * (참고) 아예 없는 주소를 접근하면 - 404 에러화면 보여줌

```java
@Slf4j
@Controller
public class ServletExController {
	// Exception 
    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생!");
    }
	// response.sendError
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }
    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
```

<br><br>

### 2. 서블릿오류 페이지 등록해보기

**`WebServerFactoryCustomizer<ConfigurableWebServerFactory>` 인터페이스를 구현체 구현해서 HTTP 오류때 에러페이지 지정이 가능**

* 위에서 했던 에러 3개를 "직접 만든 에러페이지 등록 해보기"
* 테스트는 앞에서 만든 ServletExController 에 URL로 접근해보면 됨!

```java
@Component // Bean에 등록
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
```

<br>

**에러 페이지 보여주기 위해**

```java
@Slf4j
@Controller
public class ErrorPageController {

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        return "error-page/404"; // 404.html
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        return "error-page/500"; // 500.html
    }
}
```

<br><br>

### 3. 필터, 인터셉터

**예외 발생과 오류 페이지 요청 흐름**

* `WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)`
2. `WAS /error-page/500 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-
page/500) -> View`
* "필터" 나 "인터셉터" 에서 로그인 인증 로직같은게 있으면 또 실행되니 비효율
  * 이를 위해 클라요청, 서버내부 요청인지 판단하는 `DispatcherType` 가 제공

<br>

**"필터, 인터셉터" 는 구현했다고 가정후 WebConfig 작성**

* **"필터" 는 Dispatcher 관련 메소드를 제공**

* **"인터셉터" 는 없으므로 url제외 메소드를 활용해서 해결**

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**"); //오류 페이지 경로 지정해서 "인터셉터" 사용X 설정
    }

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 요청, 에러때 둘다 "필터 사용"
        return filterRegistrationBean;
    }
}
```

<br>

**/error-ex 오류 요청 했을때 흐름**

1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
3. WAS 오류 페이지 확인
4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) ->
컨트롤러(/error-page/500) -> View

<br><br>

# 2. (핵심!) 스프링부트 - 오류 페이지

**오류페이지를 띄우기 위해 `WebServerFactoryCustomizer<ConfigurableWebServerFactory>` 인터페이스 구현체를 구현해서 HTTP 상태가 404, 500 등등 일때 원하는 "에러페이지 URL" 로 forward 했었다.**

**이때 URL에 맞게 html 매핑까지 하기위해 따로 컨트롤러도 만들었었다.**

* 이런 복잡한 수고스러움을 해결하기 위해 **"스프링부트는 기본제공"** 한다.
* 따라서 제공하는 규칙에 맞게 사용만 하면 된다.

<br>

**먼저, `WebServerFactoryCustomizer<ConfigurableWebServerFactory>` 구현했던걸 주석처리하면 바로 "스피링부트가 제공"한다.**

* `/error` 경로의 html을 접근

<br>

**뷰선택 우선순위(BasicErrorController 가 제공하는 기능)**

* **1. 뷰템플릿**
  * resources/templates/error/500.html
  * html resources/templates/error/5xx.html
* **2. 정적리소스( static , public ) resources/**
  * static/error/400.html
  * resources/static/error/404.html 
  * resources/static/error/4xx.html
* **3. 적용대상이없을 때뷰이름( error )**
  * resources/templates/error.html

**테스트**

* http://localhost:8080/error-404      404.html
* http://localhost:8080/error-400 	4xx.html  (400 오류 페이지가없지만 4xx가 있음)
* http://localhost:8080/error-500      500.html
* http://localhost:8080/error-ex      500.html  (예외는 500으로처리)

<br><br>

# 3. API 예외 처리

**API는 앞에서 공부한 "오류페이지" 보다 복잡한 이유가 "서버, 클라간의 오류 응답 스펙" 을 정해야하고 이에 맞춰서 JSON으로 데이터를 내려주어야 하기 때문이다.**

**@ExceptionHandler 를 사용하는게 제일 좋지만, 이를 이해하기 위해 단계별로 살펴보자**

<br>

## 3-1. 스프링 부트 기본 방식

**API 도 "html" 처럼 기본적인 에러 페이지 처리 방식을 제공한다.**

**테스트를 위해 ApiExceptionController.java**

```java
@GetMapping("/api/members/{id}")
public MemberDto getMember(@PathVariable("id") String id) {

    if (id.equals("ex")) { // "ex" 들어오면 Runtime에러 띄우기
        throw new RuntimeException("잘못된 사용자");
    }
}
```

<br>

**"스프링부트 기본제공" 이 아닌 "직접" 해보기 위해 주석해제**

```java
@Component // => "스프링 부트" 기본제공 오류 페이지 사용하려면 주석
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> { ... }
```

<br>

**"기본제공" 을 안쓰기 때문에 에러페이지 매핑을 API용으로 추가**

* 핵심 : `produces = MediaType.APPLICATION_JSON_VALUE`
* 이 덕분에 JSON으로(API) 요청한 경우 해당 컨트롤러로 해결

```java
// (참고) API 예외처리 부분
@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, Object>> errorPage500Api(
    HttpServletRequest request, HttpServletResponse response) {

    log.info("API errorPage 500");

    Map<String, Object> result = new HashMap<>();
    Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
    result.put("status", request.getAttribute(ERROR_STATUS_CODE));
    result.put("message", ex.getMessage());

    Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
}
```

<br>

**이 매커니즘을 "스프링부트가 기본제공" 하므로 위 내용은 잊고 바로 사용해도 됨**

* 요청할때 application/json 타입으로만 요청하면 알아서 BasicErrorController 가 API용으로 매핑해서 반환해주기 때문

<br><br>

## 3-2. HandlerExceptionResolver 사용

**"어디서, 어떻게 동작할까??"**

* 빈 ModelAndView : new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지 않고, 정상흐름 으로 서블릿이 리턴된다.
* ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
* null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿밖으로 던진다.

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/b88a0660-3489-4d45-9c71-f7716000f6e2) 

<br>

**`HandlerExceptionResolver` 인터페이스 구현체를 만들어서 `resolveException` 메소드를 오버라이드 하면 처리 할 수 있다.**

**물론, 이것도 WebConfig 에 `extendHandlerExceptionResolvers` 메소드를 오버라이드 해서 등록해줘야 적용된다.**

**`/api/members/bad` 의 경우 500 에러가 발생하는데 400으로 수정해서 반환한 예시**

```java
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call resolver", ex);

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); // 400 으로 수정
                return new ModelAndView();
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
```

```java
// 컨트롤러 내용
if (id.equals("bad")) {
    throw new IllegalArgumentException("잘못된 입력 값");
}
```

<br>

**실행모습**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/8b982001-1a95-4243-81d9-c5c091780799) 

<br>

**지금 현재 예외처리들은 전부 "WAS" 로 갔다가 다시 WAS에서 오류 페이지 정보를 찾아서 호출하는 과정은 생각해보면 매우 복잡**

* 이를 아까 개발했던 HandlerExceptionResolver를 잘활용하면 한번에 끝낼수 있음

**예외 커스텀**

```java
/**
 * RuntimeException 선언후 오버라이드로 간단 구현
 */
public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

<br>

**컨트롤러에 추가**

```java
if (id.equals("user-ex")) {
    throw new UserException("사용자 오류");
}
```

<br>

**리졸버 추가 개발**

* 핵심 : **직접 json을 만들어서 반환(커스텀을 해본것!)** 및 **text/html 의 경우 바로 뷰를 지정**해주는 중!
  * 앞에서 설명했듯이 ModelAndView 빈값 반환하면 정상적으로 반환하고
  * 뷰정보 넣어서 반환하면 뷰템플릿 찾아 반환했다고 한것을 이용

```java
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {

            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());
                    String result = objectMapper.writeValueAsString(errorResult);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    return new ModelAndView();
                } else {
                    // TEXT/HTML
                    return new ModelAndView("error/500");
                }
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
```

<br>

**추가등록 - webConfig**

```java
@Override
public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    resolvers.add(new MyHandlerExceptionResolver());
    resolvers.add(new UserHandlerExceptionResolver());
}
```

<br>

**실행화면 - API**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/2cb069d9-9efd-4b06-9fbf-6e84885f085e) 

<br><br>

## 3-3. 스프링 제공 - ExceptionResolver

스프링부트가 기본으로제공하는 ExceptionResolver 는 다음과같다.   
HandlerExceptionResolverComposite 에다음순서로 등록

1.   ExceptionHandlerExceptionResolver
     * `@ExceptionHandler` 를 처리!! **대부분 API 예외 처리**는 이 기능 활용
2.   ResponseStatusExceptionResolver
     * **HTTP 상태코드 간편지정** - `@ResponseStatus(value = HttpStatus.NOT_FOUND)`
3.   DefaultHandlerExceptionResolver   우선순위가 가장낮다.
     * 스프링 내부 기본 예외를 처리

<br>

### ResponseStatusExceptionResolver 사용법

**매우간단**

```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") 
	public class BadRequestException extends RuntimeException {
}
```

<br>

**컨트롤러**

```java
// 사용법 1
@GetMapping("/api/response-status-ex1") 
public String responseStatusEx1() {
	throw new BadRequestException(); 
}
// 사용법 2
@GetMapping("/api/response-status-ex2") 
public String responseStatusEx2() {
	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new 
	IllegalArgumentException());
}
```

<br>

### DefaultHandlerExceptionResolver 사용법

**`TypeMismatchException` 같은 스프링 내부에서 에러터지는 타입에러 같은 경우들은 500 에러가 뜨지만, 실제로 "클라이언트" 쪽에서 잘 못 입력한 경우가 대부분이라 400 (클라는 4xx, 서버는 5xx) 상태코드로 바꾸는것을 권장 및 다양한 예외를 제공**

<br>

**컨트롤러 Integer**

```java
@GetMapping("/api/default-handler-ex")
public String defaultException(@RequestParam Integer data) {
    return "ok";
}
```

<br>

**실행결과 - http://localhost:8080/api/default-handler-ex?data=aa**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/d2852abf-a752-40ab-811b-f25e7a31bd1f) 

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/acce437c-07bf-4138-b2dc-f59f271417b5)

<br><br>

## 4. (중요!) @ExceptionHandle + @ControllerAdvice

**HTML 화면 오류 vs API 오류**

* 웹 브라우저에 HTML 화면을 제공 할 때는 오류가 발생하면 BasicErrorController 를 사용하는게 편하다.
* 이때는 단순히 5xx, 4xx 관련된 오류화면을 보여주면 된다. BasicErrorController 는 이런메커니즘을 모두구현 해두었다.
* 단, API 는 예외에 따라 다양한 데이터를 출력해야하므로 각각 모양이 너무 다르기 때문에 HandlerExceptionResolver 를 직접 구현했었다.
* 근데, 이것도 ModelAndView 를 반환하면서 너무 불편했다.
* 해결방안은?? **@ExceptionHandler**

<br>

**@ExceptionHandler - ExceptionHandlerExceptionResolver 사용**

```java
@Slf4j
@RestController
public class ApiExceptionV2Controller {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 응답코드 지정 위해
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) { 
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage()); 
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) { 
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST); 
        // ResponseEntity로 반환해서 응답코드 지정도 당연히 가능
    }
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) { 
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류"); 
    }
    // 위까지 추가내용
    // 기존내용
    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자"); 
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값"); 
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류"); 
        }
        return new MemberDto(id, "hello " + id);
    }
    
    @Data
    @AllArgsConstructor
    static class MemberDto { 
        private String memberId; 
        private String name;
    }
}
```

<br>

**API 스펙..**

```java
/**
 * API 예외처리 응답 스펙
 */
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
```

<br>

**실행결과 - /api2/members/bad, /api2/members/ex**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/471c9a95-03a0-4cb6-99ac-7c15d8e1b642)

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/ff702130-4d3f-4907-af06-c8192f6d9671) 

<br>

**실행흐름**

* 컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러밖으로 던져진다. 
* 예외가발생했으로 ExceptionResolver 가 작동한다. **가장 우선순위가높은  ExceptionHandlerExceptionResolver 가 실행된다.**
* ExceptionHandlerExceptionResolver 는해당컨트롤러에 IllegalArgumentException 을처리할 
  수있는 @ExceptionHandler 가 있는지 확인한다.
* illegalExHandle() 를실행한다. @RestController 이므로 illegalExHandle() 에도 @ResponseBody 가적용된다. 따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로반환된다. 
* @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태코드 400으로 응답한다.

<br>

**@ControllerAdvice - 로직분류 목적**

* @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌적용)
* @RestControllerAdvice 는 @ControllerAdvice 와같고, @ResponseBody 가 추가되어 있다. 
* @Controller , @RestController 의차이와같다.

<br>

**대상 컨트롤러 지정 방법**

```java
// Target all Controllers annotated with @RestController 
@ControllerAdvice(annotations = RestController.class) 
public class ExampleAdvice1 {}

// Target all Controllers within specific packages 
@ControllerAdvice("org.example.controllers") 
public class ExampleAdvice2 {}

// Target all Controllers assignable to specific classes
@ControllerAdvice(assignableTypes = {ControllerInterface.class, 
AbstractController.class})
public class ExampleAdvice3 {}
```

<br>

**예시**

```java
@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
```

<br><br>

# 결론??

**웹 에러 처리의 경우에는 기존 "스프링 부트 기본제공" (BasicErrorController) 을 규칙에 맞게끔 사용하자**

**API 에러 처리의 경우에는 @ExceptionHandler 와 @ControllerAdvice 를 조합해서 사용하자**

* API 에러 처리에서 response.sendError(xxx) 는 서블릿에서 상태코드에 따른 오류를 처리하도록 위임을 하다보니 WAS로 갔다가 다시 /error 부분을 호출하는 비효율적!
* 물론, response.getWriter().println("hello"); 같이 직접 HTTP 응답 바디에 넣어주면 바로 JSON응답은 가능하지만 코드작성하기 매우 복잡
* 하지만 @ExceptionHandler 는 한방에 이 모든걸 해결!
  * 컨트롤러에 예외가 그냥 @ExceptionHandler 덕분에 "정상처리" 된다고 보면된다.
  * 실제로 상태코드도 200을 반환 -> 따라서 상태코드 변경 "필수"
* 마지막으로 이 에러처리 내용들은 @ControllerAdvice 로 로직 분류

<br><br>

# Folder Structure

생략..

