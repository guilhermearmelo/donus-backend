package com.donus.backend.controller;

import com.donus.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api")
@CrossOrigin(origins="*")
public class AccountController {

    @Autowired
    AccountService accountService;
}
