package com.donus.backend.controller;

import com.donus.backend.dto.UserDto;
import com.donus.backend.dto.UserInsertionDto;
import com.donus.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
@CrossOrigin(origins="*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    @ApiOperation(value = "Inserts one user")
    public ResponseEntity<Object> insertUser(@RequestBody UserInsertionDto userInsertionDto) {
        return userService.insert(userInsertionDto);
    }

    @GetMapping("/users")
    @ApiOperation(value = "Returns a list of all users")
    public ResponseEntity<Object> listUsers(){
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Returns a user given an id")
    public ResponseEntity<Object> listUserById(@PathVariable(value="id") long id){
        return userService.findById(id);
    }

    @PutMapping("/user/{id}")
    @ApiOperation(value = "Updates a user given an id")
    public ResponseEntity<Object> updateUserById(@PathVariable(value="id") long id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "Removes a user given an id")
    public ResponseEntity<Object> deleteUserById(@PathVariable(value="id") long id) {
        return userService.remove(id);
    }

}
