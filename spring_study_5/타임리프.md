# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **타임리프 관련 프로젝트가 2개라서 한꺼번에 정리**
  * 첫번째 프로젝트는 타임리프 순수 문법(기능) - thymeleaf-basic
  * 두번째 프로젝트는 스프링+타임리프 통합을 통해 추가로 사용가능한 타임리프 문법(기능) - form

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

# 1. 타임리프 추가 문법

**1편에서 문법을 간략히 알아봤는데 여기서 좀 더 알아보는 시간을 가진다.**

<br>

**아래 순서로 구현**

- 텍스트
  - [텍스트 출력 기본](http://localhost:8080/basic/text-basic)
  - [텍스트 text, utext](http://localhost:8080/basic/text-unescaped)
- 표준 표현식 구문
  - [변수 - SpringEL](http://localhost:8080/basic/variable)
  - [기본 객체들](http://localhost:8080/basic/basic-objects?paramData=HelloParam)
  - [유틸리티 객체와 날짜](http://localhost:8080/basic/date)
  - [링크 URL](http://localhost:8080/basic/link)
  - [리터럴](http://localhost:8080/basic/literal)
  - [연산](http://localhost:8080/basic/operation)
- 속성 값 설정
  - [속성 값 설정](http://localhost:8080/basic/attribute)
- 반복
  - [반복](http://localhost:8080/basic/each)
- 조건부 평가
  - [조건부 평가](http://localhost:8080/basic/condition)
- 주석 및 블록
  - [주석](http://localhost:8080/basic/comments)
  - [블록](http://localhost:8080/basic/block)
- 자바스크립트 인라인
  - [자바스크립트 인라인](http://localhost:8080/basic/javascript)
- 템플릿 레이아웃
  - [템플릿 조각](http://localhost:8080/template/fragment)
  - [유연한 레이아웃](http://localhost:8080/template/layout)
  - [레이아웃 상속](http://localhost:8080/template/layoutExtend)

<br><br>

## 텍스트

**HTML 엔티티**

* 웹브라우저는 < 를 HTML 테그의 시작으로 인식한다. 따라서 < 를 테그의 시작이 아니라 문자로 표현할 수 있는 방법이 필요한데, 이것을 HTML 엔티티라 한다.
* HTML 엔티티로 변경하는것을 **이스케이프(escape)**라 한다. 
* 그리고 타임리프가 제공하는 th:text , [[...]] 는기본적으로 이스케이스(escape)를 사용한다.
  * < : \&lt;
  * \> : \&gt;
* **만약 escape를 사용하지 않고 싶다면 Unescape 라고 해서 utext를 사용!**

<br>

**text vs utext**

- th:text = Hello \<b>Spring!\</b>
- th:utext = Hello **Spring!**

**[[...]] vs [(...)] -> 컨텐츠 안에서 직접 출력!**

- [[...]] = Hello \<b>Spring!\</b>
- [(...)] = Hello **Spring!**

```html
<h1>컨텐츠에 데이터 출력하기</h1>
<ul>
  <li>th:text 사용 <span th:text="${data}"></span></li>
  <li>컨텐츠 안에서 직접 출력하기 = [[${data}]]</li>
</ul>
```

<br><br>

## 표준 표현식 구문

**SpringEL 표현식**

* Object

  - ${user.username} = userA

  - ${user['username']} = userA

  - ${user.getUsername()} = userA

- List
  - ${users[0].username} = userA
  - ${users[0]['username']} = userA
  - ${users[0].getUsername()} = userA

- Map
  - ${userMap['userA'].username} = userA
  - ${userMap['userA']['username']} = userA
  - ${userMap['userA'].getUsername()} = userA

**지역 변수 - (th:with)**

* 변수 선언이라고 할 수 있음

```html
<div th:with="first=${users[0]}">
    <p>처음 사람의 이름은 <span th:text="${first.username}"></span></p>
</div>
```

**편의 객체**

* HTTP 요청 파라미터 접근: param
  * 예) ${param.paramData} 
* HTTP 세션 접근: session
  * 예) ${session.sessionData}

**url링크(심화)**

* /hello
* /hello?param1=data1&param2=data2
* /hello/data1/data2
* /hello/data1?param2=data2

```java
<li><a th:href="@{/hello}">basic url</a></li>
<li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">hello query param</a></li>
<li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">path variable</a></li>
<li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">path variable + query parameter</a></li>
```

**비교연산 : HTML 엔티티부분을 주의**

* \>(gt), <(lt), >=(ge), <=(le), !(not), ==(eq), !=(neq, ne)

**Elvis 연산자 : 조건식의 편의 버전**

```html
<li>${data}?: _ = <span th:text="${data}?: _">데이터가 없습니다.</span></li>
<li>${nullData}?: _ = <span th:text="${nullData}?: _">데이터가 없습니다.</span></li>
```

**No-Operation : 마치 타임리프가 실행되지 않는 것처럼 동작**

* _

<br><br>

## 속성값 설정

**HTML에서 checked 속성은 checked 속성의 값과 상관없이 checked 라는 속성만 있어도 체크가 된다.**

**타임리프의 th:checked 는 값이 false 인경우 checked 속성 자체를 제거해서 이를 해결한다.**

```html
<input type="checkbox" name="active" th:checked="false" />
타임리프렌더링 후:  <input type="checkbox" name="active" />
```

<br><br>

## 주석

**거희 2번을 쓴다고 생각하면 됨**

```html
<h1>1. 표준 HTML 주석</h1>
<!--
<span th:text="${data}">html data</span>
-->

<h1>2. 타임리프 파서 주석</h1>
<!--/* [[${data}]] */-->

<!--/*-->
<span th:text="${data}">html data</span>
<!--*/-->

<h1>3. 타임리프 프로토타입 주석</h1>
<!--/*/
<span th:text="${data}">html data</span>
/*/-->
```

<br><br>

## 블록

**`<th:block>` 는 타임리프가 제공하는 유일한 자체 "태그"**

**렌더링 할때는 아예 태그가 삭제된다는 특징!**

```html
<th:block th:each="user : ${users}">
    <div>
        사용자 이름1 <span th:text="${user.username}"></span>
        사용자 나이1 <span th:text="${user.age}"></span>
    </div>
    <div>
        요약 <span th:text="${user.username} + ' / ' + ${user.age}"></span>
    </div>
</th:block>
```

위 내용의 결과는 block으로 하나로 묶어서 결과가 나타난다는걸 알수 있고 `<div>...</div> <div>...</div>` 이 구조가 반복된다.

그러나 th:block을 사용안하고 임의로 div 태그로 선정해서 each 돌린다면 위 구조에서 겉에 div로 감싼 구조가 반복해서 생성된다.

<br><br>

## 인라인 JS, Fragment

**잘 아는 내용이라 생략**

<br><br>

# 2. 타임리프 스프링 통합, 폼

**타임리프는 기본적으로 혼자 쓸수있고, 이를 스프링과 통합해서도 쓸 수 있다.**

**통합할때는 설정을 많이 해야하는데, 스프링부트는 이를 거의 자동으로 지원해준다.**

**스프링 통합으로 추가되는기능들** 

* 스프링의 SpringEL 문법통합
* ${@myBean.doSomething()} 처럼스프링 빈 호출지원 
* 편리한폼 관리를 위한 추가속성
  * th:object (기능강화, 폼커맨드 객체선택) 
  * th:field , th:errors , th:errorclass
* 폼컴포넌트 기능
  * checkbox, radio button, List 등을 편리하게 사용할 수 있는 기능 지원 
* 스프링의 메시지, 국제화 기능의 편리한통합
* 스프링의 검증, 오류처리 통합
* 스프링의 변환서비스 통합(ConversionService)

<br>

## 타임리프의 개선점 - 주로 Form에서 사용

**1. th:object, th:field, *{itemName} 활용**

* th:field -> name, id, value 자동 생성
  * name, id를 변수이름(ex: itemName)으로 자동으로 만들어주고, value는 해당 변수의 값을 사용

* *{itemName} 은 item.itemName 을 의미 -> th:object로 선언한 item을 *로 접근한 것

```html
<form action="item.html" th:action th:object="${item}" method="post">
    <div>
        <label for="itemName">상품명</label>
        <input type="text" id="itemName" th:field="*{itemName}" class="form- 
        control" placeholder="이름을 입력하세요">
    </div>
</form>
```

<br>

**2. 체크박스, 라디오버튼, 셀렉트 박스(아래이미지 같은..) 에서 TIP을 소개**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/9cb596cb-494a-4536-91b3-0d7358800bcf) 

<br>

**다양한 데이터 형태를 Form으로 받을것임**

```java
// Item.java 에 아래 타입들 추가
private Boolean open; //판매 여부
private List<String> regions; //등록 지역
private ItemType itemType; //상품 종류
private String deliveryCode; //배송 방식

// DeliveryCode.java -> 배송 방식
private String code; 
private String displayName;

// ItemType.java -> enum 타입
BOOK("도서"), FOOD("식품"), ETC("기타")
```

* **체크박스 - 단일 1**

  ```html
  <hr class="my-4">
  <!-- single checkbox 수정전 --> 
  <div>판매 여부</div>
  <div>
      <div class="form-check">
          <input type="checkbox" id="open" name="open" class="form-check-input"> 
          <label for="open" class="form-check-label">판매 오픈</label>
      </div> 
  </div>
  
  <hr class="my-4">
  <!-- single checkbox 수정후 --> 
  <div>판매 여부</div>
  <div>
      <div class="form-check">
          <input type="checkbox" id="open" name="open" class="form-check-input"> 
          <input type="hidden" name="_open" value="on"/> <!-- 히든 필드 추가 --> 
          <label for="open" class="form-check-label">판매 오픈</label>
      </div> 
  </div>
  ```

  * **Form 내에서** HTML의 체크박스를 체크하고 보냈을때는 "on" 값이 넘어가고 스프링이 자동으로 true로 읽어들인다.
  * 단, 체크를 하지 않은 경우 아예 값이 넘어가질 않아서 스프링은 null로 읽는다. (false가 아니라)
  * 따라서 hidden 타입을 넣어서 무조건 전송되게 체크박스 value="on" 을 주고 _open 이라는 이름을 써서 해결한다.
    * `open=on&_open=on` : 체크박스를 체크하면 스프링 MVC가 open 에 값이 있는것을 확인하고사용한다. 이때 _open 은 무시한다.
    * `_open=on` : 체크박스를 체크하지 않으면 스프링 MVC가 _open 만 있는것을 확인하고, open 의 값이 체크되지 않았다고 인식한다. 이경우 서버에서 Boolean 타입을 찍어보면 결과가 null 이 아니라 false 인 것을 확인할 수있다. 

* **체크박스 - 단일 2**

  * 위처럼 **hidden 을 넣는 걸 자동**으로 해주는게 "타임리프 기능" - 앞서 배운 `th:filed` 만 넣어주면 끝!!

  * Form으로부터 체크(true) 데이터 얻어와서 "Item 상세" 페이지에서 **`th:filed` 가 자동으로 checked="checked" 까지 넣어줘서 체크표시를 해줌**

    * 참고로 당연히 "Item 수정" 페이지처럼 "Item" 자체 정보를 수정하는 로직도 잘 작성
    * 또한, th:object 사용 안했기에 ${item.open} 사용
    * 참고로 "Item 상세" 페이지이다 보니 여기선 체크박스 상태를 보여야지 체크가 되면 안되기 때문에 `disabled` 를 함께 적용

    ```html
    <!-- single checkbox -->
    <div>판매 여부</div>
    <div>
        <div class="form-check">
            <input type="checkbox" id="open" th:field="${item.open}" class="form-check-input" disabled>
            <label for="open" class="form-check-label">판매 오픈</label>
        </div>
    </div>
    ```

* **체크박스 - 멀티**

  * **@ModelAttribute의 특별한 사용법**으로써 전역으로 Model에 값을 항상 넣는방법 존재

    * **물론 static으로 따로 구현해두는게 성능상 더 좋지만**, 예제니까..!

    * **선언해두면 따로 호출할 필요없이 그냥 Model 사용하면 자동으로 아래 값이 담겨있음**

      ```java
      @ModelAttribute("regions") // regions 이름으로 Model에 넣음
      public Map<String, String> regions() {
          Map<String, String> regions = new LinkedHashMap<>();
          regions.put("SEOUL", "서울");
          regions.put("BUSAN", "부산");
          regions.put("JEJU", "제주"); 
      return regions;
      }
      ```

  * 멀티 체크박스의 경우 체크박스 여러개 사용하는걸 말하는데 아래처럼 구현

    * ***{regions}** 는 Item.java의 `private List<String> regions;` 를 의미
    * **th:each의 regions**는 바로 위의 Model에 담아온 regions를 의미
    * 특히 `<label>` 속의 th:for는 input의 id값과 동일하게 연결해야하는데, 해당 id가 동적으로 생성되기 때문에 동적id를 인식하는 #ids.prev() 문법을 지원

    ```java
    <!-- multi checkbox -->
    <div>
    	<div>등록 지역</div>
        <div th:each="region : ${regions}" class="form-check form-check-inline">
    	    <input type="checkbox" th:field="*{regions}" th:value="${region.key}" class="form-check-input">
        	<label th:for="${#ids.prev('regions')}" th:text="${region.value}" class="form-check-label">서울</label>
        </div>
    </div>
    ```

* **라디오 버튼**

  * 이또한 체크안하면 null이 넘어간다. 단, 전혀 문제없다. 왜???

    * 체크안하면 체크안한 상태를 "Item 상세" 에서 볼 수 있다 -> 딱히 문제없음
    * 또 체크를 한 후에는 라디오 버튼 특성상 체크를 무조건 하나는 해야한다 -> 따라서 문제없음

  * EnumType 을 사용하기 위해 Model에 또 전역으로 넣기

    ```java
    @ModelAttribute("itemTypes") 
    public ItemType[] itemTypes() {
    	return ItemType.values(); // ENUM의 모든정보가 배열로 반환
    }
    ```

  * html 모습

    ```html
    <!-- radio button -->
    <div>
        <div>상품 종류</div>
        <div th:each="type : ${itemTypes}" class="form-check form-check-inline">
            <input type="radio" th:field="${item.itemType}" th:value="${type.name()}" class="form-check-input" disabled>
            <label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label">
                BOOK
            </label>
        </div>
    </div>
    ```

* **셀렉트 박스**

  * 앞전과 다 비슷

    ```java
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }
    ```

  * 비슷 html

    ```html
    <!-- SELECT -->
    <div>
        <div>배송 방식</div>
        <select th:field="*{deliveryCode}" class="form-select">
            <option value="">==배송 방식 선택==</option>
            <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}"
                    th:text="${deliveryCode.displayName}">FAST</option>
        </select>
    </div>
    ```

* **정리**
  * "체크박스 단일" 에서는 반드시 null이 아닌 true, false 값을 받기 위해서 세팅했고,
  * "체크박스 멀티" 에서는 여러개 체크박스를 받는 세팅을 했고(List\<String>),
  * "라디오 버튼" 은 null 이 있어도 상관없고 한번 체크한 후에는 계속 체크(EnumType),
  * "셀렉트 박스" 도 전부 비슷(자바 객체 DeliveryCode 사용)

<br><br>

# Folder Structure

생략..
