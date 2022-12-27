package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Activity;
import com.walletsquire.apiservice.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity create(Activity activity) {

        return activityRepository.save(activity);

    }

    public Optional<Activity> getById(Long id) {

        return activityRepository.findById(id);

    }

    public List<Activity> getAll() {

        return activityRepository.findAll();

    }

    public Activity update(Activity activity, Long id) {

        Optional<Activity> activityOptional = activityRepository.findById(id);

        if (! activityOptional.isPresent()) {
            return null;
        }

        Activity existingActivity = activityOptional.get();

        if ( (activity.getAmount() != null) ) {
            existingActivity.setAmount(activity.getAmount());
        }
        if ( (activity.getDescription() != null) && (! activity.getDescription().isEmpty()) ) {
            existingActivity.setDescription(activity.getDescription());
        }
        if ( (activity.getName() != null) && (! activity.getName().isEmpty()) ) {
            existingActivity.setName(activity.getName());
        }
        if ( (activity.getSplitType() != null) && (! activity.getSplitType().isEmpty()) ) {
            existingActivity.setSplitType(activity.getSplitType());
        }
        if ( (activity.getAddress() != null) ) {
            existingActivity.setAddress(activity.getAddress());
        }
        if ( (activity.getCategory() != null) ) {
            existingActivity.setCategory(activity.getCategory());
        }
        if ( (activity.getCurrency() != null) ) {
            existingActivity.setCurrency(activity.getCurrency());
        }
        if ( (activity.getEvent() != null) ) {
            existingActivity.setEvent(activity.getEvent());
        }
        if ( (activity.getPaidBy() != null) ) {
            existingActivity.setPaidBy(activity.getPaidBy());
        }
        if ( (activity.getPaidFor() != null) ) {
            existingActivity.setPaidFor(activity.getPaidFor());
        }
    
        return activityRepository.save(existingActivity);

    }

    public void delete(Activity activity) {

        Optional<Activity> activityOptional = activityRepository.findById(activity.getId());

        if (activityOptional.isPresent()) {
            activityRepository.delete(activityOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Activity> activityOptional = activityRepository.findById(id);

        if (activityOptional.isPresent()) {
            activityRepository.delete(activityOptional.get());
        }

    }

}
