package siru.springdatajpa.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    protected Member() {}

    public Member(String username) {
        this.username = username;
    }

}
