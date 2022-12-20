package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Address;
import com.walletsquire.apiservice.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address create(Address address) {

        return addressRepository.save(address);

    }

    public Optional<Address> getById(Long id) {

        return addressRepository.findById(id);

    }

    public List<Address> getAll() {

        return addressRepository.findAll();

    }

    public Address update(Address address, Long id) {

        Optional<Address> addressOptional = addressRepository.findById(id);

        if (! addressOptional.isPresent()) {
            return null;
        }

        Address existingAddress = addressOptional.get();

        if ( (address.getStreetNumber() != null) && (! address.getStreetNumber().isEmpty()) ) {
            existingAddress.setStreetNumber(address.getStreetNumber());
        }
        if ( (address.getRoute() != null) && (! address.getRoute().isEmpty()) ) {
            existingAddress.setRoute(address.getRoute());
        }
        if ( (address.getLocality() != null) && (! address.getLocality().isEmpty()) ) {
            existingAddress.setLocality(address.getLocality());
        }
        if ( (address.getSubLocality() != null) && (! address.getSubLocality().isEmpty()) ) {
            existingAddress.setSubLocality(address.getSubLocality());
        }
        if ( (address.getNeighborhood() != null) && (! address.getNeighborhood().isEmpty()) ) {
            existingAddress.setNeighborhood(address.getNeighborhood());
        }
        if ( (address.getAdministrativeAreaLevel1() != null) && (! address.getAdministrativeAreaLevel1().isEmpty()) ) {
            existingAddress.setAdministrativeAreaLevel1(address.getAdministrativeAreaLevel1());
        }
        if ( (address.getAdministrativeAreaLevel2() != null) && (! address.getAdministrativeAreaLevel2().isEmpty()) ) {
            existingAddress.setAdministrativeAreaLevel2(address.getAdministrativeAreaLevel2());
        }
        if ( (address.getCountry() != null) && (! address.getCountry().isEmpty()) ) {
            existingAddress.setCountry(address.getCountry());
        }
        if ( (address.getContinent() != null) && (! address.getContinent().isEmpty()) ) {
            existingAddress.setContinent(address.getContinent());
        }
        if ( (address.getPostalCode() != null) && (! address.getPostalCode().isEmpty()) ) {
            existingAddress.setPostalCode(address.getPostalCode());
        }
        if ( (address.getLatitude() != null) ) {
            existingAddress.setLatitude(address.getLatitude());
        }
        if ( (address.getLongitude() != null) ) {
            existingAddress.setLongitude(address.getLongitude());
        }
        if ( (address.getPlaceId() != null) && (! address.getPlaceId().isEmpty()) ) {
            existingAddress.setPlaceId(address.getPlaceId());
        }
    
        return addressRepository.save(existingAddress);

    }

    public void delete(Address address) {

        Optional<Address> addressOptional = addressRepository.findById(address.getId());

        if (addressOptional.isPresent()) {
            addressRepository.delete(addressOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Address> addressOptional = addressRepository.findById(id);

        if (addressOptional.isPresent()) {
            addressRepository.delete(addressOptional.get());
        }

    }

}
