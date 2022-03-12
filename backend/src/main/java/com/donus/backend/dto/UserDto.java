package com.donus.backend.dto;

import com.donus.backend.domain.User;
import lombok.Data;

@Data
public class UserDto {

    private String name;
    private String cpf;

    public UserDto(){}

    public UserDto(User user) {
        this.name = user.getName();
        this.cpf = user.getCpf();
    }

}
