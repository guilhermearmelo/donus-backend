package com.donus.backend.repository;

import com.donus.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByCpf(String cpf);
}
