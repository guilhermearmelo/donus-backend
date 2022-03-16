package com.donus.backend.controller;

import com.donus.backend.config.security.TokenService;
import com.donus.backend.domain.LoginForm;
import com.donus.backend.dto.BaseResponseDto;

import com.donus.backend.dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<BaseResponseDto> authenticate(@RequestBody @Validated LoginForm loginForm) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        UsernamePasswordAuthenticationToken loginData = loginForm.converter();

        try{
            Authentication authentication = authenticationManager.authenticate(loginData);
            String token = tokenService.generateToken(authentication);

            baseResponseDto.setData(new TokenDto(token, "Bearer"));
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            baseResponseDto.setMessage("Authentication Succeeded!");
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (AuthenticationException e){
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            baseResponseDto.setMessage("Authentication Error!");
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        }

    }
}
