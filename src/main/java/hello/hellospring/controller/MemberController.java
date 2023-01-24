package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 회원컨트롤러가 회원서비스와회원 리포지토리를 사용할수 있게 의존관계를 준비하자.
 * 즉, 컨트롤러 -> 서비스 -> 레퍼지토리 구조를 만들려함
 */

// 자바 코드로 직접 스프링 빈 등록할때도 Controller는 컨포넌트 스캔으로 올라가기 때문에 @Controller를 추가해야 한다.
@Controller
public class MemberController {
    private final MemberService memberService;

    /**
     * @Autowired 사용시 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다 함.(DI)
     * 생성자 1개인 경우 생략 가능
     */
    @Autowired //멤버 컨트롤러가 생성이 될떄 스프링빈에 등록되어있는 멤버서비스를 주입
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // GET 방식
    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }
    // POST 방식! => createMemberForm.html 에서 form으로 데이터 오는거 매핑함
    @PostMapping(value = "/members/new")
    public String create(MemberForm form) { // 참고로 html에서 넘어온 form데이터에서 속성을 name으로 MeberForm과 똑같이 줘서 자동으로 MemberForm에 값 기록됨
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}