package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.dtos.ActivitySummaryCreditorDTO;
import com.walletsquire.apiservice.dtos.ActivitySummaryDebitorsDTO;
import com.walletsquire.apiservice.entities.Activity;
import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<ActivitySummaryDebitorsDTO> getDebitors(Activity activity) {

        // this is going to be the return object - so we'll build it up and then return it as updates from the database and applied buisiness logic
        List<ActivitySummaryDebitorsDTO> debitors = new ArrayList<>();

//        removeAllForsThatMatchBys(debitors, activity);
//        createDebitors(debitors, activity);

        return debitors;
    }


//    private void removeAllForsThatMatchBys(List<ActivitySummaryDebitorsDTO> debitors, Activity activity) {
//        int debug = 0;
//
//        // remove all fors that match the by's
//        List<PaidUsers> paidForsToRemove = new ArrayList<>();
//        for( PaidUsers paidFor : activity.getPaidFor().getPaidUsers()) {
//            if (debug == 1) {
//                System.out.printf("for %10s(%3d) -> %.2f\n", paidFor.getUser().getFirstName(), paidFor.getUser().getId(), paidFor.getAmount());
//            }
//            for( PaidUsers paidBy : activity.getPaidBy().getPaidUsers()) {
//                if (debug == 1) {
//                    System.out.printf("    - by %10s(%3d) -> %.2f\n", paidBy.getUser().getFirstName(), paidBy.getUser().getId(), paidBy.getAmount());
//                }
//                if (paidBy.getUser().getId().equals(paidFor.getUser().getId())) {
//                    paidBy.setAmount(paidBy.getAmount().subtract(paidFor.getAmount()));
//                    paidForsToRemove.add(paidFor);
//                }
//            }
//        }
//        activity.getPaidFor().getPaidUsers().removeAll(paidForsToRemove);
//
//    }
//
//    private void createDebitors(List<ActivitySummaryDebitorsDTO> debitors, Activity activity) {
//        int debug = 0;
//
//        for( PaidUsers paidFor : activity.getPaidFor().getPaidUsers()) {
//            if (debug == 1) {
//                System.out.printf("for %10s(%3d) -> %.2f\n", paidFor.getUser().getFirstName(), paidFor.getUser().getId(), paidFor.getAmount());
//            }
//            ActivitySummaryDebitorsDTO activitySummaryDebitorsDTO = new ActivitySummaryDebitorsDTO();
//            activitySummaryDebitorsDTO.setUser(paidFor.getUser().getId());
//            activitySummaryDebitorsDTO.setTotal(paidFor.getAmount());
//
//            for( PaidUsers paidBy : activity.getPaidBy().getPaidUsers()) {
//                if (debug == 1) {
//                    System.out.printf("    - by %10s(%3d) -> %.2f\n", paidBy.getUser().getFirstName(), paidBy.getUser().getId(), paidBy.getAmount());
//                }
//
//                ActivitySummaryCreditorDTO activitySummaryCreditorDTO = new ActivitySummaryCreditorDTO();
//
//                activitySummaryCreditorDTO.setUser(paidBy.getUser().getId());
//                if (debug == 1) {
//                    System.out.println("         amount(paidFor.amount) : " + paidFor.getAmount() + " divided by " + activity.getPaidBy().getPaidUsers().size());
//                }
//                activitySummaryCreditorDTO.setAmount(paidFor.getAmount().divide(BigDecimal.valueOf(activity.getPaidBy().getPaidUsers().size())));
//
//                activitySummaryDebitorsDTO.addCreditor(activitySummaryCreditorDTO);
//            }
//
//            debitors.add(activitySummaryDebitorsDTO);
//        }
//    }

}
