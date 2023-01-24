package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// MVC(Model View Controler) : 모델, 화면, 컨트롤러
// 스프링 부트는 컨트롤러가 있는지 우선 확인 => 즉 정적보다 컨트롤러가 우선순위 높음
// 없으면 static에 html 에 맞는게 있는지 확인 후 있으면 출력
// 컨트롤러가 있었다면 해당 return값의 파일명과 같은 html을 출력 => 이건 viewResolver가!
// @ResponseBody가 있으면 viewResolver가 아닌 HttpMessageConverter가 동작!

@Controller // controller 라고 알려줌
public class HelloController {

    // default
    @GetMapping("hello") // URL의 hello로 라우터 매핑(GET방식)
    public String hello(Model model) {
        model.addAttribute("data", "hello!!"); // model에 담아서 데이터 html에
        return "hello"; // 약속(라우터)
    }

    // static
    // 그냥 바로 html 만 사용

    // parameter
    @GetMapping("hello-mvc") // URL 매핑(GET)
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template"; // 해당 파일명을 실행(반환)
    }

    // API => 문자 반환
    // 이전엔 html 화면 구성이였다면, 여기는 그대로 입력 데이터 값을 반환
    @GetMapping("hello-string")
    @ResponseBody // viewResolver 사용 X
    public String helloString(@RequestParam("name") String name) { // ?name=이름
        return "hello " + name;
    }

    // API => 객체 반환
    @GetMapping("hello-api")
    @ResponseBody // viewResolver 사용 X
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; // => 자동 json으로 처리
    }

    // inner class
     static class Hello{ // Hello 객체 구조
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}