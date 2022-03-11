package com.donus.backend.service;

import com.donus.backend.domain.User;
import com.donus.backend.dto.UserDto;
import com.donus.backend.dto.UserInsertionDto;
import com.donus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto insert(UserInsertionDto userInsertionDto) {
        User user = this.parseDtoToEntity(userInsertionDto);

        return parseEntityToDto(userRepository.save(user));
    }

    public UserDto update(long id, UserDto userDto) {
        User user = userRepository.findById(id);

        if(userDto.getCpf() != null) user.setCpf(userDto.getCpf());
        if(userDto.getName() != null) user.setName(userDto.getName());

        return parseEntityToDto(userRepository.save(user));
    }

    public UserDto findById(long id) {
        User user = userRepository.findById(id);
        UserDto userDto = null;

        if(user != null){
            return new UserDto(user);
        }

        return null;
    }

    public List<UserDto> findAll() {
        List<User> userList;
        List<UserDto> userDtoList = new ArrayList<>();

        userList = userRepository.findAll();
        for(User user: userList) {
            UserDto userDto = this.parseEntityToDto(user);
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

    public void remove(long id) {
        User user = userRepository.findById(id);
        userRepository.delete(user);
    }

    public User parseDtoToEntity(UserDto userDto) {
        User user = new User(userDto.getName(), userDto.getCpf());

        return user;
    }

    public User parseDtoToEntity(UserInsertionDto userInsertionDto) {
        User user = new User(userInsertionDto.getName(), userInsertionDto.getCpf(), userInsertionDto.getPassword());

        return user;
    }

    public UserDto parseEntityToDto(User user){
        UserDto userDto = new UserDto(user);

        return userDto;
    }

}
