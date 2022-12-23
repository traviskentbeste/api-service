package com.walletsquire.apiservice.repositories;

import com.walletsquire.apiservice.entities.PaidUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaidUsersRepository extends JpaRepository<PaidUsers, Long> {
}
