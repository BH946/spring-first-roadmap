package hello.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 옛날 방식
 * HandlerMapping = BeanNameUrlHandlerMapping
 * HandlerAdapter = SimpleControllerHandlerAdapter -> 우리가 먼저한 servlet mvc v5와 유사
 *
 * 뷰리졸버는 Bean..과 Internal...(JSP처리) 2가지가 있는데, 여기서는 JSP로 처리해주는 Internal...을 사용
 */
@Component("/springmvc/old-controller") // BeanNameUrlHandlerMapping
public class OldController implements Controller { // SimpleControllerHandlerAdapter

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form"); // 이부분이 스프링이 제공한 뷰리졸버를 사용한 개념
    }
}
