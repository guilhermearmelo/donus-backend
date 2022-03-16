package com.donus.backend.config.security;

import com.donus.backend.domain.Costumer;
import com.donus.backend.repository.CostumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private CostumerRepository costumerRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Optional<Costumer> costumer = costumerRepository.findByCpf(cpf);
        if(costumer.isPresent()){
            return costumer.get();
        }
        throw new UsernameNotFoundException("Dados inválidos!");
    }
}
