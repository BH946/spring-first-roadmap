package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * HttpServlet 상속받아서 서블릿 작성 가능
 * 서블릿 호출시 -> Service 함수가 호출
 * 오버라이드 꿀팁 : Ctrl + O
 */
@WebServlet(name = "helloServlet", urlPatterns = "/hello") // url 을 hello 로 매핑
public class HelloServlet extends HttpServlet {

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("username"); // 쿼리 파라미터 조회 (ex : ?username=김)
        System.out.println("username = " + username);

        // 응답 response
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username);
    }
}
