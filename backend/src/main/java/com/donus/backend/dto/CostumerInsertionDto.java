package com.donus.backend.dto;

import com.donus.backend.domain.Costumer;
import lombok.Data;

@Data
public class CostumerInsertionDto {

    private String name;
    private String cpf;
    private String password;

    public CostumerInsertionDto(){}

    public CostumerInsertionDto(Costumer costumer) {
        this.name = costumer.getName();
        this.cpf = costumer.getCpf();
        this.password = costumer.getPassword();
    }

}
