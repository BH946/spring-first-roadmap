## Intro..

**모든 개발자를 위한 HTTP 웹 기본 지식**

* 인프런 강의

<br>

<br>

## 인터넷 통신과 기본 용어

**컴퓨터간의 통신에 인터넷을 사용하면 아래처럼 중간의 수많은 노드(컴퓨터.서버)를 거쳐서 통신 됨.**

![Image](https://github.com/user-attachments/assets/852845d2-480e-4381-b5bf-7cfa3d33f2af)  

<br>

**IP(인터넷 프로토콜)**은 주소 역할을 하고 **"패킷(Packet)" 통신 단위**로 데이터 전달

- IP는 "비연결성, 비신뢰성, 프로그램 구분 불가" 라는 한계가 있다. **-> 그래서 TCP가 등장**
- 특히 **UDP는 비연결이지만 IP와 다른 점은 "포트"**가 있어서 **프로그램 구분이 가능**하단 점이다.  
  또, **"체크섬"**도 있어서 **데이터 전달 검증**이 가능하다.

<br>

**TCP/IP 4계층의 모습 (OSI 7계층은 "역할"에 중점, 이 모델은 "프로토콜"에 중점)**

![image-20250301185216155](C:\Users\KoBongHun\Desktop\Git\Study\images\README\image-20250301185216155.png) 

![Image](https://github.com/user-attachments/assets/3ebed3f4-668e-4b90-94bb-fb2518dba898) 

![Image](https://github.com/user-attachments/assets/fcfe574c-1980-483d-a19e-518d41a48283) 

<br>

**IP 주소 "까먹음 + 변경"을 한방에 해결할 DNS 등장**

![Image](https://github.com/user-attachments/assets/196791d1-bfa0-4343-bdca-d5168ee26abe) 

<br>

<br>

## URI와 웹 브라우저 요청 흐름

**URN보다는 URL을 많이 사용하므로 URI와 URL을 비슷하게 부른다.**

![Image](https://github.com/user-attachments/assets/fb08b887-8d32-4b14-870b-807eca184632)

![Image](https://github.com/user-attachments/assets/e78544ca-4f87-4588-8157-214ade41de78) 

- URI : 인터넷 상 자원 식별 개념
- URL : 인터넷 상 자원의 **위치(Locator)**
- URN : 인터넷 상 자원의 **이름(Name)**

 <br>

**URL 문법 예시**

- 문법: scheme://[userinfo@]host\[:port]\[/path]\[?query][#fragment]

- 예시: https://www.google.com:443/search?q=hello&hl=ko
  - 프로토콜(https)
    - http, https, ftp 등
  - 호스트명(www.google.com)
    - 도메인명 또는 IP 주소 직접 사용 가능
  - 포트 번호(443)
    - http: 80
    - https: 443
  - 경로(/search)
  - 쿼리 파라미터(q=hello&hl=ko)
- **참고**
  - [userinfo@] 는 URL에 사용자정보를 포함해서 인증! => 거의 사용X
  - [#fragment] 는 html 내부 북마크 등에 사용. 즉, 서버에 전송하는 정보가 아님

<br>

**웹 브라우저 요청 흐름**

1. ![Image](https://github.com/user-attachments/assets/9eaf4066-d898-40e3-a684-81ee8767be8c) 
2. ![Image](https://github.com/user-attachments/assets/ad3b7672-4ca2-43e8-a569-61fabad2efd1)
3. ![Image](https://github.com/user-attachments/assets/b85ea1d6-a13b-4372-9381-02ed130fbfaa)  
4.  ![Image](https://github.com/user-attachments/assets/fe456c00-9eb2-4225-8456-affa59aa633e) 
5. ![Image](https://github.com/user-attachments/assets/e145fff1-9fa8-465f-a94e-23759f01ace9) 

<br>

<br>

## (중요)HTTP 정리 

**HTTP 메시지에 모든 것을 전송하다 보니 굉장히 중요하다. 지금은 HTTP 시대!**  
HTML, TEXT, IMAGE, 음성, 영상, 파일, JSON, XML 등 모든걸 다 전송 가능  
심지어 서버끼리도 TCP통신으로 따로 하는게 아닌 HTTP를 대부분 사용!

- HTTP/0.9 1991년: GET 메서드만 지원, HTTP 헤더X
- HTTP/1.0 1996년: 메서드, 헤더 추가
- **HTTP/1.1 1997년: 가장 많이 사용, 우리에게 가장 중요한 버전**
  - RFC2068 (1997) -> RFC2616 (1999) -> RFC7230~7235 (2014)
- HTTP/2 2015년: 성능 개선
- HTTP/3 진행중: TCP 대신에 UDP 사용, 성능 개선
  - TCP사용: HTTP/1.1, HTTP/2
  - UDP사용: HTTP/3

<br><br>

### HTTP 특징:

- 클라이언트 <-> 서버 구조로 통신 (우리가 잘 아는)
- **무상태 프로토콜(스테이트리스), 비연결성**
  - 상태 유지(Stateful): 거래 중간에 다른 점원으로 바뀌면 안된다.  
    (중간에 다른 점원으로 바뀔 때 상태 정보를 다른 점원에게 미리 알려줘야 한다.)
  - **무상태(Stateless)**: 거래 중간에 다른 점원으로 바뀌어도 된다.  
    (갑자기 고객이 증가해도 점원을 대거 투입할 수 있다.)
    - 갑자기 클라이언트 요청이 증가해도 서버를 대거 투입할 수 있다. (스케일아웃)
    - 무상태는 응답 서버를 쉽게 바꿀 수 있다. (에러 서버 교체 간단)
    - 단, 실무에서 어쩔 수 없이 "로그인"처럼 상태 유지를 해야하는 경우가 존재한다.
  - **비연결성(connectionless):** HTTP는 기본이 연결을 유지하지 않는 모델
    - 서버 자원을 매우 효율적으로 사용할 수 있음
    - 단, 3 way handshake 로 인해 연결이 느린데 **HTTP 지속 연결(Persistent Connections)로 문제 해결**
    - **HTTP 지속 연결(Persistent Connections) 전과 후 (0.9s -> 0.5s)**
    - ![Image](https://github.com/user-attachments/assets/88c6b5f8-2c73-4002-a104-fb33d2d6146e) ![Image](https://github.com/user-attachments/assets/d760d90f-d25e-4e0d-982a-57dc25335dec) 
- **HTTP 메시지**
  - 공식 스펙: https://tools.ietf.org/html/rfc7230#section-3
  - ![Image](https://github.com/user-attachments/assets/f4cc13cb-8bcd-4f4f-a810-a4a951271b20) 

<br><br>

### HTTP API 설계

**API URI 설계에는 "리소스"가 중요하다. "행위"는 메서드(get, post 등)로 구분하자.**  
**단, 실무에서는 행위를 URI에 작성해야 할 때도 좀 있는데 컨트롤 URI를 사용!**

- 만약 "미네랄을 캐라" 라는게 있으면 "미네랄"이 리소스임!  
  따라서 `/read/member/{id}` 이런식이 아니라 `/member/{id}` 이렇게 "리소스"를 나타낼 것!
  - 물론 실무에선 동작도 적기도 함. 배달주문->배달시작->배달완료 프로세스 일때:  
    `POST /orders/{orderId}/start-delivery` **(컨트롤 URI)**를 하기도 해. (배달시작이란 행위(동사)를 적은겨)
- 행위는 HTTP 메서드 **get, post, put, delete, patch**를 활용하자.
  - GET 은 **쿼리 파라미터(URI)**를 통해 데이터 전송 (바디는 지원하지 않는 곳이 많아 비권장)
  - POST 는 **메시지 바디**를 통해 데이터 전송
  - Put 은 리소스를 완전히 덮어씌운다. => POST와 차이점은 클라이언트가 리소스 위치를 아는 점!
    - `PUT /member/100` 이렇게 요청하면 100번째 member의 리소스를 아예 덮어씌움.  
      예로 member{"고", 25세} 100번째 데이터가 member{"김"} 로 **PUT 하면 member{"김"} 로 변경**
  - Patch는 리소스를 부분만 대체할 수 있다.
    - 예로 member{"고", 25세} 100번째 데이터가 member{"김"} 로 **Patch 하면 member{"김", 25세} 로 변경**
  - Delete는 리소스를 제거할 수 있다.
- HTTP 메서드의 속성: 안전, 멱등, 캐시가능
  - 안전(Safe): 호출해도 리소스를 변경하지 않는다. (GET으로 조회를 생각해봐라)
    - 로그같은게 쌓이는건? 그런건 고려 안한다.
  - 멱등(Idempotent): 몇 번을 호출하든 결과는 동일하다. **(POST만 멱등이 아니다!)**
    - POST 2번 호출하면 결제가 중복될 수 있다.
    - 물론, 중간에 다른 곳에서 리소스가 수정될 수 있는데 "멱등은 외부 요인 간섭까지 고려 안함"
  - 응답 결과 리소스를 캐시해서 사용해도 되는가? 가능! 근데, **GET만 대부분 사용.**

![Image](https://github.com/user-attachments/assets/2aea1b7f-33b8-49b9-b770-943463872444)

<br>

 **HTTP로 클라이언트 -> 서버 데이터 전송 4가지 상황 (참고: 전달 방식은 크게 2가지-쿼리 파라미터, 메시지 바디)**

1. **정적** 데이터 조회 -> 쿼리 파라미터 미사용
   - 이미지, 정적 텍스트 문서
2. **동적** 데이터 조회 -> 쿼리 파라미터 사용
   - 주로 검색, 게시판 목록에서 정렬 필터(검색어)

3. **HTML Form**을 통한 데이터 전송 -> GET은 쿼리 파라미터로, POST는 메시지 바디로 자동 작성!

   - 회원 가입, 상품 주문, 데이터 변경 

   - Content-Type: application/x-www-form-urlencoded
     - form의 내용을 메시지 바디를 통해서 전송(POST로 해보면 나옴)
     - 전송 데이터를 url encoding 처리
       - 예) abc김 -> abc%EA%B9%80

   - Content-Type: multipart/form-data
     - 파일 업로드 같은 바이너리 데이터 전송시 사용
     - 다른 종류의 여러 파일과 폼의 내용 함께 전송 가능(그래서 이름이 multipart)

4. **HTTP API**를 통한 데이터 전송 -> 이하 동문

   - 회원 가입, 상품 주문, 데이터 변경

   - 서버 to 서버, 앱 클라이언트, 웹 클라이언트(Ajax)

   - Content-Type: application/json

![Image](https://github.com/user-attachments/assets/62b9ff35-d602-497c-862b-e09435a7c40d) 

<br><br>

### HTTP API 설계 예시

**크게 2가지로 "컬렉션 기반"과 "스토어 기반"으로 나눠볼 수 있다. (대부분 컬렉션 방식 씀)**  
**HTML FORM 방식은 애초에 GET, POST만 지원한다. (PUT기반 아니니까 컬렉션이지)**

**1. API 설계 - 컬렉션 기반(POST)**

- 회원 목록 /members -> GET  
  회원 등록 /members -> POST  
  회원 조회 /members/{id} -> GET  
  **회원 수정 /members/{id} -> PATCH, PUT, POST**  
  회원 삭제 /members/{id} -> DELETE
  - **회원 수정에 개념적으론 PATCH 사용이 제일 좋다.**
- 클라이언트는 등록될 리소스의 URI를 모른다.
  - 회원 등록 /members -> POST
  - POST /members
- **서버가 새로 등록된 리소스 URI를 생성**해준다.
  - HTTP/1.1 201 Created   
    Location: /members/100
- 컬렉션(Collection)
  - 서버가 관리하는 리소스 디렉토리
  - 서버가 리소스의 URI를 생성하고 관리
  - 여기서 **컬렉션은 /members**

<br>

**2. API 설계 - 스토어 기반(PUT)**

- 파일 목록 /ﬁles -> GET  
  파일 조회 /ﬁles/{ﬁlename} -> GET  
  파일 등록 /ﬁles/{ﬁlename} -> PUT  
  파일 삭제 /ﬁles/{ﬁlename} -> DELETE  
  파일 대량 등록 /ﬁles -> POST
- 클라이언트가 리소스 URI를 알고 있어야 한다.
  - 파일 등록 /ﬁles/{ﬁlename} -> PUT
  - PUT /ﬁles/star.jpg
- **클라이언트가 직접 리소스의 URI를 지정**한다.
- 스토어(Store)
  - 클라이언트가 관리하는 리소스 저장소
  - 클라이언트가 리소스의 URI를 알고 관리
  - 여기서 **스토어는 /ﬁles**

<br>

**3. HTML FORM 사용 - GET, POST**

- 회원 목록     /members -> GET  
  **회원 등록 폼 /members/new -> GET**  
  **회원 등록     /members/new, /members -> POST**  
  회원 조회     /members/{id} -> GET  
  회원 수정 폼 /members/{id}/edit -> GET  
  회원 수정     /members/{id}/edit, /members/{id} -> POST  
  회원 삭제     /members/{id}/delete -> POST
  - URI 안바뀌는 /members/new 방식을 좀 더 선호하고, /members로 등록하는 사람도 있음.
  - GET, POST만 지원하니까 이런 제약을 해결하고자 동사를 사용한 **컨트롤 URI 방식도 많이 사용.**

<br>

**참고 문서: https://restfulapi.net/resource-naming**

- 문서(document)
  - 단일 개념(파일 하나, 객체 인스턴스, 데이터베이스 row)
  - 예) /members/100, /ﬁles/star.jpg
- 컬렉션(collection) -> **주로 이 방식만 접할거임.ㅇㅇ.**
  - 서버가 관리하는 리소스 디렉터리
  - 서버가 리소스의 URI를 생성하고 관리
  - 예) /members
- 스토어(store) 
  - 클라이언트가 관리하는 자원 저장소
  - 클라이언트가 리소스의 URI를 알고 관리
  - 예) /ﬁles
- 컨트롤러(controller), 컨트롤 URI
  - 문서, 컬렉션, 스토어로 해결하기 어려운 추가 프로세스 실행
  - 동사를 직접 사용
  - 예) /members/{id}/delete

<br><br>

### HTTP 헤더는?

**HTTP 헤더는 HTTP 전송에 필요한 모든 부가정보 가짐.**

![Image](https://github.com/user-attachments/assets/a6d904d8-75b8-44ef-b001-137fbf8be40d) 

<br>

**Entity 헤더가 "표현 헤더"로 변경 과정**

- 옛날 RFC2616 은 헤더 분류가 아래와 같음

  - General 헤더: 메시지 전체에 적용되는 정보, 예) Connection: close

  - Request 헤더: 요청 정보, 예) User-Agent: Mozilla/5.0 (Macintosh; ..)

  - Response 헤더: 응답 정보, 예) Server: Apache

  - Entity 헤더: 엔티티 바디 정보, 예) Content-Type: text/html, Content-Length: 3423

- 현재 RFC7230 은 엔티티를 "표현"이라는 단어로 바꿔 사용 함.

  - 엔티티(Entity) -> 표현(Representation)

  - Representation = representation Metadata + Representation Data  
    표현 = 표현 메타데이터 + 표현 데이터

  - **표현 헤더는 전송, 응답 둘다 사용**

    - Content-Type: 표현 데이터의 형식 - text/html; charset=utf-8

    - Content-Encoding: 표현 데이터의 압축 방식 - gzip

    - Content-Language: 표현 데이터의 자연 언어 - ko

    - Content-Length: 표현 데이터의 길이 - 바이트 단위

<br>

**협상 헤더는 요청시에만 사용**

- Accept: 클라이언트가 선호하는 미디어 타입 전달
- Accept-Charset: 클라이언트가 선호하는 문자 인코딩
- Accept-Encoding: 클라이언트가 선호하는 압축 인코딩
- Accept-Language: 클라이언트가 선호하는 자연 언어
- 협상(콘텐츠 네고시에이션): 클라이언트가 선호하는 표현 요청임. 우선순위는?
  - 협상과 우선순위1: Quality Values(q) 값 사용. 0~1이고 클수록 높은 우선순위 (생략은 1)
    - 예로 `Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7` 이런식 우선순위 보내면 "다중 언어 지원 서버"에서 처리!
  - 협상과 우선순위2: 구체적인 것이 우선한다.
    - 예로 `Accept: text/*, text/plain, text/plain;format=ﬂowed, */*` 이면 1등은 text/plain;format=flowed 임. 2등은 text/plain이고..
  - 협상과 우선순위3: 구체적인 것 기준으로 미디어 타입을 맞춘다. (미디어 타입: text/*, 등)
    - 예로 `Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5` 라면 text/html;level=3 의 경우 선언된거 없으니까 젤 미디어 타입 중 젤 구체적인 text/html 꺼를 사용해서 0.7 우선순위다.

<br>

**헤더 정보**

- **일반 정보**
  - From: 유저 에이전트의 이메일 정보 -> 잘 안씀.
  - Referer: 이전 웹 페이지 주소
  - User-Agent: 유저 에이전트 애플리케이션 정보
  - Server: 요청을 처리하는 오리진 서버의 소프트웨어 정보
    - origin 서버: 여러 프록시 서버들도 지날텐데 최종적으로 도달할 서버
  - Date: 메시지가 생성된 날짜
- **특별 정보**
  - Host: 요청한 호스트 정보(도메인) -> 필수.
    - 하나의 서버가 여러 도메인을 처리해야 할 때! 이걸로 구분.
  - Location: 페이지 리다이렉션
    - 웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면, Location 위치로 자동 이동 
      (리다이렉트)
  - Allow: 허용 가능한 HTTP 메서드
    - 405 (Method Not Allowed) 에서 응답에 포함해야함
    - Allow: GET, HEAD, PUT
  - Retry-After: 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간
    - 503 (Service Unavailable): 서비스가 언제까지 불능인지 알려줄 수 있음
    - Retry-After: Fri, 31 Dec 1999 23:59:59 GMT (날짜 표기)
    - Retry-After: 120 (초단위 표기)
- **인증**
  - Authorization: 클라이언트 인증 정보를 서버에 전달
    - Authorization: Basic xxxxxxxxxxxxxxxx
  - WWW-Authenticate: 리소스 접근시 필요한 인증 방법 정의
    - 401 Unauthorized 응답과 함께 사용
    - WWW-Authenticate: Newauth realm="apps", type=1, title="Login to \"apps\"", Basic realm="simple"
- **쿠키**
  - Set-Cookie: **서버에서 클라이언트로** 쿠키 전달(응답)  
    - 예) set-cookie: sessionId=abcde1234; expires=Sat, 26-Dec-2020 00:00:00 GMT; path=/; domain=.google.com; Secure
  - Cookie: 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청시 **서버로 전달**
    - 사용처
      - 사용자 로그인 세션 관리
      - 광고 정보 트래킹
    - 쿠키 정보는 항상 서버에 전송됨
      - 네트워크 트래픽 추가 유발
      - 최소한의 정보만 사용(세션 id, 인증 토큰)
      - 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹 스토리지 (localStorage, sessionStorage) 참고
    - 주의!
      - 보안에 민감한 데이터는 저장하면 안됨(주민번호, 신용카드 번호 등등)
  - 쿠키 생명주기 설정 가능, 원하는 도메인에 적용 가능, 원하는 경로에만 쿠키 접근 가능, 보안도 있음(HttpOnly 쓰면 JS에서 접근 불가)
    - Expires, max-age
    - Domain
    - Path
    - Secure, HttpOnly, SameSite

<br><br>

### HTTP 헤더 - 캐시

**캐시 사용하는건 당장 웹페지에 F12-네트워크에서 확인할 수 있다.**  
캐시 사용한건 회색 텍스트임을 알자. 또한, 새로고침해보면 304 Not Modified도 잘 나온다.

일반적으로 브라우저 캐시 사용하면 처음 데이터 다운 이후부터 굉장히 시간을 단축할 수 있다. 왜냐하면 아래 그림처럼 브라우저 캐시에서 데이터를 바로 가져오기 때문이다. **(캐시 유효 시간만 검증. 캐시 덕분에 캐시 가능 시간동안 네트워크를 사용하지 않아도 된다.)**

![Image](https://github.com/user-attachments/assets/a9446d81-f7da-4d5e-a269-0f15914aacb5) 

<br>

그런데, 생각해보면 유효시간이 만료되더라도 서버에서 데이터가 수정된게 없다면 브라우저 캐시 데이터를 그대로 사용하는게 훨씬 효과적이다. **이를 위해 "검증 헤더와 조건부 요청"을 사용한다.** (물론 **서버와 검증할 통신 시간은 있음.** 데이터 전송(=메시지 바디부분)은 아니라서 매우 짧은 시간!)  
**=> 즉, 서버 데이터 갱신되지 않으면 "304 Not Modiﬁed + 헤더 메타 정보만 응답(바디X)"**

**검증 헤더 2가지 (Validator)** 

- ETag: "v1.0", ETag: "asid93jkrh2l" 
  - 이름을 지정(태그)해서 검증. 한꺼번에 이름 수정하기도 수월.

- Last-Modiﬁed: Thu, 04 Jun 2020 07:19:24 GMT
  - 날짜 기반(수정시간)의 로직 사용. 만약 **서버 데이터 수정할 일이 거의 없는 경우는 ETag가 좋지.**

**조건부 요청 헤더**

- If-Match, If-None-Match: ETag 값 사용

- If-Modiﬁed-Since, If-Unmodiﬁed-Since: Last-Modiﬁed 값 사용

<br>

**아래는 캐시 유효시간 만료되었는데 304응답으로 캐시 사용하는 모습 (검증 헤더 2가지 ver)**

![Image](https://github.com/user-attachments/assets/cbdf9c36-abea-43c4-812b-7a6a0defbf44) 

![Image](https://github.com/user-attachments/assets/6a34c13b-1f8b-4796-8be4-cf7b0ab41bf0) 

<br>

**캐시 제어 헤더**

- Cache-Control: 캐시 제어
  - Cache-Control: max-age
    - 캐시 유효 시간, 초 단위
  - Cache-Control: no-cache
    - 데이터는 캐시해도 되지만, 항상 원(origin) 서버에 검증하고 사용
    - **이름에 주의!!** 원 서버 검증하면 로컬 브라우저 캐시 사용할 수 있음.
  - Cache-Control: no-store
    - 데이터에 민감한 정보가 있으므로 저장하면 안됨   
      (메모리에서 사용하고 최대한 빨리 삭제. 하드디스크에 저장 안하게!)
- Pragma: 캐시 제어(하위 호환) -> 옛날..
- Expires: 캐시 유효 기간(하위 호환) -> 옛날..

<br><br>

### HTTP 헤더 - 캐시 무효화? 프록시 캐시?

**여기서 프록시 캐시를 도입한걸 현대에선 AWS CloudFront의 CDN서비스로 볼 수 있음.**

![Image](https://github.com/user-attachments/assets/424374f0-88be-4803-bf94-1cb573478d82) 

- Cache-Control: public 
  - 응답이 public 캐시에 저장되어도 됨
- Cache-Control: private 
  - 응답이 해당 사용자만을 위한 것임, private 캐시에 저장해야 함(기본값)
- Cache-Control: s-maxage 
  - 프록시 캐시에만 적용되는 max-age
- Age: 60 (HTTP 헤더)
  - 오리진 서버에서 응답 후 프록시 캐시 내에 머문 시간(초)

<br>

**확실한 캐시 무효화 응답:**

- Cache-Control: no-cache, no-store, must-revalidate 
- Pragma: no-cache
  - HTTP 1.0 하위 호환까지 필요하다면 ㅇㅇ.

![Image](https://github.com/user-attachments/assets/c8125f27-deac-4b7f-825a-721244c8e95e) 

![Image](https://github.com/user-attachments/assets/8a92def0-58eb-40d6-bcdc-ec1374e62007) 

![Image](https://github.com/user-attachments/assets/9a44ddf3-b75b-4f79-803e-89c45def4446) 

<br>

<br>

## Folder Structure

생략..
