package com.donus.backend.service;

import com.donus.backend.domain.Account;
import com.donus.backend.domain.User;
import com.donus.backend.dto.BaseResponseDto;
import com.donus.backend.dto.UserDto;
import com.donus.backend.dto.UserInsertionDto;
import com.donus.backend.exceptions.CpfAlreadyExistsException;
import com.donus.backend.exceptions.CpfDoesntMatchPatternException;
import com.donus.backend.exceptions.NoFieldsToUpdateException;
import com.donus.backend.exceptions.UserNotFoundException;
import com.donus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> insert(UserInsertionDto userInsertionDto) {
        User user = this.parseDtoToEntity(userInsertionDto);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            Pattern p = Pattern.compile("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)");
            Matcher m = p.matcher((user.getCpf()));

            if(!m.matches())
                throw new CpfDoesntMatchPatternException();

            if(userRepository.findByCpf(user.getCpf()) != null )
                throw new CpfAlreadyExistsException();

            baseResponseDto.setData(parseEntityToDto(userRepository.save(user)));
            baseResponseDto.setMessage("User successfully inserted!");
            baseResponseDto.setStatusCode(HttpStatus.CREATED.value());

            return new ResponseEntity<>(baseResponseDto, HttpStatus.CREATED);
        } catch(CpfDoesntMatchPatternException e){
            baseResponseDto.setMessage(new CpfDoesntMatchPatternException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        } catch (CpfAlreadyExistsException e) {
            baseResponseDto.setMessage(new CpfAlreadyExistsException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<Object> update(long id, UserDto userDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(userRepository.findById(id) == null)
                throw new UserNotFoundException();

            if(userDto.getCpf() == null && userDto.getName() == null ||
                    Objects.equals(userDto.getCpf(), "") && Objects.equals(userDto.getName(), ""))
                throw new NoFieldsToUpdateException();

            Pattern p = Pattern.compile("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)");
            Matcher m = p.matcher((userDto.getCpf()));

            if(!m.matches())
                throw new CpfDoesntMatchPatternException();

            if(userRepository.findByCpf(userDto.getCpf()) != null )
                throw new CpfAlreadyExistsException();

            User user = userRepository.findById(id);
            if(userDto.getCpf() != null) user.setCpf(userDto.getCpf());
            if(userDto.getName() != null) user.setName(userDto.getName());

            baseResponseDto.setData(parseEntityToDto(userRepository.save(user)));
            baseResponseDto.setMessage("User successfully updated!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());

            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (UserNotFoundException e){
            baseResponseDto.setMessage(new UserNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        } catch (NoFieldsToUpdateException e) {
            baseResponseDto.setMessage(new NoFieldsToUpdateException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        } catch(CpfDoesntMatchPatternException e){
            baseResponseDto.setMessage(new CpfDoesntMatchPatternException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        } catch (CpfAlreadyExistsException e) {
            baseResponseDto.setMessage(new CpfAlreadyExistsException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<Object> findById(long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(userRepository.findById(id) == null)
                throw new UserNotFoundException();

            User user = userRepository.findById(id);

            baseResponseDto.setData(new UserDto(user));
            baseResponseDto.setMessage("User found!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (UserNotFoundException e){
            baseResponseDto.setMessage(new UserNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findAll(Pageable pageable) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        Page<User> userList = userRepository.findAll(pageable);
        baseResponseDto.setData(UserDto.converter(userList));

        if(userList.isEmpty()){
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            baseResponseDto.setMessage("No user was found!");
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        }

        baseResponseDto.setStatusCode(HttpStatus.OK.value());
        baseResponseDto.setMessage("Users found!");
        return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> remove(long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(userRepository.findById(id) == null)
                throw new UserNotFoundException();

            User user = userRepository.findById(id);
            userRepository.delete(user);

            baseResponseDto.setMessage("User successfully deleted!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            baseResponseDto.setMessage(new UserNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        }
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
