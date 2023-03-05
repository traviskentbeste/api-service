package com.walletsquire.apiservice.repositories;

import com.walletsquire.apiservice.entities.EventSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSummaryRepository extends JpaRepository<EventSummary, Long> {
}
