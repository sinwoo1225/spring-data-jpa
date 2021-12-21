package siru.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import siru.springdatajpa.dto.MemberDto;
import siru.springdatajpa.entity.Member;
import siru.springdatajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

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
}