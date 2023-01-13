package com.walletsquire.apiservice.repositories;

import com.walletsquire.apiservice.entities.Activity;
import com.walletsquire.apiservice.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> getActivitiesByEvent(Event event);

}
