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
                addCreditorToDebitorInDebitors(debitors, debitor.getUser(), creditor.getUser(), creditor.getAmount());
            }
        }
    }

    private int forwardFound(List<ActivitySummaryDebitorsDTO> debitors, UserDTO debitorUser, UserDTO creditorUser) {
        int found = 0;
        int debug = 0;

        for( ActivitySummaryDebitorsDTO debitor : debitors ) {
            if (debug == 1) {
                System.out.println("debitor : " + debitor.getUser().getFirstName());
            }
            if (found == 0) {
                if (debitor.getUser().getId().equals(debitorUser.getId())) {
                    found++;
                    if (debug == 1) {
                        System.out.println("found is : " + found);
                    }
                    for ( ActivitySummaryCreditorDTO creditor : debitor.getCreditors() ) {
                        if (debug == 1) {
                            System.out.println("    creditor : " + creditor.getUser().getFirstName());
                        }
                        if (found == 1) {
                            if (creditorUser.getId().equals(creditor.getUser().getId())) {
                                found++;
                                if (debug == 1) {
                                    System.out.println("found is : " + found);
                                }
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

        if (debug == 1) {
            System.out.println("        createCreditorAndDebitorWithAmountAndAddToDebitors : creditor : " + creditor);
            System.out.println("        createCreditorAndDebitorWithAmountAndAddToDebitors : debitor  : " + debitor);
            System.out.println("        createCreditorAndDebitorWithAmountAndAddToDebitors : amount   : " + amount);
        }

        ActivitySummaryDebitorsDTO activitySummaryDebitorsDTO = new ActivitySummaryDebitorsDTO();
        activitySummaryDebitorsDTO.setTotal(amount);
        activitySummaryDebitorsDTO.setUser(debitor);

        ActivitySummaryCreditorDTO activitySummaryCreditorDTO = new ActivitySummaryCreditorDTO();
        activitySummaryCreditorDTO.setAmount(amount);
        activitySummaryCreditorDTO.setUser(creditor);

        activitySummaryDebitorsDTO.addCreditor(activitySummaryCreditorDTO);

        if (debug == 1) {
            System.out.println("        createCreditorAndDebitorWithAmountAndAddToDebitors : result   : " + activitySummaryDebitorsDTO);
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


    private void addCreditorToDebitorInDebitors(List<ActivitySummaryDebitorsDTO> debitors, UserDTO debitorUser, UserDTO creditorUser, BigDecimal amount) {
        int debug = 1;

        System.out.println("    debitorUser  - person who owes    : " + debitorUser.getFirstName());
        System.out.println("    creditorUser - person who is owed : " + creditorUser.getFirstName());
        System.out.println("    amount                            : " + amount);
        System.out.println("    in plain english                  : " + debitorUser.getFirstName() + " owes " + creditorUser.getFirstName() + " " + amount);
        System.out.println("----------------------------------------");

        int reverseFound = reverseFound(debitors, debitorUser, creditorUser);
        int forwardFound = forwardFound(debitors, debitorUser, creditorUser);

        // reverse found takes precedence, the two means that the forward was found AND the reverse was found
        if (reverseFound == 2) {

            if (debug == 1) {
                System.out.println("    CCCCC : canceling out scenario, reverse match found");
            }

            System.out.println("    looking for the reverse entry");

            int removeDebitorLoopBecauseEmpty = 0;
            int reverseEntryFound = 0;
            ActivitySummaryDebitorsDTO debitorToRemove = null;
            ActivitySummaryCreditorDTO creditorToRemove = null;
            UserDTO debitorUserToAdd = null;
            UserDTO creditorUserToAdd = null;
            BigDecimal createAmount = null;
            BigDecimal addAmount = null;

            for(ActivitySummaryDebitorsDTO debitorLoop : debitors) {

                if (reverseEntryFound == 0) {
                    System.out.println("        debitorLoop : " + debitorLoop.getUser().getFirstName() + ", debitorLoop compared to " + creditorUser.getFirstName() + ", creditor");

                    if (debitorLoop.getUser().equals(creditorUser)) {

                        for (ActivitySummaryCreditorDTO creditorLoop : debitorLoop.getCreditors()) {
                            if (reverseEntryFound == 0) {
                                System.out.println("            creditorLoop : " + creditorLoop.getUser().getFirstName() + ", creditorLoop compared to " + debitorUser.getFirstName() + ", debitor");
                                if (creditorLoop.getUser().equals(debitorUser)) {
                                    System.out.println("            ***** reverse entry found!");
                                    reverseEntryFound = 1;

                                    int rv = creditorLoop.getAmount().compareTo(amount);
                                    System.out.println("            compare creditorLoop " + creditorLoop.getAmount() + ":y, to incoming amount of " + amount + ":x,  is : " + rv);
                                    System.out.println("            x: " + creditorLoop.getAmount());
                                    System.out.println("            y : " + amount);

                                    if ( amount.compareTo(creditorLoop.getAmount()) > 0) {
                                        System.out.println("            y > x");

                                        System.out.println("            creditorToRemove : " + creditorLoop);
                                        creditorToRemove = creditorLoop;

                                        int found = forwardFound(debitors, creditorLoop.getUser(), debitorLoop.getUser());
                                        if (found == 2) {
                                            System.out.println("                found = 2 | increment A by (y-x)");
                                        } else if (found == 1) {
                                            System.out.println("                found = 1 | add incomming creditorLoop A:" + creditorLoop.getUser().getFirstName() + " to debitorLoop B:" + debitorLoop.getUser().getFirstName() + " with amount (y-x):" + amount.subtract(creditorLoop.getAmount()));

                                            creditorUserToAdd = creditorLoop.getUser();
                                            addAmount = amount.subtract(creditorLoop.getAmount());

                                        } else if (found == 0) {
                                            System.out.println("                found = 0 | create B:" + debitorLoop.getUser() + " -> A:" + creditorLoop.getUser() + " with amount (y-x)");

                                            debitorUserToAdd = debitorLoop.getUser();
                                            creditorUserToAdd = creditorLoop.getUser();
                                            createAmount = amount.subtract(creditorLoop.getAmount());

                                        }

                                    } else if ( amount.compareTo(creditorLoop.getAmount()) < 0) {
                                        System.out.println("            y < x");

                                        System.out.println("total : " + debitorLoop.getTotal());

                                        // now update the total, order is important here
                                        debitorLoop.setTotal(debitorLoop.getTotal().subtract(amount));

                                        // NOW we can update the creditorLoop amount
                                        creditorLoop.setAmount(creditorLoop.getAmount().subtract(amount));

                                    } else if ( creditorLoop.getAmount().compareTo(amount) == 0) {
                                        System.out.println("            y = x");

                                        creditorToRemove = creditorLoop;

                                    }

                                }
                            }
                        }

                        // if we only have the case to add a creditor to this debitor, then we can proceed here
                        if ( (creditorUserToAdd != null) && (debitorUserToAdd == null) && (createAmount == null) ) {

                            // add the creditor
                            ActivitySummaryCreditorDTO creditorDTO = new ActivitySummaryCreditorDTO();
                            creditorDTO.setUser(creditorUserToAdd);
                            creditorDTO.setAmount(addAmount);
                            System.out.println("creditorUserToAdd : " + creditorDTO);
                            debitorLoop.getCreditors().add(creditorDTO);

                            // update the total
                            debitorLoop.setTotal(debitorLoop.getTotal().add(addAmount));

                        }

                        // remove a creditor and the debitor if there are none left
                        if (creditorToRemove != null) {

                            System.out.println("creditorToRemove : " + creditorToRemove);
                            debitorLoop.getCreditors().remove(creditorToRemove);

                            System.out.println("creditorToRemove : checking to see if we should remove this debitor also : " + debitorLoop.getCreditors().size());
                            if (debitorLoop.getCreditors().size() == 0) {
                                System.out.println("creditorToRemove : yes, remove");
                                removeDebitorLoopBecauseEmpty = 1;
                                debitorToRemove = debitorLoop;
                            } else {
                                System.out.println("creditorToRemove : no, do not remove because it has a size greater than 0");
                            }

                            // update the total
                            System.out.println("creditorToRemove : subtracting " + creditorToRemove.getAmount() + " from the debitorLoop total");
                            debitorLoop.setTotal(debitorLoop.getTotal().subtract(creditorToRemove.getAmount()));

                        }
                    }
                }

            }

            if (removeDebitorLoopBecauseEmpty == 1) {

                System.out.println("removeDebitorLoopBecauseEmpty : removing debitorLoop from debitors");
                debitors.remove(debitorToRemove);

                // no need to set the total because the debitor is getting removed
            }

            if ( (debitorUserToAdd != null) && (creditorUserToAdd != null) && (createAmount != null) ) {
                createCreditorAndDebitorWithAmountAndAddToDebitors(debitors, debitorUserToAdd, creditorUserToAdd, createAmount);
            }

        }
        else if (forwardFound == 0) {

            if (debug == 1) { System.out.println("    AAAAA : adding because not forwardFound, basically the creditor " + creditorUser.getFirstName() + " is not found in our debitors array"); }
            createCreditorAndDebitorWithAmountAndAddToDebitors(debitors, creditorUser, debitorUser, amount);

        }
        else if (forwardFound > 0) {

            if (debug == 1) { System.out.println("      FFFFF : forwardFound : " + forwardFound); }

            if (forwardFound == 1) {

                if (debug == 1) { System.out.println("         - add object to : " + debitorUser); }
                addCreditorWithAmountToExistingDebitorinDebitors(debitors, creditorUser, debitorUser,amount);

            } else if (forwardFound == 2) {

                if (debug == 1) { System.out.println("         - update the amount (" + amount + ") for debitor : " + debitorUser + " and creditor : " + creditorUser); }
                updateCreditorAmountForDebitorInDebitors(debitors, creditorUser, debitorUser, amount);

            }

        }

        System.out.println("----------------------------------------\n");

        // output the current debitors
        if (debug == 1) {
            System.out.println("***** ----------------------------------------------- *****");
            System.out.println("***** resulting array of debitors and their creditors *****");
            System.out.println("***** ----------------------------------------------- *****");
            int index = 1;
            for (ActivitySummaryDebitorsDTO debitorLoop : debitors) {
                System.out.println("  " + index + " : " + debitorLoop);
                index++;
            }
            System.out.println("***** ----------------------------------------------- *****");
        }
    }

}
