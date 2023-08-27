# Intro..

**스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술**

* 인프런 강의듣고 공부한 내용입니다.

<br>

해당 프로젝트 폴더는 강의를 수강 후 강의에서 진행한 프로젝트를 직접 따라 작성했습니다.

따로 강의 자료(pdf)를 주시기 때문에 필요할때 해당 자료를 이용할 것이고,

이곳 README.md 파일에는 기억할 내용들만 간략히 정리하겠습니다.

* **파일 업로드** - upload

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

# 참고

**HTTP 통신에 form 데이터를 file까지 함께 사용하기 위해서는 "멀티" 를 지원해주는 "multipart/form-data" 타입을 사용**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/885199a8-b660-4543-ad56-c34556690e21) 

<br>

**"제출할 여러타입 데이터들"**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/4c318095-bb67-4fca-882e-00f04838f5c6) 

<br>

**"멀티파트" 로 "파트별" 나눠서 데이터가 들어옴**

![image](https://github.com/BH946/spring-first-roadmap/assets/80165014/722da4b3-41b1-4f33-b553-f9aba490e9e3) 

<br>

**HTTP 요청 메시지 전부 보려면?**

```properties
logging.level.org.apache.coyote.http11=debug
```

<br><br>

# 1. 서블릿 파일 업로드

**업로드 사이즈 제한가능**

* `spring.servlet.multipart.max-file-size=1MB` : 파일 하나 최대사이즈, 기본 1MB
* `spring.servlet.multipart.max-request-size=10MB` : 전체 파일 합, 기본 10MB

<br>

**"파일 경로" 설정**

```properties
file.dir=/Users/kimyounghan/study/file/
```

<br>

**"컨트롤러"**

* 핵심
  * `Collection<Part> parts = request.getParts();` : form 데이터 파트별 접근
  * `part.write(fullPath);` : 파일 저장

```java
@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {
    @Value("${file.dir}") // application.properties 에 설정한 file.dir 값 주입
    private String fileDir;
    
    @GetMapping("/upload") // form 띄우기
    public String newFile() {
        return "upload-form"; 
    }
    
    @PostMapping("/upload") // 파일 업로드
    public String saveFileV1(HttpServletRequest request) throws 
        ServletException, IOException {
        log.info("request={}", request);
        String itemName = request.getParameter("itemName"); 
        log.info("itemName={}", itemName);
        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);
        for (Part part : parts) {
            log.info("==== PART ===="); 
            log.info("name={}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, 
                         part.getHeader(headerName));
            }
            //편의 메서드 - content-disposition; filename
            log.info("submittedFileName={}", part.getSubmittedFileName()); 
            log.info("size={}", part.getSize()); //part body size
            //데이터 읽기 - 바이너리 to 스트링엔 "인코딩(UTF_8 등) 필수"
            InputStream inputStream = part.getInputStream(); 
            String body = StreamUtils.copyToString(inputStream, 
                                                   StandardCharsets.UTF_8);
            log.info("body={}", body); // "데이터 확인용", 실제론 렉만 유발
            //파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName(); 
                log.info("파일 저장 fullPath={}", fullPath); 
                part.write(fullPath);
            } 
        }
        return "upload-form"; 
    }
}
```

<br>

**참고**

* 다음 옵션들은 끄는게 좋다. 렉 유발하므로.
  * `logging.level.org.apache.coyote.http11=debug`
  * `log.info("body={}", body);`
* (POST) upload 동작흐름 : 웹 브라우저 -> WAS(톰캣) -> upload 비지니스 로직 -> 파일 저장

<br><br>

# 2. 스프링 파일 업로드

**`MultipartFile` 지원**

```java
@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {
    @Value("${file.dir}") 
    private String fileDir;
    
    @GetMapping("/upload") 
    public String newFile() {
        return "upload-form"; 
    }
    
    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file, HttpServletRequest
                           request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);
        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename(); 
            log.info("파일 저장 fullPath={}", fullPath); 
            file.transferTo(new File(fullPath)); // 파일 저장
        }
        return "upload-form"; 
    }
}
```

<br><br>

# 3. (예제) 상품 - 파일 업로드, 다운로드

**예제로 구현해보며, "다운로드" 까지 추가해보자**

**요구사항**

* 상품을 관리
  * 상품이름 
  * 첨부파일 하나 
  * 이미지 파일 여러개
* 첨부파일을 업로드 다운로드할 수있다.
* 업로드한 이미지를 웹브라우저에서 확인할 수 있다.

<br>

**업로드, 다운로드 개발에 필요한 지식**

* **파일은 "스토리지"** 저장, 경로와 이름 등 정보**(EX: Item)는 "DB"** 저장
* **Item** - uploadFileName, storeFileName 는 필수 저장
  * uploadFileName(업로드 파일명), storeFileName(서버에 저장된 파일명) 둘 다 DB에 기록해놔야 함
  * 업로드 파일명들은 사람마다 중복될 수 있으며, 서버 파일명은 중복되면 안돼서 UUID 같은걸로 지정하기에 "둘 다 기록"
* **ItemForm** - Item의 Dto 용으로 만들어서 Form 데이터를 받는 도메인을 만들어줘야 함
  * 여기선 `MultipartFile` 타입을 사용해 데이터 받을거라 Item 에선 할 수 없기에 만들어줌
* **FileStore.java**
  * "스토리지" 에 저장하는 로직을 작성해서 "컨트롤러" 에서 사용
* **컨트롤러에서..**
  * `@GetMapping("/images/{filename}")` : \<img> 태그로 **이미지를 조회**할 때 사용
    * UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로 이미지 바이너리를 반환
    * 경로에 "file:" 을 넣어야 내부저장소 경로를 접근하는 것 (스토리지에 파일 있으니까!)
      * 이 부분을 통해 **"경로 설정" 을 꼭 해줘야 정상 접근**
  * `@GetMapping("/attach/{itemId}")` : **파일을 다운로드** 할때 실행
    * "/attach/{itemId}" - \<a> 태그 "href" 활용해 "파일명" 을 눌러서 접근하게 한 URL 경로
      * 파일 다운로드시 권한 체크같은 복잡한 상황까지 가정해서 이미지 id 를 요청하도록 함
    * 파일 다운이 되려면 반환할때 **"헤더" 가 필수**
    * 파일 다운로드시에는 고객이 업로드한 파일 이름으로 다운로드 하는게 좋다. 
      * Content-Disposition 해더에 `attachment; filename="업로드 파일명"`

<br><br>

## 3-1. 도메인 구현

**Item.java**

* UploadFile 중요

```java
@Data
public class Item {
    
    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
```

<br>

**UploadFile.java**

```java
@Data
public class UploadFile {
    
    private String uploadFileName; // EX : test.png
    private String storeFileName; // EX : 123-232-443sf-asdf....png

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
```

<br>

**ItemRepository.java**

* 현재 메모리에 저장코드 이지만, **실제론 DB에 저장하면 됨**

```java
@Repository
public class ItemRepository {

    private final Map<Long, Item> store = new HashMap<>();
    private long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }
}
```

<br><br>

## 3-2. 파일 저장로직 구현

**FileStore.java**

* "스토리지" 에 파일 저장 - 
  * MultipartFile 로 form 데이터 매우 쉽게 가져옴
  * MultipartFile 로 transferTo 로 간단히 파일 저장

```java
@Component // 스프링 빈 등록 -> 컨트롤러에 주입 목적
public class FileStore {
    // application.properties 에 등록한 file.dir 값 사용
    @Value("${file.dir}")
    private String fileDir;

    // "경로 + 파일 이름" 반환
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // "여러 파일 저장" - List<MultipartFile>
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    // "파일 저장" - MultipartFile
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName))); // "파일 진짜 저장"
        return new UploadFile(originalFilename, storeFileName); // 경로 반환
    }
    
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // ".png" 처럼 확장자를 구하기 위해
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
```

<br><br>

## 3-3. 컨트롤러 구현

**ItemForm.java**

* HTTP 전송되는 "Form 데이터" 용 Dto
* MultipartFile 타입 사용이 중요!

```java
@Data
public class ItemForm {

    private Long itemId;
    private String itemName;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
```

<br>

**ItemController.java**

* @GetMapping("/items/new") : 등록 폼을 보여줌 - item-form.html
* @PostMapping("/items/new") : 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트
* @GetMapping("/items/{id}") : 상품을 보여줌 - item-view.html
* @GetMapping("/images/{filename}") : \<img> 태그로 이미지를 조회할때 사용
* @GetMapping("/attach/{itemId}") : 파일을 다운로드 할때 실행

```java

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form"; // item-form.html
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        //스토리지에 저장
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile()); // 파일 1개
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles()); // 파일 여러개

        //데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        // return 에 {itemId} 위해 redirectAttributes 사용
        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}"; // 바로 아래 URL 로 접근
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view"; // item-view.html
    }

    
    // "이미지를 읽고" @ResponseBody 로 바이너리로 반환
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    // "파일 다운로드"
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        // "인코딩" 을 적용해야 "한글" 로 잘 저장
        // UriUtils.encode 는 정말 다양한 "인코딩 지원"
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); // 파일명 - 인코딩
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\""; // 파일명 - 헤더용

        // 반드시 "header 부분" 넣어야 "다운가능"
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
```

<br><br>

## 3-4. HTML (Thymeleaf)

**item-form.html**

* `multiple="multiple"` : 여러 파일이 가능

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>

<div class="container">

    <div class="py-5 text-center">
        <h2>상품 등록</h2>
    </div>

    <form th:action method="post" enctype="multipart/form-data">
        <ul>
            <li>상품명 <input type="text" name="itemName"></li>
            <li>첨부파일<input type="file" name="attachFile" ></li>
            <li>이미지 파일들<input type="file" multiple="multiple" name="imageFiles" ></li>
        </ul>
        <input type="submit"/>
    </form>

</div> <!-- /container -->
</body>
</html>
```

<br>

**item-view.html**

* `th:if` 로 값 있을때! 해당 태그 사용
* "첨부파일, img" 부분
  * "첨부파일" 부분 클릭시 다운로드로 넘어가게끔 작성
  * \<img> 태그 사용

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>

<div class="container">

    <div class="py-5 text-center">
        <h2>상품 조회</h2>
    </div>

    상품명: <span th:text="${item.itemName}">상품명</span><br/>
    첨부파일: <a th:if="${item.attachFile}" th:href="|/attach/${item.id}|" th:text="${item.getAttachFile().getUploadFileName()}" /><br/>
    <img th:each="imageFile : ${item.imageFiles}" th:src="|/images/${imageFile.getStoreFileName()}|" width="300" height="300"/>

</div> <!-- /container -->
</body>
</html>
```

<br><br>

# Folder Structure

생략..
