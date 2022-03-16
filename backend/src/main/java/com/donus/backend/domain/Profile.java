package com.donus.backend.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
public class Profile implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(insertable = true, updatable = true)
    private long id;

    @Column(insertable = true, updatable = true)
    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
