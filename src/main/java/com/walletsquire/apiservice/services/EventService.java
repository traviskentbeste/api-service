package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.dtos.ActivitySummaryCreditorDTO;
import com.walletsquire.apiservice.dtos.ActivitySummaryDebitorsDTO;
import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.*;
import com.walletsquire.apiservice.repositories.ActivityRepository;
import com.walletsquire.apiservice.repositories.AddressRepository;
import com.walletsquire.apiservice.repositories.CurrencyRepository;
import com.walletsquire.apiservice.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityRepository activityRepository;

    public Event create(Event event) {

        return eventRepository.save(event);
    }

    public Optional<Event> getById(Long id) {

        return eventRepository.findById(id);

    }

    public List<Event> getAll() {

        return eventRepository.findAll();

    }

    public Event update(Event event, Long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (! eventOptional.isPresent()) {
            return null;
        }

        Event existingEvent = eventOptional.get();

        if ( (event.getName() != null) && (! event.getName().isEmpty()) ) {
            existingEvent.setName(event.getName());
        }
        if ( (event.getDescription() != null) && (! event.getDescription().isEmpty()) ) {
            existingEvent.setDescription(event.getDescription());
        }
        if ( (event.getEndDatetimestamp() != null) ) {
            existingEvent.setEndDatetimestamp(event.getEndDatetimestamp());
        }
        if ( (event.getStartDatetimestamp() != null) ) {
            existingEvent.setStartDatetimestamp(event.getStartDatetimestamp());
        }
        if ( (event.getAddress() != null) ) {
            existingEvent.setAddress(event.getAddress());
        }
        if ( (event.getCurrency() != null) ) {
            existingEvent.setCurrency(event.getCurrency());
        }
    
        return eventRepository.save(existingEvent);

    }

    public void delete(Event event) {

        Optional<Event> eventOptional = eventRepository.findById(event.getId());

        if (eventOptional.isPresent()) {
            eventRepository.delete(eventOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isPresent()) {
            eventRepository.delete(eventOptional.get());
        }

    }

    public List<Activity> getAllActivities(Event event) {

        return activityRepository.getActivitiesByEvent(event);

    }

    public void combineActivityDebitors(List<ActivitySummaryDebitorsDTO> debitors, List<ActivitySummaryDebitorsDTO> activityDebitors) {
        int debug = 1;

        for( ActivitySummaryDebitorsDTO debitor : activityDebitors) {
            if (debug == 1) {
                System.out.println("debitor : " + debitor.getUser());
            }

            for (ActivitySummaryCreditorDTO creditor : debitor.getCreditors()) {
                if (debug == 1) {
                    System.out.println("  - creditor : " + creditor.getUser() + " amount : " + creditor.getAmount());
                }
                addToDebitors(debitors, debitor.getUser(), creditor.getUser(), creditor.getAmount());
            }
        }
    }

    private int forwardFound(List<ActivitySummaryDebitorsDTO> debitors, UserDTO debitorUser, UserDTO creditorUser) {
        int found = 0;

        for( ActivitySummaryDebitorsDTO debitor : debitors ) {
            if (found == 0) {
                if (debitor.getUser().getId().equals(debitorUser.getId())) {
                    found++;
                    for ( ActivitySummaryCreditorDTO creditor : debitor.getCreditors() ) {
                        if (found == 1) {
                            if (creditorUser.getId().equals(creditor.getUser().getId())) {
                                found++;
                            }
                        }
                    }
                }
            }
        }

        return found;
    }

    // TODO: combine this with forwardFound into just a Found function
    private int reverseFound(List<ActivitySummaryDebitorsDTO> debitors, UserDTO debitorUser, UserDTO creditorUser) {
        int found = 0;

        for( ActivitySummaryDebitorsDTO debitor : debitors ) {
            if (found == 0) {
                if (debitor.getUser().getId().equals(creditorUser.getId())) {
                    found++;
                    for ( ActivitySummaryCreditorDTO creditor : debitor.getCreditors() ) {
                        if (found == 1) {
                            if (debitorUser.getId().equals(creditor.getUser().getId())) {
                                found++;
                            }
                        }
                    }
                }
            }
        }

        return found;
    }


    private void createCreditorAndDebitorWithAmountAndAddToDebitors(List<ActivitySummaryDebitorsDTO> debitors, UserDTO creditor, UserDTO debitor, BigDecimal amount) {
        int debug = 1;

        ActivitySummaryDebitorsDTO activitySummaryDebitorsDTO = new ActivitySummaryDebitorsDTO();
        activitySummaryDebitorsDTO.setTotal(amount);
        activitySummaryDebitorsDTO.setUser(debitor);

        ActivitySummaryCreditorDTO activitySummaryCreditorDTO = new ActivitySummaryCreditorDTO();
        activitySummaryCreditorDTO.setUser(creditor);
        activitySummaryCreditorDTO.setAmount(amount);

        activitySummaryDebitorsDTO.addCreditor(activitySummaryCreditorDTO);

        if (debug == 1) {
            System.out.println("adding : " + activitySummaryDebitorsDTO);
        }

        debitors.add(activitySummaryDebitorsDTO);

    }

    private void addCreditorWithAmountToExistingDebitorinDebitors(List<ActivitySummaryDebitorsDTO> debitors, UserDTO creditor, UserDTO debitor, BigDecimal amount) {
        int debug = 0;

        ActivitySummaryCreditorDTO activitySummaryCreditorDTO = new ActivitySummaryCreditorDTO();
        activitySummaryCreditorDTO.setUser(creditor);
        activitySummaryCreditorDTO.setAmount(amount);

        for(ActivitySummaryDebitorsDTO loopDebitor : debitors) {
            if (loopDebitor.getUser().equals(debitor)) {
                if (debug == 1) {
                    System.out.println("            - adding entire object to " + debitor);
                }
                loopDebitor.setTotal(loopDebitor.getTotal().add(amount));
                loopDebitor.addCreditor(activitySummaryCreditorDTO);
            }
        }
    }

    private void updateCreditorAmountForDebitorInDebitors(List<ActivitySummaryDebitorsDTO> debitors, UserDTO creditor, UserDTO debitor, BigDecimal amount) {
        int debug = 0;

        for(ActivitySummaryDebitorsDTO debitorLoop : debitors) {

            if (debug == 1) { System.out.println("debitorLoop : " + debitorLoop); }
            if (debitorLoop.getUser().equals(debitor)) {

                for (ActivitySummaryCreditorDTO creditorLoop : debitorLoop.getCreditors()) {

                    if (creditorLoop.getUser().equals(creditor)) {

                        // update the amount owed to this creditor
                        creditorLoop.setAmount(creditorLoop.getAmount().add(amount));

                        // also update the total
                        debitorLoop.setTotal(debitorLoop.getTotal().add(amount));
                    }

                }

            }

        }
    }


    private void addToDebitors(List<ActivitySummaryDebitorsDTO> debitors, UserDTO debitorUser, UserDTO creditorUser, BigDecimal amount) {
        int debug = 1;

        int forwardFound = forwardFound(debitors, debitorUser, creditorUser);
        int reverseFound = reverseFound(debitors, debitorUser, creditorUser);

        // reverse found takes precedence
        if (reverseFound == 2) {

            if (debug == 1) {
                System.out.println("    * working on canceling out");
            }

            // concurrency issues:
            // 1.  (okay) - we need to delete a creditor from a debitor, we can do that outside the creditor loop
            // 2.  (okay) -  we need to update amounts for creditor - no issues here
            // 3.  we need to add a creditor
            // 3.1. possibly remove this creditor and add a debitor

            // concurrency issue #3 and #3.1, rv < 0
            ActivitySummaryDebitorsDTO debitorToRemove = null;
            ActivitySummaryDebitorsDTO debitorToAdd = null;

            for(ActivitySummaryDebitorsDTO debitorLoop : debitors) {
                System.out.println("        debitorLoop : " + debitorLoop);
                if (debitorLoop.getUser().equals(creditorUser)) {

                    System.out.printf("%s(creditor) owes %s(debitor) : %.2f\n ", creditorUser.getFirstName(), debitorLoop.getUser().getFirstName(), amount);

                    // currency issue #1, rv = 0
                    ActivitySummaryCreditorDTO creditorToDelete = null;

                    // concurrency issue #3 and #3.1, rv < 0
                    ActivitySummaryCreditorDTO creditorToAdd = null;

                    for (ActivitySummaryCreditorDTO creditorLoop : debitorLoop.getCreditors()) {
                        System.out.println("            creditorLoop : " + creditorLoop);
                        if (creditorLoop.getUser().equals(debitorUser)) {

                            System.out.println("                okay - we need to figure out how much to remove here..." + amount);
                            int rv = creditorLoop.getAmount().compareTo(amount);

                            if ( rv == 0 ) {

                                System.out.println("                    they're equal - remove this entry");
                                System.out.println("                    removing creditorLoop : " + creditorLoop);

                                // reduce the amount
                                debitorLoop.setTotal(debitorLoop.getTotal().subtract(amount));

                                // set that we need to remove this one
                                creditorToDelete = creditorLoop;

                            }
                            else if ( rv > 0 ) {

                                System.out.println("                    it's more, we'll just reduce the amount and total");

                                // reduce the amount
                                creditorLoop.setAmount(creditorLoop.getAmount().subtract(amount));

                                // reduce the total
                                debitorLoop.setTotal(debitorLoop.getTotal().subtract(amount));

                            }
                            else if ( rv < 0 ) {

                                System.out.println("                    it's less, we'll need to remove this node and add another (or add to an existing)");

                                int subFound = forwardFound(debitors, debitorUser, creditorUser);
                                System.out.println("subFound : " + subFound);

                                if (subFound == 1) {
                                    // add

                                    ActivitySummaryCreditorDTO newCreditor = new ActivitySummaryCreditorDTO();
                                    newCreditor.setUser(creditorUser);
                                    newCreditor.setAmount(amount.subtract(creditorLoop.getAmount()));

                                    creditorToAdd = newCreditor;

                                } else if (subFound == 0) {
                                    // create

                                    BigDecimal recalculatedAmount = amount.subtract(creditorLoop.getAmount());

                                    ActivitySummaryCreditorDTO newCreditor = new ActivitySummaryCreditorDTO();
                                    newCreditor.setUser(creditorUser);
                                    newCreditor.setAmount(recalculatedAmount);

                                    List<ActivitySummaryCreditorDTO> creditors = new ArrayList<>();
                                    creditors.add(newCreditor);

                                    ActivitySummaryDebitorsDTO newDebitor = new ActivitySummaryDebitorsDTO();
                                    newDebitor.setUser(debitorUser);
                                    newDebitor.setTotal(recalculatedAmount);
                                    newDebitor.setCreditors(creditors);

                                    debitorToAdd = newDebitor;

                                    debitorToRemove = debitorLoop;
                                }
                            }

                        }
                    }

                    // remove creditor outsize the iteration
                    if (creditorToDelete != null) {
                        System.out.println("deleting creditor");

                        debitorLoop.getCreditors().remove(creditorToDelete);

                        // if there are no more creditors, then remove
                        if (debitorLoop.getCreditors().size() == 0) {
                            debitorToRemove = debitorLoop;
                        }

                    }

                    if (creditorToAdd != null) {
                        System.out.println("adding creditor");

                        // add creditor
                        debitorLoop.addCreditor(creditorToAdd);

                        // update amount
                        debitorLoop.setTotal(debitorLoop.getTotal().add(amount));
                    }


                }
            }

            // remove/add the debitor outside the iteration
            if ( (debitorToRemove != null) && (debitorToAdd != null) ) {
                debitors.remove(debitorToRemove);

                debitors.add(debitorToAdd);
            } else if (debitorToRemove != null) {

                // this is the use case where there was a cancel out
                debitors.remove(debitorToRemove);

            }

        } else if (forwardFound == 0) {

            if (debug == 1) { System.out.println("    * adding because not forwardFound"); }
            createCreditorAndDebitorWithAmountAndAddToDebitors(debitors, creditorUser, debitorUser, amount);

        } else if (forwardFound > 0) {

            if (debug == 1) { System.out.println("      # forwardFound : " + forwardFound); }

            if (forwardFound == 1) {

                if (debug == 1) { System.out.println("         - add object to : " + debitorUser); }
                addCreditorWithAmountToExistingDebitorinDebitors(debitors, creditorUser, debitorUser,amount);

            } else if (forwardFound == 2) {

                if (debug == 1) { System.out.println("         - update the amount (" + amount + ") for debitor : " + debitorUser + " and creditor : " + creditorUser); }
                updateCreditorAmountForDebitorInDebitors(debitors, creditorUser, debitorUser, amount);

            }

        }


        // output the current debitors
        if (debug == 1) {
            System.out.println("--------------------");
            System.out.println("resulting array of debtors and their creditors");
            for (ActivitySummaryDebitorsDTO debitorLoop : debitors) {
                System.out.println("    * : " + debitorLoop);
            }
            System.out.println("----------------------------------------");
        }
    }

}
