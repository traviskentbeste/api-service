package com.walletsquire.apiservice.repositories;

import com.walletsquire.apiservice.entities.Debitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitorRepository extends JpaRepository<Debitor, Long> {
}
