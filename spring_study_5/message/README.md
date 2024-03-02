# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **스프링이 제공하는 메시지, 국제화 기능 적용 프로젝트(form프로젝트에 적용)** - message

<br><br>

#  프로젝트 환경설정 & 생성

**이전에 한 form 프로젝트에 "메시지 국제화" 기능 추가**

<br><br>

# 메시지, 국제화

**html을 보면 label 태그 이름들 전부 하드코딩 되어있는데 이걸 한번에 수정하려면 정말 여러곳을 수정해야한다.**

**이를 한번에 수정하게끔 한곳에서 관리하는 것을 `"메시지 기능"`**

**`"국제화"` 는 messages_en.properties, messages_ko.properties 이런식으로 설정파일 나눠서**
**나라별로 다른 이름을 적용하는 기능**

**스프링은 이 기능을 제공해주고, 타임리프는 이 기능들을 쉽게 적용할 수 있음**

<br><br>

## 사용법

`MessageSource` 를 스프링 빈에 등록해야하는데 인터페이스라서 구현체인 `ResourceBundleMessageSource ` 를 등록해야한다.

* 직접 @Bean으로 등록해도 되지만 **쉬운방법이 존재**
* `application.properties` 에 `spring.messages.basename=message` 를 추가!!
* 이후 `messages.properties` 를 추가해서 messages에 담을 내용을 세팅하면 끝!
* 참고) 한글 깨지면 세팅에서 File-Encoding 부분을 UTF-8로 수정

<br>

**테스트 코드로 동작 확인!**

* getMessage 에 locale 을 null 로 보내면 지정했던 basename값을 사용
* 또한, default 를 지정하면 messages에 없는내용은 default 값을 나타냄
* 마지막 arg(파라미터)는 Object 형태로 넘겨줘야 함

```java
/**
* 참고로 hello=hello와 hello.name=hello {0} 가 messages.properties에 기록되어 있음
*/

@Autowired // 스프링 빈 필드 주입
MessageSource ms;

@Test
void helloMessage() {
    String result = ms.getMessage("hello", null, null);
    assertThat(result).isEqualTo("안녕");
}

@Test
void notFoundMessageCode() {
    assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
        .isInstanceOf(NoSuchMessageException.class);
}

// 기본값
@Test
void notFoundMessageCodeDefaultMessage() {
    String result = ms.getMessage("no_code", null, "기본 메시지", null);
    assertThat(result).isEqualTo("기본 메시지");
}

// 파라미터
@Test
void argumentMessage() {
    String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
    assertThat(message).isEqualTo("안녕 Spring");
}

// 국제화
@Test
void defaultLang() {
    assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
    assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
}

@Test
void enLang() {
    assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
}
```

<br><br>

## 프로젝트에 적용하기 - 메시지

**`<h2 th:text="#{page.addItem}">상품 등록</h2>` 이런식으로 타임리프로 간단히 적용**

**적용 리스트**

* label.item=상품
* label.item.id=상품 ID
* label.item.itemName=상품명
* label.item.price=가격 
* label.item.quantity=수량
* page.items=상품 목록
* page.item=상품 상세
* page.addItem=상품 등록
* page.updateItem=상품 수정
* button.save=저장 
* button.cancel=취소

<br><br>

## 프로젝트에 적용하기 - 국제화

**정말 간단하게 `messages_en.properties` 로 다른 나라용으로 설정파일 똑같이 만들어서 추가하면 끝**

* label.item=Item
* label.item.id=Item ID
* label.item.itemName=Item Name
* label.item.price=price
* label.item.quantity=quantity
* page.items=Item List
* page.item=Item Detail
* page.addItem=Item Add
* page.updateItem=Item Update
* button.save=Save 
* button.cancel=Cancel

<br>

테스트 방법은 크롬브라우저 -> 설정 -> 언어를 검색하고, 우선순위를 변경 후 테스트

* 요청시 Accept-Language 의 값이 변경됨
* 스프링은 `Locale` 정보를 Accept-Language 헤더 값을 사용
  * 또한 이를 코드로 구성한 AcceptHeaderLocaleResolver 구현체를 사용
  * 이 리졸버를 당연히 따로 더 구현해서 임의로 `Locale` 정보를 주게끔 구현도 가능
  * LocaleResolver라는 "인터페이스" 를 전부 구현해서 사용하기 때문

<br>

**LocaleResolver 인터페이스 구조**

```java
public interface LocaleResolver {
    Locale resolveLocale(HttpServletRequest request);
    
    void setLocale(HttpServletRequest request, @Nullable HttpServletResponse 
    response, @Nullable Locale locale);
}
```

<br><br>

# Folder Structure

생략..
