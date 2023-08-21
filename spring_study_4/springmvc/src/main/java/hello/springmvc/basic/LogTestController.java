package hello.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Slf4J 선언시 Logger log 선언할 필요없이 바로 사용가능
 */
//@Slf4j
@RestController // @Controller 는 String 반환시 뷰를 반환해서 @RestController 사용
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        log.trace(" trace my log="+ name);

        log.trace("trace log={}", name);

        log.debug("debug log={}", name);
        log.info(" info log={}", name);
        log.warn(" warn log={}", name);
        log.error("error log={}", name);
        
        

        return "ok";
    }
}
