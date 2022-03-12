package com.donus.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponseDto implements Serializable {

    private Object data;
    private String message;
    private Integer statusCode;

}
