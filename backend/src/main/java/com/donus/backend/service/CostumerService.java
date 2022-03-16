package com.donus.backend.service;

import com.donus.backend.domain.Costumer;
import com.donus.backend.dto.BaseResponseDto;
import com.donus.backend.dto.CostumerDto;
import com.donus.backend.dto.CostumerInsertionDto;
import com.donus.backend.exceptions.CpfAlreadyExistsException;
import com.donus.backend.exceptions.CpfDoesntMatchPatternException;
import com.donus.backend.exceptions.NoFieldsToUpdateException;
import com.donus.backend.exceptions.CostumerNotFoundException;
import com.donus.backend.repository.CostumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CostumerService {

    @Autowired
    private CostumerRepository costumerRepository;

    public ResponseEntity<Object> insert(CostumerInsertionDto costumerInsertionDto) {
        Costumer costumer = this.parseDtoToEntity(costumerInsertionDto);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            Pattern p = Pattern.compile("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)");
            Matcher m = p.matcher((costumer.getCpf()));

            if(!m.matches())
                throw new CpfDoesntMatchPatternException();

            if(costumerRepository.findByCpf(costumer.getCpf()).isPresent())
                throw new CpfAlreadyExistsException();

            costumer.setPassword(new BCryptPasswordEncoder().encode(costumer.getPassword()));
            baseResponseDto.setData(parseEntityToDto(costumerRepository.save(costumer)));
            costumerRepository.grantCostumerProfile(costumerRepository.findByCpf(costumerInsertionDto.getCpf()).get().getId(), 2);

            baseResponseDto.setMessage("Costumer successfully inserted!");
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

    public ResponseEntity<Object> update(long id, CostumerDto costumerDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(costumerRepository.findById(id) == null)
                throw new CostumerNotFoundException();

            if(costumerDto.getCpf() == null && costumerDto.getName() == null ||
                    Objects.equals(costumerDto.getCpf(), "") && Objects.equals(costumerDto.getName(), ""))
                throw new NoFieldsToUpdateException();

            Pattern p = Pattern.compile("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)");
            Matcher m = p.matcher((costumerDto.getCpf()));

            if(!m.matches())
                throw new CpfDoesntMatchPatternException();

            if(costumerRepository.findByCpf(costumerDto.getCpf()).isPresent())
                throw new CpfAlreadyExistsException();

            Costumer costumer = costumerRepository.findById(id);
            if(costumerDto.getCpf() != null) costumer.setCpf(costumerDto.getCpf());
            if(costumerDto.getName() != null) costumer.setName(costumerDto.getName());

            baseResponseDto.setData(parseEntityToDto(costumerRepository.save(costumer)));
            baseResponseDto.setMessage("Costumer successfully updated!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());

            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (CostumerNotFoundException e){
            baseResponseDto.setMessage(new CostumerNotFoundException().getMessage());
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
            if(costumerRepository.findById(id) == null)
                throw new CostumerNotFoundException();

            Costumer costumer = costumerRepository.findById(id);

            baseResponseDto.setData(new CostumerDto(costumer));
            baseResponseDto.setMessage("Costumer found!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (CostumerNotFoundException e){
            baseResponseDto.setMessage(new CostumerNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findAll(Pageable pageable) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        Page<Costumer> userList = costumerRepository.findAll(pageable);
        baseResponseDto.setData(CostumerDto.converter(userList));

        if(userList.isEmpty()){
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            baseResponseDto.setMessage("No costumer was found!");
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        }

        baseResponseDto.setStatusCode(HttpStatus.OK.value());
        baseResponseDto.setMessage("Costumers found!");
        return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> remove(long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(costumerRepository.findById(id) == null)
                throw new CostumerNotFoundException();

            Costumer costumer = costumerRepository.findById(id);
            costumerRepository.delete(costumer);

            baseResponseDto.setMessage("Costumer successfully deleted!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (CostumerNotFoundException e) {
            baseResponseDto.setMessage(new CostumerNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    public Costumer parseDtoToEntity(CostumerDto costumerDto) {

        return new Costumer(costumerDto.getName(), costumerDto.getCpf());
    }

    public Costumer parseDtoToEntity(CostumerInsertionDto costumerInsertionDto) {

        return new Costumer(costumerInsertionDto.getName(), costumerInsertionDto.getCpf(), costumerInsertionDto.getPassword());
    }

    public CostumerDto parseEntityToDto(Costumer costumer){

        return new CostumerDto(costumer);
    }

}
