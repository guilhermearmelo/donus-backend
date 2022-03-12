package com.donus.backend.dto;

import com.donus.backend.domain.User;
import lombok.Data;

@Data
public class UserInsertionDto {

    private String name;
    private String cpf;
    private String password;

    public UserInsertionDto(){}

    public UserInsertionDto(User user) {
        this.name = user.getName();
        this.cpf = user.getCpf();
        this.password = user.getPassword();
    }

}
