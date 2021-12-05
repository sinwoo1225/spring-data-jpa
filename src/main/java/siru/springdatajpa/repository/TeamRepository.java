package siru.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import siru.springdatajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
