package hello.hellospring;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 자바 코드로 스프링빈 등록하는 방법이다.
 */
@Configuration // 직접 스프링 빈을 등록하기 위한 클래스임을 알림
public class SpringConfig {
    // DataSource는 데이터베이스 커넥션(연결)을 가지며 빈으로 등록하여 인자로 넘겨준다.
    // => 스프링이 제공해줌
    private final DataSource dataSource;
    private final EntityManager em;
    private final MemberRepository memberRepository;

    public SpringConfig(DataSource dataSource, EntityManager em, MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.em = em;
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() throws SQLException {
//        return new MemberService(memberRepository());
        return new MemberService(memberRepository);
    }

    @Bean
    public MemberRepository memberRepository() throws SQLException {
//    return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }


    // AOP를 여기에 스프링빈 등록하며 관리하기도하고, 간단한건 그냥
    // TimeTraceAop.java 여기서 @Component로 바로 빈에 등록하기도 한다.
//    @Bean
//    public TimeTraceAop timeTraceAop() {
//        return new TimeTraceAop();
//    }
}