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

**이러한 구조를 기반으로 "서블릿"의 역할은 클라이언트의 요청에 따라 WAS(톰캣)에서 생성된 request, response 에 관해서 "비지니스 로직" 을 작성하고 이를 반환해서 응답하는 구조를 이룬다.**

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

**DB를 위해 간단히 회원 저장소를 구현**



<br>

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



<br><br>

## 2. JSP

<br><br>

## 3. 서블릿 MVC





<br><br>

# MVC 프레임워크 만들기





<br><br>

# 스프링 MVC 구조 이해



<br><br>

# Folder Structure

생략..
