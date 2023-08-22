# Intro..

**스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **프로젝트 총 3가지** -> 현재 포스팅은 item-service 프로젝트
  * servlet : 서블릿, JSP, MVC 패턴, MVC 프레임워크, 스프링 MVC 이해
  * springmvc : 스프링 mvc 기능 알아보기
  * **item-service : 스프링 mvc 로 프로젝트 해보기(웹페이지 만들기)**
    * **부트스트랩 + 타임리프도 함께 사용**

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

# 서비스 분석

**index.html은 `/resources/static/` 위치에 두기 -> Welcome 페이지!**

* Jar 이기에 webapp 경로 사용X 

<br><br>

## 1. 요구사항 분석

- **상품 도메인 모델**
  - 상품 ID 
  - 상품명 
  - 가격
  - 수량
- **상품 관리 기능**
  - 상품목록 
  - 상품상세 
  - 상품등록 
  - 상품수정

<br><br>

## 2. 예상 서비스 화면

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/cdff394f-4252-47d2-a8eb-2f75650c13f7) 

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/f37f2407-e01e-45dc-94c5-8c6de5ac17f6) 

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/985111da-2ab7-451f-bbf0-2ab497d61d68) 

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/e8257684-580d-4e55-8b71-14694d07a71b) 

<br><br>

## 서비스 제공 흐름

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/d84e9f3c-77ed-44bd-92f7-e17ccea6cbca) 

<br><br>

# 비지니스 로직

**간단히 DB없이 메모리를 저장소로 바로 사용! -> 따라서 static으로 구현**

**레퍼지토리 예시**

```java
@Repository
public class ItemRepository {
	// 실무에서는 동시성 고려한 Map을 사용해줘야 함 -> 아래 Hash는 동시성 고려X
    private static final Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }
    // ...
}
```

<br>

**상품 도메인 개발 간단해서 PASS**

<br><br>

## 상품 서비스 HTML

**정적으로 넣은 HTML이며 방법과 태그 사용 방식 확인**

**"부트스트랩 설치"**

* 부트스트랩 공식 사이트: https://getbootstrap.com 
* 부트스트랩을 다운로드 받고 압축을 풀자.
* 이동: https://getbootstrap.com/docs/5.0/getting-started/download/ 
  * Compiled CSS and JS 항목을 다운로드 하자.
  * 압축을출고 `bootstrap.min.css` 를 복사해서 다음폴더에 추가하자 
    * `resources/static/css/bootstrap.min.css`
* 주의사항
  * 가끔 복사해서 붙여넣는데 적용이 안될때가 있다. 
  * 이때는 out폴더를 삭제후 서버를 껏다켜면 out폴더가 새로 빌드되며 css가 생성되어서 적용이 될것이다.

<br>

**전달받은 HTML을 `resources/static/html/` 경로에 전부 작성**

* addForm, editForm, Item, Items.html

<br>

**사용 태그분석**

* `<div>` 를 감싸서 보통 구분(container 역할)
* `<label>, <input>` 조합으로 form 데이터 입력받는중
* `<button>` 버튼 넣는것 조차 `<div>` 로 감싸서 원하는 위치, 크기 등등 조절
* `<table>, <thead>, <tbody>` 방식으로 데이터 목록들을 표시
  * `<tr>, <th>, <td>` 주로 사용
  * `<a>` 태그 활용해서 간단히 링크연결도 자주 사용

<br><br>

## 타임리프 사용

**이제는 동적 HTML을 사용할거라 `resources/templates/` 경로를 사용**

**"타임리프"의 장점은 타임리프 문법을 사용한 경우에 동적으로 HTML 생성해주고, 그게아닌건 바로 정적으로 HTML 나타내주는 장점이 있다.**

<br>

**간단한 문법들 살펴보자**

* 핵심 : 서버로 실행(뷰 템플릿 사용)하면 타임리프 문법들이 적용해서 동적으로 변경!
* 타임리프 사용 선언
  * `<html xmlns:th="http://www.thymeleaf.org">`
  * **참고로 `param` 으로 바로 파라미터값 불러올 수 있게 제공도 해주는 중**

* 속성 변경

  * `th:href="@{/css/bootstrap.min.css}"`
  * `th:onclick="|location.href='@{/basic/items/add}'|"`
  * `<td th:text="${item.price}">10000</td>`
  * `th:value="${item.id}"`
  * `th:action`
  * ... 등등 매우 다양

* URL 링크표현식 - @{...}

  * `th:href="@{/css/bootstrap.min.css}"`

  * `th:href="@{/basic/items/{itemId}(itemId=${item.id})}"`
  * 심화) `th:href="@{/basic/items/{itemId}(itemId=${item.id}, query='test')}" `
    * 생성된 링크: `http://localhost:8080/basic/items/1?query=test`
  * 간편) `th:href="@{|/basic/items/${item.id}|}"`
    * 리터럴 대체문법 적용가능

* 리터럴 대체 - |...|

  * 타임리프에서 문자와 표현식 등은 분리되어 있기 때문에 더해서 사용해야 한다.
    * `<span th:text="'Welcome to our application, ' + ${user.name} + '!'">`
  * 다음과같이 리터럴 대체문법을사 용하면, 더하기 없이 편리하게 사용할 수 있다.
    * `<span th:text="|Welcome to our application, ${user.name}!|">`
    * `th:onclick="|location.href='@{/basic/items/{itemId}/edit(itemId=${item.id})}'|"`

* 변수표현식 - ${...}

  * `<td th:text="${item.price}">10000</td>`

* 반복출력 - th:each

  * `<tr th:each="item : ${items}">`
  * 컬렉션의 수만큼 `<tr>..</tr>` 이 하위 테그를 포함해서 생성된다.

* 조건문 - th:if

  * `<h2 th:if="${param.status}" th:text="'저장 완료'"></h2>`

<br><br>

## 컨트롤러

**상품목록**

```java
@GetMapping
public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items"; // resources/templates/basic/items.html 을 사용
}
```

<br>

**상품상세**

```java
@GetMapping("/{itemId}")
public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
}
```

<br>

**상품등록 -> PRG 적용**

```java
@GetMapping("/add")
public String addForm() {
    return "basic/addForm";
}

// PRG 적용전
@PostMapping("/add")
public String addItemV5(Item item) {
    itemRepository.save(item);
    return "redirect:/basic/items/" + item.getId(); // 이런식은 인코딩에 위험
}
// PRG 적용후 -> RedirectAttributes 도 함께 활용
@PostMapping("/add")
public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/basic/items/{itemId}"; // redirectAttributes 덕분에 {itemId} 가능
}
```

<br>

**상품수정 -> redirect 활용**

```java
@GetMapping("/{itemId}/edit")
public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
}

@PostMapping("/{itemId}/edit")
public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/basic/items/{itemId}"; // PathVariable 덕분에 {itemId}
}
```

<br><br>

## PRG Post/Redirect/Get 

**웹 브라우저의 새로고침은 마지막 서버에 전송한 데이터를 다시 전송한다.**  

**따라서 POST 적용후 새로고침을 하면 계속 POST 보내는 문제가 발생하므로 이를 Redirect를 통해서 GET으로 요청하는 방식으로 해결할 수 있다.**

<br>

**PRG 적용 전의 모습**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/612699ba-36c5-462c-acdb-9aebd3a26f19) 

<br>

**PRG 적용 후의 모습**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/e604c337-0aa1-4ddc-a39d-aa1bcf686b45) 

<br><br>

## RedirectAttributes

**리다이렉트를 할 때 `RedirectAttributes` 를 사용하면 URL 인코딩 문제도 줄어들고 Model로 값넘기는것처럼 파라미터로 값 넘기는것도 제공하기 때문에 활용하기 좋음**

**특히 실무에서는 Form 데이터 등록후 "저장완료" 이런 표시를 제공할때 많이 사용**

* `redirectAttributes.addAttribute("status", true);`
  * 위 코드로 status 파라미터 값을 적용해주고
* `<h2 th:if="${param.status}" th:text="'저장 완료'"></h2>`
  * param.status 를 사용해서 "저장완료" 표시!!

<br><br>

# Folder Structure

생략..
