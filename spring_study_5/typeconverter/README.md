# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **스프링 타입 컨버터** - typeconverter

<br><br>

#  프로젝트 환경설정 & 생성

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

# 스프링 타입 컨버터, 포맷터

**"타입 컨버터" 는 String to Integer, Integer to String 처럼 타입을 변환하는 것이며 이는 개발하다보면 정말 많이 사용한다.**

**"포맷터" 의 경우 특별한 "타입 컨버터" 일 뿐 완전 다른개념이 아니다.**

* **웹**에 주로 사용하는 `@Requestparam, @ModelAttribute, @PathVariable` 을 사용해보면 이미 스프링이 지원하고 있음을 알 수 있다.
  * 물론, **"타임리프"** 도 지원 - `th:field, ${{...}}`
* 단, **HTTP API (@ResponseBody 등)**의 경우 지원하지 않는다(**HttpMessageConverter 는 "컨버전 서비스 적용 불가"**)
  * 이 경우에는 Jackson 같은 라이브러리에서 포맷터를 찾아 써야한다.

* **(웹) ArgumentResolver는 내부적으로 등록된 컨버터(포멧터)를 사용해서 값을 바인딩**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/4464139b-adf9-464c-b53c-a292f1915099) 

<br>

* **(HTTP API) ArgumentResolver는 미디어 타입과 요청 클래스 타입을 고려한 다음, 적절한 HttpMessageConverter를 사용**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/68e836de-9a94-486a-bf8b-38def3d9b046) 

<br>

## 1. 확장 - 기본

**개발자가 확장할 수 있도록 "컨버터 인터페이스" 제공 - `Converter`**

* **"컨버전 서비스" 에 등록해서 사용이 편리 - `DefaultFormattingConversionService`**
* 왜 포맷팅꺼를 사용했냐면 "컨버터, 포맷팅" 둘다 지원하기 때문

```java
// String -> IpPort (직접 만든 객체) 를 예시
@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {
        log.info("convert source={}", source);
        //"127.0.0.1:8080" -> IpPort 객체
        String[] split = source.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        return new IpPort(ip, port);
    }
}
```

```java
// 컨버전 서비스 사용 예시
public class FormattingConversionServiceTest {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        //포멧터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        //컨버터 사용
        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
        //포멧터 사용
        assertThat(conversionService.convert(1000, String.class)).isEqualTo("1,000");
        assertThat(conversionService.convert("1,000", Long.class)).isEqualTo(1000L);

    }
}
```

<br><br>

## 2 . (권장) 확장 - 스프링 빈 등록

**위처럼 "컨버전 서비스" 를 직접 선언해서 등록하는것이 아닌 "스프링 빈" 에 등록을 해보자(1순위로 적용 됨)**

* @RequestParam 등으로 테스트 해보면 자동으로 사용함을 알 수 있다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 우선순위 때문에 주석처리(타입변환이 겹쳐서)
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        //추가
        registry.addFormatter(new MyNumberFormatter());
    }
}
```

<br>

**또한, 포맷터도 인터페이스가 있지만 굳이 언급안한것은 더 쉽게 구현하는법이 있기때문**

* 예로 @NumberFormat,  @DateTimeFormat

```java
@Data
static class Form {
    @NumberFormat(pattern = "###,###")
    private Integer number;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;
}
```

<br><br>

# Folder Structure

생략..
