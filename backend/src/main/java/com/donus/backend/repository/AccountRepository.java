package com.donus.backend.repository;

import com.donus.backend.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(long id);
    Account findByCode(String code);
    @Query(value="SELECT * FROM tb_account WHERE tb_account.costumer_id=?1", nativeQuery = true)
    Account findByCostumer(long id);
}
