package com.donus.backend.repository;

import com.donus.backend.domain.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface CostumerRepository extends JpaRepository<Costumer, Long> {
    Costumer findById(long id);
    Optional<Costumer> findByCpf(String cpf);
    @Modifying
    @Query(value="INSERT INTO tb_costumer_profiles (costumer_id, profiles_id) VALUES (?1,?2)", nativeQuery = true)
    void grantCostumerProfile(long userId, long profileId);
}