package hello.hellospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//컨트롤러: 웹 MVC의 컨트롤러역할
//서비스: 핵심 비즈니스 로직구현
//리포지토리: 데이터베이스에접근, 도메인 객체를 DB에저장하고 관리
//도메인: 비즈니스도메인객체, 예) 회원, 주문, 쿠폰등등 주로데이터베이스에저장하고 관리됨

// 스프링빈 등록방법 2가지!!
//	1. 컴포넌트 스캔과자동의존관계설정 => 주석 처리 하겠음(@Controller, @Service, 등등 으로 표시한 방식을 컴포넌트 스캔이라 함)
//	2. 자바코드로직접 스프링빈등록하기 => 메모리 리포지토리를 다른 리포지토리로 변경할 예정이므로 이걸 사용(SpringConfig)
@SpringBootApplication // 톰캣 내장(서버)
public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}

}
