package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.dtos.ActivitySummaryCreditorDTO;
import com.walletsquire.apiservice.dtos.ActivitySummaryDebitorsDTO;
import com.walletsquire.apiservice.entities.Activity;
import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.mappers.PaidMapper;
import com.walletsquire.apiservice.mappers.UserMapper;
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

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaidMapper paidMapper;

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
        int debug = 0;

        // this is going to be the return object - so we'll build it up and then return it as updates from the database and applied buisiness logic
        List<ActivitySummaryDebitorsDTO> debitors = new ArrayList<>();

        removeAllForsThatMatchBys(debitors, activity);
        if (debug == 1) {
            System.out.println("--------------------------------------------------------------------------------");
        }
        createDebitorsFromRemaining(debitors, activity);

        return debitors;
    }


    private void removeAllForsThatMatchBys(List<ActivitySummaryDebitorsDTO> debitors, Activity activity) {
        int debug = 0;

        // remove all fors that match the by's
        List<PaidUsers> paidForsToRemove = new ArrayList<>();
        for( PaidUsers paidFor : activity.getPaidFor().getPaidUsers() ) {
            if (debug == 1) {
                System.out.printf("removeAllForsThatMatchBys   - for %10s(%3d)\n", paidFor.getUser().getFirstName(), paidFor.getUser().getId());
            }
            for( PaidUsers paidBy : activity.getPaidBy().getPaidUsers() ) {
                if (debug == 1) {
                    System.out.printf("removeAllForsThatMatchBys   -     - (%3d) - by %10s(%3d)\n", paidFor.getUser().getId(), paidBy.getUser().getFirstName(), paidBy.getUser().getId());
                }
                if (paidBy.getUser().getId().equals(paidFor.getUser().getId())) {
                    if (debug == 1) {
                        System.out.printf("removeAllForsThatMatchBys   -             - removing %d because paidBy <=> paidFor\n", paidFor.getUser().getId());
                    }
                    paidBy.setAmount(paidBy.getAmount().subtract(paidFor.getAmount()));
                    paidForsToRemove.add(paidFor);
                }
            }
        }
        activity.getPaidFor().getPaidUsers().removeAll(paidForsToRemove);

    }

    private void createDebitorsFromRemaining(List<ActivitySummaryDebitorsDTO> debitors, Activity activity) {
        int debug = 0;

        int paidByPaidUsersSize = activity.getPaidBy().getPaidUsers().size();

        for( PaidUsers paidFor : activity.getPaidFor().getPaidUsers() ) {
            if (debug == 1) {
                System.out.printf("createDebitorsFromRemaining - for %10s(%3d) -> %.2f\n", paidFor.getUser().getFirstName(), paidFor.getUser().getId(), paidFor.getAmount());
            }

            // create the summaryDebtorsDTO
            ActivitySummaryDebitorsDTO activitySummaryDebitorsDTO = new ActivitySummaryDebitorsDTO();
            activitySummaryDebitorsDTO.setUser(userMapper.toDto(paidFor.getUser()));
            activitySummaryDebitorsDTO.setTotal(paidFor.getAmount());

            for( PaidUsers paidBy : activity.getPaidBy().getPaidUsers() ) {

                if (debug == 1) {
                    System.out.printf("createDebitorsFromRemaining -     - (%3d) - by %10s(%3d) - %5.2f\n", paidFor.getUser().getId(), paidBy.getUser().getFirstName(), paidBy.getUser().getId(), paidFor.getAmount().divide(BigDecimal.valueOf(paidByPaidUsersSize)));
                }

                // create the summaryCreditorDTO
                ActivitySummaryCreditorDTO activitySummaryCreditorDTO = new ActivitySummaryCreditorDTO();
                activitySummaryCreditorDTO.setUser(userMapper.toDto(paidBy.getUser()));
                activitySummaryCreditorDTO.setAmount(paidFor.getAmount().divide(BigDecimal.valueOf(paidByPaidUsersSize)));

                // add creditor
                activitySummaryDebitorsDTO.addCreditor(activitySummaryCreditorDTO);
            }

            debitors.add(activitySummaryDebitorsDTO);
        }
    }

}
