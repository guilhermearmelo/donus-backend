package com.donus.backend.controller;

import com.donus.backend.dto.CostumerDto;
import com.donus.backend.dto.CostumerInsertionDto;
import com.donus.backend.service.CostumerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api")
public class CostumerController {

    @Autowired
    CostumerService costumerService;

    @PostMapping("/costumer")
    @CacheEvict(value="costumersList", allEntries=true)
    @ApiOperation(value = "Inserts one costumer")
    public ResponseEntity<Object> insertCostumer(@RequestBody CostumerInsertionDto costumerInsertionDto) {
        return costumerService.insert(costumerInsertionDto);
    }

    @GetMapping("/costumers")
    @Cacheable(value = "costumersList")
    @ApiOperation(value = "Returns a list of all costumers")
    public ResponseEntity<Object> listCostumers(@PageableDefault(sort="id", direction= Sort.Direction.DESC, page=0, size=10) Pageable pageable){
        return costumerService.findAll(pageable);
    }

    @GetMapping("/costumer/{id}")
    @ApiOperation(value = "Returns a costumer given an id")
    public ResponseEntity<Object> listCostumerById(@PathVariable(value="id") long id){
        return costumerService.findById(id);
    }

    @PutMapping("/costumer/{id}")
    @CacheEvict(value="costumersList", allEntries=true)
    @ApiOperation(value = "Updates a costumer given an id")
    public ResponseEntity<Object> updateCostumerById(@PathVariable(value="id") long id, @RequestBody CostumerDto costumerDto) {
        return costumerService.update(id, costumerDto);
    }

    @DeleteMapping("/costumer/{id}")
    @CacheEvict(value="costumersList", allEntries=true)
    @ApiOperation(value = "Removes a costumer given an id")
    public ResponseEntity<Object> deleteCostumerById(@PathVariable(value="id") long id) {
        return costumerService.remove(id);
    }

}
