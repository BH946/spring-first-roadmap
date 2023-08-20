package hello.servlet.web.frontcontroller.v4;

import java.util.Map;


/**
 * MyView 나 ModelView 도 아닌 이번엔 String으로 반환값을 바꿨음.
 * => 개발자 편의를 위해서 수정한 것 (매번 ModelView를 반환하는것은 편리하지는 않기때문)
 */
public interface ControllerV4 {

    /**
     * model 은 주소로 넘어가니까 안에서 수정해도 잘 적용
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
