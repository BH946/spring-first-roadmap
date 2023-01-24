package hello.hellospring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 순서가 컨트롤러 확인 -> 있으면 컨트롤러 관련 우선 수행 -> 없다면 바로 static꺼 수행(index.html도 static에있음)
 * 따라서 index.html이 무시되는것
 */

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
