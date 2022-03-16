package com.donus.backend.dto;

import com.donus.backend.domain.Costumer;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class CostumerDto {

    private String name;
    private String cpf;

    public CostumerDto(){}

    public CostumerDto(Costumer costumer) {
        this.name = costumer.getName();
        this.cpf = costumer.getCpf();
    }

    public static Page<CostumerDto> converter(Page<Costumer> userList) {
        return userList.map(CostumerDto::new);
    }
}
