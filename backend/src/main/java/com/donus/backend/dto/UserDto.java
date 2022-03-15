package com.donus.backend.dto;

import com.donus.backend.domain.User;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class UserDto {

    private String name;
    private String cpf;

    public UserDto(){}

    public UserDto(User user) {
        this.name = user.getName();
        this.cpf = user.getCpf();
    }

    public static Page<UserDto> converter(Page<User> userList) {
        return userList.map(UserDto::new);
    }
}
