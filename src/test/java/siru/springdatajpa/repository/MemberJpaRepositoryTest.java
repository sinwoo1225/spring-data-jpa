package siru.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import siru.springdatajpa.entity.Member;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        // given
        Member member = new Member("memberA");

        // when
        Member savedMember = memberJpaRepository.save(member);

        // then
        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
}