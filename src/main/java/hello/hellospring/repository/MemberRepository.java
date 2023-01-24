package hello.hellospring.repository;

// 아직 DB를 어떻게 설정할지 결정하지 않았으므로 구현체를 자유롭게 구현하기 쉽게
// 인터페이스로 구현한다.
// 또한 임시로 메모리를 DB로 사용하겠다.

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
