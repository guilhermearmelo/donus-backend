package com.donus.backend.domain;

import com.donus.backend.dto.UserDto;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="TB_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "password")
    private String password;

    public User(){}

    public User(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    public User(String name, String cpf, String password) {
        this.name = name;
        this.cpf = cpf;
        this.password = password;
    }

}
