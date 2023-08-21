# Intro..

**스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **프로젝트 총 3가지** -> 현재 포스팅은 servlet 프로젝트
  * **servlet : 서블릿, JSP, MVC 패턴, MVC 프레임워크, 스프링 MVC 이해**
  * springmvc : 스프링 mvc 기능 알아보기
  * item-service : 스프링 mvc 로 프로젝트 해보기(웹페이지 만들기)


<br>

**아래 순서로 구현**

- [서블릿 basic](http://localhost:8080/basic.html)
- 서블릿
  - [회원가입](http://localhost:8080/servlet/members/new-form)
  - [회원목록](http://localhost:8080/servlet/members)
- JSP
  - [회원가입](http://localhost:8080/jsp/members/new-form.jsp)
  - [회원목록](http://localhost:8080/jsp/members.jsp)
- 서블릿 MVC
  - [회원가입](http://localhost:8080/servlet-mvc/members/new-form)
  - [회원목록](http://localhost:8080/servlet-mvc/members)
- FrontController - v1
  - [회원가입](http://localhost:8080/front-controller/v1/members/new-form)
  - [회원목록](http://localhost:8080/front-controller/v1/members)
- FrontController - v2
  - [회원가입](http://localhost:8080/front-controller/v2/members/new-form)
  - [회원목록](http://localhost:8080/front-controller/v2/members)
- FrontController - v3
  - [회원가입](http://localhost:8080/front-controller/v3/members/new-form)
  - [회원목록](http://localhost:8080/front-controller/v3/members)
- FrontController - v4
  - [회원가입](http://localhost:8080/front-controller/v4/members/new-form)
  - [회원목록](http://localhost:8080/front-controller/v4/members)
- FrontController - v5 - v3
  - [회원가입](http://localhost:8080/front-controller/v5/v3/members/new-form)
  - [회원목록](http://localhost:8080/front-controller/v5/v3/members)
- FrontController - v5 - v4
  - [회원가입](http://localhost:8080/front-controller/v5/v4/members/new-form)
  - [회원목록](http://localhost:8080/front-controller/v5/v4/members)
- SpringMVC - v1
  - [회원가입](http://localhost:8080/springmvc/v1/members/new-form)
  - [회원목록](http://localhost:8080/springmvc/v1/members)
- SpringMVC - v2
  - [회원가입](http://localhost:8080/springmvc/v2/members/new-form)
  - [회원목록](http://localhost:8080/springmvc/v2/members)
- SpringMVC - v3
  - [회원가입](http://localhost:8080/springmvc/v3/members/new-form)
  - [회원목록](http://localhost:8080/springmvc/v3/members)

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
  * **Packaging: War (JSP 사용위해 반드시!)**
  * Java: 11
  * Dependencies : Spring Web, Lombok
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

# 서블릿

**`서블릿` 은 WAS 위에 올려서 서버를 실행, `스프링부트` 는 WAS(톰캣) 내장하므로 따로 WAS 설치없이 바로 실행가능하므로 `스프링부트` 프로젝트를 사용**

**main 파트에 `@ServletComponentScan` 선언을 통해서 직접 서블릿을 등록(커스텀) 가능!**

* 아파치 톰캣은 `webapp` 경로를 기본 사용하므로 해당경로에 `index.html` 을 바로 등록 가능

<br>

**아래 순서로 구현**

- hello 서블릿
  - [hello 서블릿 호출](http://localhost:8080/hello?username=servlet)
- HttpServletRequest
  - [기본 사용법, Header 조회](http://localhost:8080/request-header)
  - HTTP 요청 메시지 바디 조회
    - [GET - 쿼리 파라미터](http://localhost:8080/request-param?username=hello&age=20)
    - [POST - HTML Form](http://localhost:8080/basic/hello-form.html)
    - HTTP API - MessageBody -> Postman 테스트
- HttpServletResponse
  - [기본 사용법, Header 조회](http://localhost:8080/response-header)
  - HTTP 요청 메시지 바디 조회
    - [HTML 응답](http://localhost:8080/response-html)
    - [HTTP API JSON 응답](http://localhost:8080/response-json)

<br><br>

## 서블릿 기본 사용

**`HttpServlet` 을 상속 및 `@WebServlet` 사용 및 `service` 를 오버라이드가 기본 사용법**

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse 
    response)
    throws ServletException, IOException {
        return;
    }
}
```

<br>

**스프링부트가 내장톰켓 생성 -> 내장톰켓이 서블릿 생성**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/3157971f-0493-4652-be50-bd9cce075c52) 

<br>

**웹 애플리케이션 서버의 요청과 응답 구조**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/e3af891b-6d53-448f-8fe7-3f66efd3788b) 

<br>

**이러한 구조를 기반으로 "서블릿"의 역할은 클라이언트의 요청에 따라 WAS(톰캣)에서 생성된 request, response 에 관해서 "비지니스 로직, 뷰, 등등" 을 작성하고 이를 반환해서 응답하는 구조를 이룬다.**

<br><br>

## HTTP 통신 - Request, Response

**HTTP 요청 메시지를 개발자가 직접 파싱해서 사용하는건 매우 불편하므로 "서블릿"은 이를 대신해서 그 결과를 `HttpServletRequest` 객체에 담아서 제공!**

**start-line, header 정보 조회에 관해서는 생략하고, 바로 요청 데이터 조회방법을 살펴보자.**

<br>

### 1. HttpServletRequest

**주로 3가지 방법**

* GET - 쿼리파라미터
  * 주로 `?username=hello&age=20`
  * body X
  * content-type: 필요X
* POST - HTML Form
  * 주로 `Html Form`
  * body O -> Form데이터가 body에 입력되어서 넘어오는것!
  * content-type: application/x-www-form-urlencoded
    * form 데이터가 body에 GET의 쿼리 파라미터(`?username=he...`) 형태로 전달됨
* HTTP message body
  * 주로 `JSON` -> 단순한 String도 당연히 가능
  * body O
  * content-type: text/plain (단순 텍스트), application/json (JSON)

<br>

**Json은 보통 자바 객체로 변환해서 사용하는데 그것을 돕는 라이브러리가 다양 -> Gson, Jackson**

**여기선 Jackson 이 제공하는 ObjectMapper 소개**

```java
/**
 * json to object
 * 1) InputStream으로 Body 메시지를 읽고, 해당 내용을 String으로 변환
 * 2) ObjectMapper-readValue로 미리 만든 HelloData객체 형태로 Body 메시지 내용을 변환
 */
private ObjectMapper objectMapper = new ObjectMapper();

@Override
protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

    System.out.println("helloData.username = " + helloData.getUsername());
    System.out.println("helloData.age = " + helloData.getAge());
    response.getWriter().write("ok");
}
```

<br>

### 2. HttpServletResponse

**주로 3가지 방법**

* 단순 텍스트 응답
  * 간단 - `writer.println("ok");` 
* HTML 응답
  * 말그대로 "\<html>" 같은 html을 직접 작성해서 반환
* HTTP API - MessageBody JSON 응답
  * JSON 형태로 반환(실제론 전부 String)

<br>

**JSON으로 반환할때는 객체에서 String으로 바꿔줘야 한다.**

```java
/**
 * json to string
 * 1) ObjectMapper-writeValueAsString로 미리 만든 HelloData객체를 String으로 변환
 */
@Override
protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //Content-Type: application/json
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");

    HelloData helloData = new HelloData();
    helloData.setUsername("kim");
    helloData.setAge(20);

    //{"username":"kim", "age":20}
    String result = objectMapper.writeValueAsString(helloData); // json to string
    response.getWriter().write(result);
}
```

<br><br>

# 서블릿, JSP, MVC 패턴

**현재 스프링을 사용하지않고 개발중이기 때문에 "회원 저장소(DB)"를 싱글톤으로 구현**

```java
/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    // static final로 싱글톤 적용
    private static final MemberRepository instance = new MemberRepository();
    public static MemberRepository getInstance() {
        return instance;
    }
    // private으로 생성자 접근 막기
    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence); // id 이때 할당
        store.put(member.getId(), member); // HashMap 제공 메소드 - put()
        return member;
    }
    public Member findById(Long id) {
        return store.get(id); // HashMap 제공 메소드 - get()
    }
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // store 모든값 ArrayList로 감싸기 -> store 안건드리려고
    }
    public void clearStore() {
        store.clear(); // HashMap 제공 메소드 - clear()
    }
}
```

<br>

**"Form화면, 회원저장, 회원조회" 이렇게 총 3개씩 각각 파일을 만들 예정이다.**

**아래 순서로 구현**

- 서블릿
  - [회원가입](http://localhost:8080/servlet/members/new-form)
  - [회원목록](http://localhost:8080/servlet/members)
- JSP
  - [회원가입](http://localhost:8080/jsp/members/new-form.jsp)
  - [회원목록](http://localhost:8080/jsp/members.jsp)
- 서블릿 MVC
  - [회원가입](http://localhost:8080/servlet-mvc/members/new-form)
  - [회원목록](http://localhost:8080/servlet-mvc/members)

<br><br>

## 1. 서블릿

앞에서 공부한 "서블릿"을 그대로 사용하면 된다.

* **단, 여기서 특징은 HTML 코드를 서블릿 안에서 작성**

<br>

**아래 예시는 회원저장 파트**

```java
@WebServlet(name = "memberSaveServlet", urlPatterns = "/servlet/members/save")
public class MemberSaveServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MemberSaveServlet.service");
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter w = response.getWriter();
        w.write("<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "성공\n" +
                "<ul>\n" +
                "    <li>id="+member.getId()+"</li>\n" +
                "    <li>username="+member.getUsername()+"</li>\n" +
                "    <li>age="+member.getAge()+"</li>\n" +
                "</ul>\n" +
                "<a href=\"/index.html\">메인</a>\n" +
                "</body>\n" +
                "</html>");
    }
}
```

<br><br>

## 2. JSP

**JSP는 `<% ~ %>` 내에서 자바코드 사용가능하며, 밖에서는 HTML코드가 작성가능한 구조를 가진다.**

* JSP, Thymeleaf 등등은 **템플릿 엔진**

**아래는 회원저장 파트 예시이다.**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%
    //request, response 사용 가능
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);

%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
```

<br><br>

## 3. 서블릿 MVC

**앞에서 "서블릿, JSP" 등등을 보면 "비지니스 로직" 과 "뷰 렌더링" 을 모두 처리했는데, 이는 너무 많은 역할을 하며, 특히 둘의 라이프사이클은 다른 문제도 있다.**

* 이러한 이유들 때문에 MVC 패턴이 등장하기 시작
* 여기서부터 MVC 패턴을 계속 수정 보완해가면서 점차적으로 스프링 MVC 패턴을 이루게 됨

<br>

`Servlet(비지니스 로직) + JSP(뷰)` 로 나눠서 개발을 하는 MVC 패턴을 적용해본다.

* 서블릿이 비지니스 로직을 담당한다고 했는데, 정확히는 "컨트롤러" 역할을 하며 비지니스 로직은 그 아래 층에서 진행한다. (여기선 그냥 한꺼번에 진행)

<br>

**MvcMemberFormServlet.java**

```java
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 서버 내에서 forward를 통해 제어권을 new-form.jsp 로 넘긴다 -> 이것이 핵심!!
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

<br>

**new-form.jsp**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<!-- 상대경로 사용, [현재 URL이 속한 계층 경로 + /save] -->
<form action="save" method="post">
    username: <input type="text" name="username" />
    age:      <input type="text" name="age" />
    <button type="submit">전송</button>
</form>

</body>
</html>
```

<br><br>

# MVC 프레임워크 만들기

**앞에서 MVC 패턴은 단점이 존재**

* 포워드 중복
* ViewPath 중복
* 사용하지 않는 코드(예로 HttpServletRequest)
* **공통 처리가 어려움 -> 핵심**

<br>

**이를 해결하기 위해 "프론트 컨트롤러" 패턴을 도입**

**도입 전**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/7de97331-8a65-430e-8a7f-9c7a07acc095) 

<br>

**도입 후**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/d3b26880-2927-46ba-a85d-9bffc7761b3b) 

<br>

**v1~v5 까지는 점진적으로 프레임워크를 발전시키는 모습이다. (코드는 프로젝트에서 확인)**

* v1: 프론트 컨트롤러를 도입

  * 기존 컨트롤러를 인터페이스로 전부 구현 및 "프론트 컨트롤러"만 `@WebServlet` 적용
  * 특히 urlPatterns 에 "/*" 를 적용함으로써 해당 "프론트 컨트롤러"에 요청들이 들어오게 함
    * 인터페이스로 컨트롤러를 각각 구현했기 때문에 "다형성"으로 다 처리할 수 있음

* v2: View 분류

  * 단순 반복되는 뷰 로직 분리 
  * 포워드 하는 중복 코드를 없애기 위해 MyView 같은걸로 통일해서 해당 값을 반환

* v3: Model 추가

  * 서블릿(request,response) 종속성 제거
    * MyView 가 아닌 ModelView 를 반환 
    * `HttpServletRequest` 를 잘 사용안하므로 이를 없애기 위함
  * 뷰네임 중복 제거
    * ModelView를 반환하게 됨으로써 기존 MyView 는 프론트 컨트롤러에서 간단하게 뷰리졸버 개념을 도입해서 구현

* v4: 단순하고 실용적인 컨트롤러 

  * MyView 나 ModelView 도 아닌 이번엔 String으로 반환값을 바꿨음.

    * v3와 거의 비슷
    * 개발자 편의를 위해서 수정한 것 (매번 ModelView를 반환하는것은 편리하지는 않기때문)

    * 구현 입장에서 ModelView를 직접 생성해서 반환하지 않도록 편리한 인터페이스 제공
    * 스프링 MVC에서 반환할때 그냥 String으로 뷰를 반환할 수 있는데 그 기능!

* v5: 유연한 컨트롤러 
  * 어댑터 도입 -> 프론트 컨트롤러와 컨트롤러 사이에 동작해서 유연하게 "컨트롤러 선택"
    * supports : 핸들러(컨트롤러)를 사용할 수 있는가 여부
    * handle : 핸들러(컨트롤러) 실행 및 ModelView 반환
    * Object 로 넘어오기 때문에 `handler instanceof ControllerV3` 로 비교 가능

<br><br>

# 스프링 MVC 구조 이해

**스프링 MVC의 문법의 경우에는 많이 공부한 내용들이어서 생략하고, 구조만 간략히 설명**

**직접 만든 MVC 프레임워크 구조**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/c2823b34-1ed1-44f2-a95c-0890a31212b7) 

<br>

**SpringMVC 구조**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/8e2e26a2-a39c-40ed-bced-91f03b58189f) 

<br>

**직접 만든 프레임워크 <-> 스프링 MVC 비교** 

* FrontController <-> DispatcherServlet 
* handlerMappingMap <-> HandlerMapping 
* MyHandlerAdapter <-> HandlerAdapter 
* ModelView <-> ModelAndView 
* viewResolver <-> ViewResolver
* MyView <-> View 

<br>

**동작 순서**

1. 핸들러조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
2.  핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
3.  핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
3.  핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
5. ModelAndView 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서  반환한다.
6.  viewResolver 호출: 뷰 리졸버를 찾고 실행한다.
* JSP의경우: `InternalResourceViewResolver` 가 자동 등록되고, 사용된다.
7. View 반환: 뷰리졸버는 뷰의 논리 이름을 물리이름으로 바꾸고, 렌더링 역할을 담당하는 뷰객체를 반환한다.
   * JSP의경우 `InternalResourceView(JstlView)` 를반환하는데, 내부에 forward() 로직이있다.
8. 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다.

<br>

**우선순위 -> 실제로는 더 다양**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/2660c1ba-03ef-4b1b-a77f-09c02bc0ab23)  

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/e06690a7-9a3d-439c-81c3-ffa8f007d23d) 

<br>

**SimpleControllerHandlerAdapter 예시**

```java
/**
 * 옛날 방식
 * HandlerMapping = BeanNameUrlHandlerMapping
 * HandlerAdapter = SimpleControllerHandlerAdapter -> 우리가 먼저한 servlet mvc v5와 유사
 *
 * 뷰리졸버는 Bean..과 Internal...(JSP처리) 2가지가 있는데, 여기서는 JSP로 처리해주는 Internal...을 사용
 */
@Component("/springmvc/old-controller") // BeanNameUrlHandlerMapping
public class OldController implements Controller { // SimpleControllerHandlerAdapter

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form"); // 이부분이 스프링이 제공한 뷰리졸버를 사용한 개념
    }
}
```

<br>

**HttpRequestHandlerAdapter 예시**

```java
/**
 * 옛날 방식
 * HandlerMapping = BeanNameUrlHandlerMapping
 * HandlerAdapter = HttpRequestHandlerAdapter
 */
@Component("/springmvc/request-handler") // BeanNameUrlHandlerMapping
public class MyHttpRequestHandler implements HttpRequestHandler { // HttpRequestHandlerAdapter
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
```

<br>

스프링 부트는 `InternalResourceViewResolver` 라는 뷰 리졸버를 자동 등록하는데, 이때 따로 `application.properties` 에 등록한 `spring.mvc.view.prefix , spring.mvc.view.suffix` 설정 정보를 사용해서 등록한다.

* **요즘에는 Thymeleaf 의 경우 이 설정까지 자동으로 등록해줘서 설정할 필요가 없다.**

<br><br>

# Folder Structure

생략..
