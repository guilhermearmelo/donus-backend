package com.donus.backend.dto;

import lombok.Data;

@Data
public class TokenDto {

    private String token;
    private String type;

    public TokenDto(String token, String type) {
        this.token = token;
        this.type = type;
    }
}
