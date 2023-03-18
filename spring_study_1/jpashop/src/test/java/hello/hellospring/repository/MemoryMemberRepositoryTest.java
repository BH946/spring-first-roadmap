package hello.hellospring.repository;

// junit 라이브러리 이용해서 test 코드 작성

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    /**
     * 테스트는 함수 순서가 자기맘대로 실행하기 때문에, 다른 테스트한내용을 지워주는 형식이 필요
     */
    @AfterEach // 테스트 이후 진행하므로 이 때 저장된 데이터 clear
    public void afterEach() {
        repository.clearStore();
    }

    /**
     * 보통 given -> when -> then 방식으로 테스트
     * assert로 결과확인 (안동일하면 오류, 동일하면 아무일도 없음)
     */
    @Test
    public void save() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        repository.save(member);

        // then
        Member result = repository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findByName() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // when
        Member result = repository.findByName("spring1").get();

        // then
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // when
        List<Member> result = repository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2); // size가 2일테니까 2와 비교

    }


}
