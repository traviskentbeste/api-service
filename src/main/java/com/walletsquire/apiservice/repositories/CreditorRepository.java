package com.walletsquire.apiservice.repositories;

import com.walletsquire.apiservice.entities.Creditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditorRepository extends JpaRepository<Creditor, Long> {
}
