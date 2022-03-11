package com.donus.backend.repository;

import com.donus.backend.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(long id);
    Account findByCode(String code);
}
