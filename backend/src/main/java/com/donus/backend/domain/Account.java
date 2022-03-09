package com.donus.backend.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="TB_ACCOUNT")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "balance")
    private Double balance;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

}
