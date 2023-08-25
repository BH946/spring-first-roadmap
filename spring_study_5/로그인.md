# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **로그인 처리 관련 프로젝트** - login

<br><br>

#  프로젝트 환경설정 & 생성

**이전에 한 프로젝트에서 "로그인" 기능을 추가할 예정**

<br><br>

# 로그인 처리1 - 쿠키, 세션

**로그인의 `도메인, 서비스, 컨트롤러 기능`들을 개발하는것은 중요하지 않고,**

**`쿠키, 세션` 그리고 뒤에서 더 배울 `필터, 인터셉터` 가 중요한 개념이므로 이부분만 정리하겠다.**

<br><br>

## 요구사항

**로그인 요구사항**

* 홈 화면 - 로그인전
  * 회원가입
  * 로그인
* 홈 화면 - 로그인후
  * 본인이름(누구님환영합니다.) 
  * 상품관리
  * 로그아웃 
* 보안 요구사항
  * 로그인 사용자만 상품에 접근하고, 관리할 수 있음
  * 로그인 하지 않은 사용자가 상품관리에 접근하면 로그인 화면으로 이동 
* 회원가입, 상품 관리

<br><br>

## 로그인 쿠키, 세션

### 1. 쿠키

**"쿠키" 를 이용해서 로그인을 "유지" 해보자!**

**먼저, 쿠키는 "모든 요청에 포함" 되는 저장소로 볼 수 있다.**

* 영속 쿠키 : 만료 날짜 **입력시** 해당 날짜까지 유지
* 세션 쿠키 : 만료 날짜 **생략시** 브라우저 종료까지 유지

<br>

**쿠키생성**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/f9bc7ae6-b481-4f99-9439-9c7705d0be22) 

<br>

**클라이언트 쿠키 전달 및 서버 응답**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/d4c4ae02-facf-473d-aa31-95db09138ba0) 

<br>

**"세션 쿠키" 를 만드는 예시**

```java
//쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료) 
Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId())); 
response.addCookie(idCookie); // HttpServletResponse 에 담은것
```

<br>

**"쿠키 간단 조회" 예시**

```java
@GetMapping("/") 
public String homeLogin( @CookieValue(name = "memberId", required = false) Long memberId, Model model) 
{ ... }
```

<br>

**"세션 쿠키의 로그아웃" 예시**

```java
// 로그아웃 방법 2가지
// 1. "세션 쿠키" 는 브라우저 종료시에도 쿠키 사라지니까 "로그아웃"
// 2. 또는 아래처럼 "memberId" 쿠키를 종료날짜 0으로 지정하면 "로그아웃"
@PostMapping("/logout")
public String logout(HttpServletResponse response) { 
	expireCookie(response, "memberId");
	return "redirect:/"; 
}
private void expireCookie(HttpServletResponse response, String cookieName) { 
    Cookie cookie = new Cookie(cookieName, null);
    cookie.setMaxAge(0);
    response.addCookie(cookie); 
}
```

<br>

**하지만 "쿠키" 만을 사용한 방식에는 심각한 보안 문제(예로 memberId 정보)들이 존재하므로 "세션" 개념을 도입**

<br><br>

### 2. 세션 - 서블릿 HTTP 세션

**세션 : 서버에 중요한 정보를 보관하고 연결을 유지하는 방법**

**서블릿 HTTP 세션 : "서블릿이 공식 지원" 해주는 세션이며, 사용법만 익히면 바로 사용가능!**

**"세션" 을 도입해도 "쿠키" 는 당연히 사용하며 기존의 memberId 같은 중요정보를 쿠키에 담는게 아닌 임의로 생성한 UUID(토큰.랜덤값) 같은 중요정보가 없는 데이터를 담는다.**

* "쿠키"에 중요정보가 없기때문에 탈취당해서 해석해도 안전
* 만약 UUID같은 토큰을 탈취해서 이것으로 서버 "세션" 에 접근하려고 하더라도 "토큰 만료시간을 짧게" 하면 안전 or 해킹 의심의 경우 서버에서 강제로 "토큰 제거"

<br>

**세션 및 쿠키 생성**

* `HttpSession session = request.getSession();` 과정을 통해서
  * sessionId 를 UUID 형식으로 생성하고, 쿠키에는 임의로 설정한 이름으로 sessionId 기록
* `session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);` 과정을 통해서
  * "세션 메모리" 즉, 서버단에 메모리에 임의로 설정한 이름을 키로 loginMember값 기록

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/706c9e31-d211-46db-97be-c50775a5b56b) 

<br>

**클라이언트 쿠키 전달 및 서버 응답**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/62a0b7df-bb19-4e26-b321-d895cd5a8d5d) 

<br>

**상수 - 쿠키 조회에 해당 이름만 사용할거라 "상수" 로 등록**

```java
public class SessionConst {
	public static final String LOGIN_MEMBER = "loginMember"; 
}
```

<br>

**로그인 컨트롤러에 "로그인 성공 로직"**

```java
//(true) 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성 
HttpSession session = request.getSession();
//세션에 로그인 회원 정보 보관
session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
```

<br>

**세션 삭제하는 "로그아웃 로직"**

```java
//(false) 세션이 있으면 있는 세션 반환, 없으면 null 반환
HttpSession session = request.getSession(false);
if (session != null) { 
        session.invalidate(); // 세션 제거
}
```

<br>

**간단히 @GET 에 사용할 "로그인 확인(세션확인)로직"**

```java
@GetMapping("/")
public String homeLoginV3Spring( @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    //세션에 회원 데이터가 없으면 home 
    if (loginMember == null) {
    	return "home"; 
	}
	//세션이 유지되면 로그인으로 이동
    model.addAttribute("member", loginMember);
	return "loginHome"; 
}
```

<br>

**(참고) 브라우저 쿠키 미지원시 URL 쿠키 전달하는데 이를 끄는 옵션**

```properties
server.servlet.session.tracking-modes=cookie
```

<br><br>

### 3. 세션 정보와 타임아웃 설정

**기존 "쿠키"는 브라우저 종료시 같이 종료되므로 보안에 문제가 없지만, "서버의 세션" 의 경우 브라우저 종료를 알 수 없으므로 같이 제거를 할 수 없는 보안문제가 아직 존재한다.**

* HTTP는 비연결성 이므로 브라우저의 종료 여부를 알 수 없는것
  * 물론, 브라우저나 앱이 종료할때 통신을 보내서 종료 여부를 알게 하는 방식도 분명 존재
* 따라서 `HttpSession` 은 기본값 30분으로 "세션 만료 시간" 을 정해둔다.
  * `HttpSession`의 큰 장점은 **시간 기준이 사용자가 "서버 요청"을 했을때 기준**이라는 점이다.
  * 따라서 사용자가 사용할때는 거의 로그인이 풀릴일이 없는것!!
* **문법 참고**
  * `session.getLastAccessedTime()` : 최근 세션 접근시간
  * LastAccessedTime 이후로 timeout 시간이 지나면, WAS가 내부에서 해당세션을 제거

<br>

**application.properties** (글로벌 설정은 분 단위로 설정.  60(1분), 120(2분), ...)

```properties
// 60초, 기본은 1800(30분) 
server.servlet.session.timeout=60
```

<br>

특정 세션단위로 시간설정

```java
session.setMaxInactiveInterval(1800); //1800초
```

<br><br>

### (추가) 이해를 돕기위해 "세션" 처리과정

```java
/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {

        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }


    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

}
```

<br><br>

# 로그인 처리2 - 필터, 인터셉터

**공통 관심 사항 : 애플리케이션 여러 로직에서 공통으로 관심이 있는 것**

* **보통 스프링 AOP로 해결이 제일 베스트**
* **그러나 웹의 경우 HTTP, URL 정보 등을 제공하는 "서블릿 필터, 스프링 인터셉터" 를 권장**
* **아래에서 부터는 "서블릿, 인터셉터" 로 `로그 -> 로그인 인증` 을 구현해보겠다.**

<br>

## 1. 서블릿 필터

**필터를 적용하면 필터가 호출 된 다음에 서블릿이 호출!!**

**필터 흐름**

* `HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러`
* `HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러`

* 참고로 "스프링" 의 경우 위 서블릿은 "디스패처 서블릿" 을 의미

<br>

**필터 인터페이스**

```java
//init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
//doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다. 
//destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.

public interface Filter {
    public default void init(FilterConfig filterConfig) throws ServletException 
    {}
    public void doFilter(ServletRequest request, ServletResponse response, 
                FilterChain chain) throws IOException, ServletException;
    public default void destroy() {} 
}
```

<br><br>

### 1-1. 로그 체크

**먼저 필터를 구현(Filter 구현체)**

* `ServletRequest` 는 HTTP 요청이 아닌 경우도 고려해서 만든 인터페이스라서 한마디로 "부모"
  * 따라서 `HttpServletRequest` 로 다운 캐스팅이 가능
* `chain.doFilter()` 는 다음 필터가 있으면 호출해주는 기능

```java
@Slf4j
public class LogFilter implements Filter {
    // init()
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { 
        log.info("log filter init");
    }
    // doFilter()
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
    FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request; 
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST  [{}][{}]", uuid, requestURI); 
            chain.doFilter(request, response); // 다음 필터 실행(있을시)
    } catch (Exception e) {
    throw e; 
    } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
    }
    }
    // destroy()
    @Override
    public void destroy() {
        log.info("log filter destroy");
    } 
}
```

<br>

**필터를 등록**

* `FilterRegistrationBean` 에 등록하는 방법 권장

```java
@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 필터 체인 순서
        filterRegistrationBean.addUrlPatterns("/*");
	    return filterRegistrationBean; 
    }
}
```

<br><br>

### 1-2. 인증 체크

**먼저 필터를 구현(Filter 구현체)**

* 이번엔 로그인 인증 체크 필터
* 화이트리스트에 "회원가입, 로그인, 로그아웃, css" 는 당연히 인증할 필요 없으니 제외한 것

```java
@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request; 
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            if (isLoginCheckPath(requestURI)) { // 화이트리스트 체크
                log.info("인증 체크 로직 실행 {}", requestURI); 
                HttpSession session = httpRequest.getSession(false);
                if (session == null ||
                    session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { 
                    log.info("미인증 사용자 요청 {}", requestURI);
                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + 
                                              requestURI);
                    return; //여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝! 
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함 
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        } 
    }
    /**
     * 화이트 리스트의 경우 인증 체크X 
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI); 
    }
}
```

<br>

**필터를 등록**

```java
// WebConfig
@Bean
public FilterRegistrationBean loginCheckFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new 
FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new LoginCheckFilter());
    filterRegistrationBean.setOrder(2); // 순서 2번째
    filterRegistrationBean.addUrlPatterns("/*"); 
    return filterRegistrationBean;
}
```

<br><br>

## 2. 스프링 인터셉터

**스프링 MVC가 제공하는 기능!**

**스프링 인터셉터 흐름**

* `HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러`
* `HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터1 -> 인터셉터2 -> 컨트롤러`
* 스프링 인터셉터는 서블릿 필터보다 편리하고, 더 정교하고 다양한 기능을 지원

<br>

**스프링 인터셉터 인터페이스**

```java
// preHandle 핸들러 어댑터 호출전에 호출, 응답값이 true 이면 다음으로 진행하고, false 이면 더는 진행하지 않는다
// postHandle 핸들러 어댑터 호출후에 호출
// afterCompletion 뷰가 렌더링된 이후에 호출

public interface HandlerInterceptor {
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {}
    
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {}

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {}
}
```

<br>

**스프링 인터셉터 호출 흐름**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/4e31e4a4-b569-432f-8154-238713c6dcc4) 

<br>

**스프링 인터셉터 예외 상황**

* 예외가 발생해도 `afterCompletion()` 은 호출되므로 이곳에 "공통 처리" 가능

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/48799d88-a6e1-4fc0-8b2e-3c7487739375) 

<br><br>

### 2-1. 로그 체크

```java
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        //@RequestMapping정보를 HandlerMethod가 정보가져서 넘어옴
        //정적 리소스 정보는 ResourceHttpRequestHandler가 정보가져서 넘어옴
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // false 진행X
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }

    }
}
```

<br>

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) { 
        registry.addInterceptor(new LogInterceptor())
            .order(1) // 인터셉터 순번
            .addPathPatterns("/**") // 적용O
            .excludePathPatterns("/css/**", "/*.ico", "/error"); // 적용X
    }
    //...
}
```

<br><br>

### 2-2. 인증 체크

**로그인 인증의 경우 `preHandle` 만 사용하면 충분 - 인증 실패시 "로그인창으로 이동"**

```java
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(); // false 하는게 더 좋을듯

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
```

<br>

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) { 
        registry.addInterceptor(new LogInterceptor()) // 로그 인터셉터
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico", "/error");
        registry.addInterceptor(new LoginCheckInterceptor()) // 로그인 인터셉터
            .order(2)
            .addPathPatterns("/**") 
            .excludePathPatterns(
            "/", "/members/add", "/login", "/logout", 
            "/css/**", "/*.ico", "/error"
        );
    }
    //...
}
```

<br><br>

## 3. ArgumentResolver 활용 (로그인 멤버 조회)

**@Login 같은 애노테이션을 ArgumentResolver를 활용해서 직접 만들어서 사용**

* 로그인 인증은 "인터셉터"로 통과를 했어도 해당 정보로 "멤버정보" 를 가져오는 코드도 매우 반복적인 중복 코드이다.
* 하지만 이를 @Login 하나로 해결!!
* ArgumentResolver는 이전에 공부한 내용이라 설명 생략

<br>

**@Login 애노테이션 위해 인터페이스 생성**

```java
@Target(ElementType.PARAMETER) // 파라미터에서만 사용
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 정보 남기기
public @interface Login {
}
```

<br>

**LoginMemberArgumentResolver 생성**

* `supportsParameter()` : @Login 애노테이션 + Member 타입 이라면 resolveArgument 실행
* `resolveArgument()` : 컨트롤러 호출직전에 호출되어서 필요한 파라미터 정보를 생성
  * 여기서는 세션에있는 로그인 회원정보인 member 객체를 찾아서 반환

```java
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
```

<br>

**WebMvcConfigurer에 추가**

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> 
                                     resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
    //...
}
```

<br><br>

# 결론..

URL 접근시 로그인 인증은 필터말고 **"인터셉터" 로 구현**을 권장

마지막 **ArgumentResolver 를 활용해서 간편히 @Login**으로 컨트롤러에서 Member 객체 가져다 쓰길 권장

* WebMvcConfigurer 추가는 반드시 필수 - "인터셉터, ArgumentResolver"

<br><br>

# Folder Structure

생략..
