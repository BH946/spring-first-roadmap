# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **검증 관련해서 프로젝트는 2개를 진행**
  * **첫번째 검증 프로젝트는 우리가 "직접" 검증 로직을 작성해보기** - validation
  * **두번째 검증 프로젝트는 "스프링" 제공 검증 어노테이션 활용해보기** - validation


<br><br>

# 1. "직접" 검증 로직 작성

**컨트롤러의 중요한 역할중하나는 HTTP 요청이정상인지 검증하는것**

**참고: 클라이언트 검증, 서버검증**

* 클라이언트 검증은 조작할수 있으므로 보안에 취약하다. 
  * 예로 포스트맨으로 JS 실행없이 데이터 조작 요청이 가능
* 서버만으로 검증하면, 즉각적인 고객 사용성이 부족해진다. 
* 둘을 적절히 섞어서 사용하되, **최종적으로 서버검증은 필수**
* API 방식을 사용하면 API 스펙을 잘정의해서 검증 오류를 API 응답결과에 잘 남겨주어야 함

<br>

**구현 순서**

- 상품 관리
  - [상품 관리 - 검증 v1](http://localhost:8080/validation/v1/items)
  - [상품 관리 - 검증 v2](http://localhost:8080/validation/v2/items)
  - [상품 관리 - 검증 v3](http://localhost:8080/validation/v3/items)
  - [상품 관리 - 검증 v4](http://localhost:8080/validation/v4/items)

<br><br>

## 프로젝트 환경설정 & 생성

**이전에 한 프로젝트를 일부 수정했을 뿐**

<br><br>

## 검증 직접 처리 - V1

**어디서 검증을 진행하나??**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/ab19f190-c4b5-4c04-86ce-70182a2a467f) 

<br>

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/6248d156-911d-4f31-b9c7-75c3d54aec7b) 

**이곳이 제일 핵심**

<br>

**검증 작성 흐름**

* **검증 오류 결과를 보관**하는 Map 이 필요
  * 특정 필드의 경우 "Key" 를 필드 이름으로 기록
  * 복합(필드 여러개 사용) 의 경우 "Key" 를 globalError 이런식으로 기록
* **검증 로직**을 작성 (예로 공백X, 숫자 범위 지정 등등)
  * 검증 실패시 "다시" 기존 입력 폼으로..(forward)
    * 이때!! 검증 오류 보관한 Map 데이터를 Model에 담아서!!
  * 검증 성공시 "성공" 화면으로..(redirect)
* 뷰 템플릿 꾸미기 -> 즉, **에러 메시지 출력**

<br>

**이 과정을 모두 코드로 한번에 살펴보자.**

* **@GET 부분 주석도 잘 참고!! 중요한 내용!**

```java
@GetMapping("/add")
public String addForm(Model model) {
    // 아래 코드가 있으면 타임리프에 ${item} 관련 로직 작성이 되니까 일부러 넣어둔것!!
    // POST에서 검증 실패시 @ModelAttribute 때문에 model에 "item" 데이터 담아서 html로 넘어가는 장점!
    // => 자원 재활용(item)!!
    model.addAttribute("item", new Item());
    return "validation/v1/addForm";
}

@PostMapping("/add")
public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

    //검증 오류 결과를 보관
    Map<String, String> errors = new HashMap<>();

    //검증 로직
    if (!StringUtils.hasText(item.getItemName())) { // StringUtils 사용이유 : NullSafe
        errors.put("itemName", "상품 이름은 필수입니다.");
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
        errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9999) {
        errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
    }

    //특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
            errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
        }
    }

    //검증에 실패하면 다시 입력 폼으로
    if (!errors.isEmpty()) {
        log.info("errors = {} ", errors);
        model.addAttribute("errors", errors);
        return "validation/v1/addForm"; // addForm.html 로 접근(forward)
    }

    //성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v1/items/{itemId}";
}
```

<br>

```html
...
<div th:if="${errors?.containsKey('globalError')}">
    <p class="field-error" th:text="${errors['globalError']}">전체 오류 메시지</p>
</div>

<div>
    <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
    <input type="text" id="itemName" th:field="*{itemName}"
           th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"
           class="form-control" placeholder="이름을 입력하세요">
    <div class="field-error" th:if="${errors?.containsKey('itemName')}" th:text="${errors['itemName']}">
        상품명 오류
    </div>
</div>
...
```

<br>

**실행 모습**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/321f07b3-b6d2-4196-9457-b0f336cb1f92) 

<br>

**남은 문제점**

* 뷰 템플릿에 중복 처리가 많음
* 타임 오류 처리가 안됨
* 고객이 입력한 값도 어딘가에 별도로 관리가 필요
  * 만약 Integer 로 숫자 입력받아야하는걸 고객이 String으로 문자를 입력했는데, 애초에 Integer라서 해당 데이터를 입력받지 못해 사라지고
  * 고객은 뭘 입력했길래 틀렸는지 이해하기 어려워짐

<br><br>

## BindingResult - V2

### addItemV1 - BindingResult 사용법

**스프링이 제공해주는 `BindingResult` 사용해보기**

* BindingResult에 에러를 담아준다!
* 특정 필드는 FiledError에, 복합(global했던거)의 경우에는 ObjectError 객체에 담자!
* **주의**
  * BindingResult bindingResult 파라미터의위치는 @ModelAttribute Item item 다음에와야 한다
  * 왜냐하면 Model에 담기는 Item이 타입이 안맞으면 에러페이지로 그냥 넘어가는데,
  * BindingResult 를 뒤에 놔두면 BindingResult 에 에러 결과를 담아주므로,
  * **에러페이지가 아닌 현재 컨트롤러를 그대로 진행해준다.**

```java
public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    //검증 로직
    if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
    }
    /... 생략
```

<br>

**타임리프에서는??**

* #fields : #fields 로 BindingResult 가 제공하는 검증오류에접근할 수있다.
* th:errors : 해당필드에 오류가 있는 경우에 태그를 출력한다. th:if 의 편의버전이다. 
* th:errorclass : th:field 에서 지정한 필드에 오류가 있으면 class 정보를 추가한다.

* **글로벌 오류 처리**

```html
<div th:if="${#fields.hasGlobalErrors()}">
    <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="$ 
    {err}">전체 오류 메시지</p>
</div>
```

* **필드 오류 처리**

```html
<input type="text" id="itemName" th:field="*{itemName}"
th:errorclass="field-error" class="form-control" placeholder="이름을 
입력하세요">
<div class="field-error" th:errors="*{itemName}">
    상품명 오류 
</div>
```

<br><br>

### addItemV2 - 에러 및 입력값 제공

**앞에서 BindingResult 순서가 중요하다고 했음 -> `@ModelAttribute, BindingResult` 순서**

**타입오류확인**

* 숫자가 입력되어야 할곳에 문자를 입력해서 타입을 다르게해서 BindingResult 를 호출하고 bindingResult 의 값을 확인해보자.
* **에러내용이 담겨서 출력되고, 입력한 값도 사라진다. 이제 이것을 해결해보자!**

<br>

**FieldError 생성자 2가지 제공**

```java
public FieldError(String objectName, String field, String defaultMessage); 

public FieldError(String objectName, String field, @Nullable Object 
rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable 
Object[] arguments, @Nullable String defaultMessage)
```

<br>

**두번째 생성자의 rejectedValue 사용시 입력받은 값을 따로 기록해두기 때문에 에러가 발생해도 입력값이 사라지지 않는다!! -> 문제점 해결완료**

**예시 - 오류발생시 사용자 입력값유지**

```java
new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 
1,000,000 까지 허용합니다.")
```

<br>

**특히 타임리프 th:field는 알아서 이것을 반영!!**

<br><br>

## addItemV3 - 에러메시지 통합관리v1

**에러메시지도 messages 했던것처럼 따로 errors.properties 만들어서 사용하자!!(한번에 관리 목적)**

**그리고 codes 파라미터 부분에 에러메시지 보여주는 코드를 작성!! (따라서 기본값은 삭제)**

* 배열이라 메시지 여러개도 전송 가능

```java
//errors.propreties 내용 : range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
new FieldError("item", "price", item.getPrice(), false, new String[] 
{"range.item.price"}, new Object[]{1000, 1000000}
```

<br><br>

## addItemV4 - 에러메시지 통합관리v2

**addItemV3 에서 한 방식이 아니라 `rejectValue(), reject()` 를 사용해보자.**

**에러메시지는 범용성 있게 작성한 후 세밀하게 작성할 필요가 있을때만 세밀하게 작성하는 방식을 권장**

* 이를 스프링이 하기쉽게 "우선순위"를 제공 -> **MessageCodesResolver** 가 제공
* **DefaultMessageCodesResolver의기본메시지 생성규칙(상세->범용순)**
  * **객체 오류**의 경우 다음 순서로 2가지 생성
    * 1.: code + "." + object name  
      2.: code
    * 예) 오류 코드: required, object name: item   
      * 1.: required.item  
        2.: required
  * **필드 오류**의 경우 다음 순서로 4가지 메시지 코드 생성
    * 1.: code + "." + object name + "." + field  
      2.: code + "." + field  
      3.: code + "." + field type  
      4.: code
    * 예) 오류 코드: typeMismatch, object name "user", field "age", field type: int  
      * 1.: "typeMismatch.user.age"    
        2.: "typeMismatch.age"  
        3.: "typeMismatch.int"  
        4.: "typeMismatch"
  * **스프링은 타입관련한 오류는 `typeMismatch` 를 사용한다는걸 기억**

**수정된 코드**

```java
public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    /**
         * bject와 Target을 앞의 @ModelAttribute 덕분에 BindingResult도 알고있음
         * objectName=item //@ModelAttribute name
         * target=Item(id=null, itemName=상품, price=100, quantity=1234)
         */
    log.info("objectName={}", bindingResult.getObjectName());
    log.info("target={}", bindingResult.getTarget());

    if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.rejectValue("itemName", "required");
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
        bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9999) {
        bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
    }

    //특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
        }
    }
//    ... 생략
```

<br>

**errors.properties 예시**

```properties
#required.item.itemName=상품 이름은 필수입니다.
#range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
#max.item.quantity=수량은 최대 {0} 까지 허용합니다.
#totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}

#==ObjectError==
#Level1
totalPriceMin.item=상품의 가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}

#Level2 - 생략
totalPriceMin=전체 가격은 {0}원 이상이어야 합니다. 현재 값 = {1}


#==FieldError==
#Level1
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.

#Level2 - 생략

#Level3
required.java.lang.String = 필수 문자입니다.
required.java.lang.Integer = 필수 숫자입니다.
min.java.lang.String = {0} 이상의 문자를 입력해주세요.
min.java.lang.Integer = {0} 이상의 숫자를 입력해주세요.
range.java.lang.String = {0} ~ {1} 까지의 문자를 입력해주세요.
range.java.lang.Integer = {0} ~ {1} 까지의 숫자를 입력해주세요.
max.java.lang.String = {0} 까지의 숫자를 허용합니다.
max.java.lang.Integer = {0} 까지의 숫자를 허용합니다.

#Level4
required = 필수 값 입니다.
min= {0} 이상이어야 합니다.
range= {0} ~ {1} 범위를 허용합니다.
max= {0} 까지 허용합니다.

#추가
typeMismatch.java.lang.Integer=숫자를 입력해주세요.
typeMismatch=타입 오류입니다.
```

<br><br>

## Validator 분리

**컨트롤러에서 검증을 하니 너무 코드가 복잡해져서 "검증" 부분을 따로 분리해보자**

* `Validator` 인터페이스의 구현체를 생성해서 사용
* 그리고 @Component 로 "스프링빈" 에 등록!
* 이후 컨트롤러에서 이를 가져와 사용하는 구조!

<br>

이를 "글로벌" 설정할수도 있고, 해당 컨트롤러에서만 사용도 가능

* 필요할때 찾아보고, 굳이 잘 사용하지 않기에 넘어가겠음
* 애초에 여기서 "글로벌" 설정하면 "스프링 제공" 어노테이션이 사용 불가

<br><br>

# 2. "스프링" 제공 검증 어노테이션 사용

## 프로젝트 환경설정 & 생성

**이전에 한 프로젝트를 일부 수정했을 뿐**

<br><br>

## Bean Validation

**Gradle에 의존성 추가 필수!**

`implementation 'org.springframework.boot:spring-boot-starter-validation'`

**이때 스프링 부트는 자동으로 "글로벌" 로 LocalValidatorFactoryBean 를 등록 하기에 아래 검증 애노테이션이 동작!**

* 검증오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다. 
* 주의 할점은 꼭 기존 등록한 글로벌 Bean Vaildation이 있으면 제거

<br>

**검증 애노테이션**

* @NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않는다. 
* @NotNull : null 을 허용하지 않는다.
* @Range(min = 1000, max = 1000000) : 범위 안의 값이어야 한다. 
* @Max(9999) : 최대 9999까지만 허용한다.
* **[검증 애노테이션 모음](https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/ 
  html_single/#validator-defineconstraints-spec)**

<br>

**검증 애노테이션이 적용되려면 컨트롤러에서 @Valid나 @Validated 선언 필수!**

**검증 순서**

1. @ModelAttribute 각각의 필드에 **타입변환 시도**
   1. 성공하면 다음으로
   2. 실패하면 typeMismatch 로 FieldError 추가
2. Validator 적용

<br>

**BeanValidation 메시지 찾는순서**

1.  생성된 메시지코드 순서대로 messageSource 에서 메시지 찾기
2. 애노테이션의 message 속성사용 -> `@NotBlank(message = "공백! {0}")`
3.  라이브러리가 제공하는 기본값 사용 -> `공백일수 없습니다.`

<br>

**FieldError 경우 기존처럼 errors.properties 활용**

```properties
NotBlank.item.itemName=상품 이름을 적어주세요.
NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

<br>

**ObjectError 경우 @ScriptAssert() 사용 => 하지만 권장하지는 않음**

**따라서 배웠던 내용으로 하는것을 권장**

```java
public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    //특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
        }
    }
    // ... 생략
```

<br><br>

## Bean Validation 한계 해결방안

**Item을 수정할때 id값은 필수이고, quantity 값은 무제한으로 바꾸게 하고싶어서 id에 @NotNull 추가, quantity의 @Max(9999) 를 제거한다면??**

* 수정은 정상 동작한다!
* 단, "등록" 부분에서 문제가 발생한다.
  * "등록" 에서는 id가 존재하지않고, quantity도 9999로 제한이 안걸렸기 때문!
  * 참고) id에 왜 검증을 넣냐고 볼 수도 있지만, POSTMAN같은 툴로 충분히 악의적 접근이 가능하기 때문에 "최종 검증은 서버에서 진행하는 것이 안전"

<br>

**두가지 해결방안 존재**

* BeanValidation의 groups 기능을 사용한다.
* Item을 직접 사용하지않고, ItemSaveForm, ItemUpdateForm 같은 폼전송을 위한 별도의 모델 객체를 만들어서 사용한다.

<br>

### Bean Validation - groups

**@Valid가 아닌 @Validated 를 사용해야 groups 적용 가능**

```java
// (인터페이스) 저장용 SaveCheck.java
package hello.itemservice.domain.item; 
	public interface SaveCheck {
}
// (인터페이스) 수정용 UpdateCheck.java
package hello.itemservice.domain.item; 
	public interface UpdateCheck {
}
// Item.java
@Data
public class Item {
    @NotNull(groups = UpdateCheck.class) //수정시에만 적용 
    private Long id;
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}) 
    private String itemName;
}
// 컨트롤러 적용 - ValidationItemControllerV3.java
@PostMapping("/add")
public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, 
BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//...
}
```

<br>

### Form 전송 분리 - DTO 사용

**실무에서는 groups 를 잘 사용안하므로 이부분 내용을 사용하자!**

**참고로 등록 폼과 수정 폼을 하나의 html에서 개발하는건 많이 복잡하기 때문에 특별한 이유가 없다면 나누는것을 권장(addForm.html, editForm.html 이런식으로)**

<br>

**Form 전송 분리의 개념은 정말 간단하다. **

**Form 데이터를 Item 도메인으로 항상 해결했는데, 이것을 "등록용", "수정용" 으로 각각 DTO처럼 만들어서 "검증 애노테이션"을 적용하자는 개념이다!**

**동작과정 : `HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository`**

**DTO**

```java
// ItemSaveForm.java
@Data
public class ItemSaveForm {
    @NotBlank
    private String itemName; 
    @NotNull
    @Range(min = 1000, max = 1000000) 
    private Integer price;
    @NotNull
    @Max(value = 9999) 
    private Integer quantity;
}

// ItemUpdateForm.java
@Data
public class ItemUpdateForm {
    @NotNull 
    private Long id;
    @NotBlank
    private String itemName; 
    @NotNull
    @Range(min = 1000, max = 1000000) 
    private Integer price;
    //수정에서는 수량은 자유롭게 변경할 수 있다. 
    private Integer quantity;
}
```

<br>

**Controller 예시**

* **주의**
* @ModelAttribute("item") 에 item 이름을 넣어준 부분을 주의 
* 이것을넣지않으면 ItemSaveForm 의 경우 규칙에 의해 itemSaveForm 이라는 이름으로 MVC Model에 담기게 된다. 
* 이렇게되면 뷰템플릿에서 접근하는 th:object 이름도 함께 변경해주어야 한다.
* 물론 바꿔도 되지만 여기선 그냥 간단한 예제니까 뷰템플릿 수정을 하지않으려고 이렇게 선언하였다.

```java
@PostMapping("/{itemId}/edit")
public String edit(@PathVariable Long itemId, @Validated
@ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
    //특정 필드 예외가 아닌 전체 예외
    if (form.getPrice() != null && form.getQuantity() != null) { 
        int resultPrice = form.getPrice() * form.getQuantity(); 
        if (resultPrice < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
        } 
    }
    if (bindingResult.hasErrors()) {
        log.info("errors={}", bindingResult);
    	return "validation/v4/editForm"; 
    }
    // DTO -> Item으로 변환
    Item itemParam = new Item();
    itemParam.setItemName(form.getItemName());
    itemParam.setPrice(form.getPrice());
    itemParam.setQuantity(form.getQuantity());
    itemRepository.update(itemId, itemParam);
    
    return "redirect:/validation/v4/items/{itemId}"; 
}
```

<br><br>

## (API) Bean Validation - HTTP 메시지 컨버터

**이번엔 API 요청을 다루는 곳에서의 "검증" 개념을 이해해보자**

**참고**

* @ModelAttribute 는 HTTP 요청파라미터(URL 쿼리 스트링, POST Form)를 다룰때 사용한다. 
* @RequestBody 는 HTTP Body의 데이터를객체로 변환할 때사용한다. 주로 API JSON 요청을 다룰때
  사용한다.

<br>

**API의 경우 3가지 경우를 나누어 생각해야한다.**

* 성공요청: 성공
* 실패요청: JSON을 객체로 생성하는것 자체가 실패함
* 검증오류요청: JSON을 객체로 생성하는것은 성공했고, 검증에서 실패함

<br>

**@ModelAttribute vs @RequestBody**

* @ModelAttribute는 필드 단위로 세밀하게 적용되어서 Item 객체가 "검증" 전에 이미 만들어지기 때문에 "타입에러" 에도 "컨트롤러가 동작" 한다.

* 그러나 HttpMessageConverter 의 경우 객체 단위로 적용되어서 Item 객체가 "검증" 전에는 만들어지지 않기에 아예 "예외"가 발생한다.
  * 물론, "타입에러"가 아니면 정상 동작하고 "검증(예로 숫자범위)" 에서 처리된다.
  * 왜 "타입에러"를 강조했냐면, "타입에러"는 우리가 만든 "검증" 부분이 아닌 객체를 만들때 발생하는 에러이므로 다르기 때문이다.
  * **따라서 이 경우에는 이 부분을 나중에 "API 예외처리" 부분에 자세히 다룬다.**

<br><br>

# Folder Structure

생략..

