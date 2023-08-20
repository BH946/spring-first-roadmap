package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 레퍼지토리의 경우 domain에 분류되는것은 아니지만 하나밖에 없기에 그냥 다 여기에 모아두겠음.
 *
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    // static final로 싱글톤 적용
    private static final MemberRepository instance = new MemberRepository();
    public static MemberRepository getInstance() {
        return instance;
    }
    // private으로 생성자 접근 막기
    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence); // id 이때 할당
        store.put(member.getId(), member); // HashMap 제공 메소드 - put()
        return member;
    }
    public Member findById(Long id) {
        return store.get(id); // HashMap 제공 메소드 - get()
    }
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // store 모든값 ArrayList로 감싸기 -> store 안건드리려고
    }
    public void clearStore() {
        store.clear(); // HashMap 제공 메소드 - clear()
    }
}
