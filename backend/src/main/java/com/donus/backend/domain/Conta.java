package com.donus.backend.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="TB_CONTA")
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "saldo")
    private String saldo;

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

}
