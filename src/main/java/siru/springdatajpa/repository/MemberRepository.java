package siru.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siru.springdatajpa.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
}