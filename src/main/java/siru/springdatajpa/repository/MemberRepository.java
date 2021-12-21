package siru.springdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import siru.springdatajpa.dto.MemberDto;
import siru.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new siru.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalByUsername(String username); // Optional

    @Query(value = "select m from Member m left join m.team t"
            , countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

}
