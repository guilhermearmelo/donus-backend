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

    @Column(name = "code", unique=true)
    private String code;

    @Column(name = "balance")
    private Double balance;

    @OneToOne
    @JoinColumn(name = "costumer_id", referencedColumnName = "id", unique=true)
    private Costumer costumer;

    @Column(name = "key")
    private String key;

    public Account(){}

    public Account(String code, Double balance){
        this.code = code;
        this.balance = balance;
    }

    public boolean hasMoney(Double amount){
        return amount <= this.getBalance();
    }

}
