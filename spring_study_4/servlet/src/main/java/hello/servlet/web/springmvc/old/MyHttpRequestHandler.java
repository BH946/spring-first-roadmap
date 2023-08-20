package hello.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 옛날 방식
 * HandlerMapping = BeanNameUrlHandlerMapping
 * HandlerAdapter = HttpRequestHandlerAdapter
 */
@Component("/springmvc/request-handler") // BeanNameUrlHandlerMapping
public class MyHttpRequestHandler implements HttpRequestHandler { // HttpRequestHandlerAdapter
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
