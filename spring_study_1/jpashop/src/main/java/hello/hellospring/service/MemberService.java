package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//@Service // 스프링 빈 등록
@Transactional
public class MemberService {
    // final 은 상수
    // private final MemberRepository memberRepository = new MemoryMemberRepository();

    // DI(외부 의존성) 가능하게 변경
    private final MemberRepository memberRepository;

    // @Autowired 생략 가능(생성자 1개), 객체 생성 시점에 스프링 컨테이너에서 해당 스프링 빈을 찾아서 주입
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 검증
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * ifPresent() : Optional 객체가 값을 가지고 있다면 true, 값이 없다면 false 리턴
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                   throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
