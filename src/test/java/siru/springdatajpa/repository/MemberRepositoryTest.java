package siru.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import siru.springdatajpa.dto.MemberDto;
import siru.springdatajpa.entity.Member;
import siru.springdatajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    void member_test() {
        // given
        Member member = new Member("memberA");

        // whenR
        Member savedMember = memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElseThrow();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        Member findMember1 = memberRepository.findById(member1.getId()).orElseThrow();
        Member findMember2 = memberRepository.findById(member2.getId()).orElseThrow();

        // then
        // -> 단건 조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // -> 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        // then
        assertThat(result.get(0).getUsername()).isEqualTo("memberA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByUsername("memberA");

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("memberA");
        assertThat(result.get(1).getUsername()).isEqualTo("memberA");
    }

    @Test
    void findUsernameList() {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<String> result = memberRepository.findUsernameList();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo("memberA");
        assertThat(result.get(1)).isEqualTo("memberB");
    }

    @Test
    void test_findMemberDto() {
        // given
        Member member1 = new Member("memberA", 10);
        memberRepository.save(member1);

        Team team = new Team("teamA");
        member1.setTeam(team);
        teamRepository.save(team);

        // when
        List<MemberDto> memberDtoList = memberRepository.findMemberDto();

        // then
        assertThat(memberDtoList.size()).isEqualTo(1);
        assertThat(memberDtoList.get(0).getUsername()).isEqualTo("memberA");
        assertThat(memberDtoList.get(0).getTeamname()).isEqualTo("teamA");

    }

    @Test
    void findByNames() {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByNames(Arrays.asList("memberA", "memberB"));

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("memberA");
        assertThat(result.get(1).getUsername()).isEqualTo("memberB");
    }

    @Test
    void returnType() {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> list = memberRepository.findListByUsername("memberA");
        Member memberResult = memberRepository.findMemberByUsername("memberA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("memberA");

        // then
    }

    @Test
    void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> members = page.getContent();

        // then
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(members.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void slicing() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);
        List<Member> members = page.getContent();

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        // -> 벌크연산 후에는 영속성 컨텍스트에 반영이 안되니 유의해야한다.

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void findMemberLazy() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 =new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findAll(); // -> LAZY_LOADING으로 인한 N+1문제발생 (Entity Graph로 해결)

        members.forEach(member -> {
            System.out.println("member = " + member.getUsername() + " team = " + member.getTeam());
        });

        // then
    }

    @Test
    void findMemberFetchJoin() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 =new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findMemberFetchJoin();

        members.forEach(member -> {
            System.out.println("member = " + member.getUsername() + " team = " + member.getTeam());
        });

        // then
    }

    @Test
    void queryHint() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush(); // -> 변경 감지 맟 sql 저장소의 쿼리를 DB로 날림 (QueryHint로 최적화)

        // then

    }
    @Test
    void lock() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        List<Member> result = memberRepository.findLockByUsername("member1");

        // then

    }


}