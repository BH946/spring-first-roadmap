package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;


/**
 * MyView 가 아닌 ModelView 를 반환 -> 서블릿(request,response) 종속성 제거
 * 대신 MyView 는 프론트 컨트롤러에서 간단하게 뷰리졸버 개념을 도입해서 구현 -> 뷰네임 중복 제거
 */
public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
