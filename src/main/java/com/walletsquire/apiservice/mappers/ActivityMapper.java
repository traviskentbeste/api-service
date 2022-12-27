package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.ActivityDTO;
import com.walletsquire.apiservice.entities.Activity;
import com.walletsquire.apiservice.entities.Address;
import com.walletsquire.apiservice.entities.Category;
import com.walletsquire.apiservice.services.AddressService;
import com.walletsquire.apiservice.services.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class ActivityMapper {

    @Autowired
    AddressService addressService;

    @Autowired
    CategoryService categoryService;

    public abstract Activity toEntity(ActivityDTO activityDto);
    
    public abstract ActivityDTO toDto(Activity activity);

    public Address mapLongToAddress(Long value) {
        if (value != null) {
            Optional<Address> addressOptional = addressService.getById(value);
            if (addressOptional.isPresent()) {
                return addressOptional.get();
            }
        }
        return null;
    }

    public Category mapLongToCategory(Long value) {
        if (value != null) {
            Optional<Category> categoryOptional = categoryService.getById(value);
            if (categoryOptional.isPresent()) {
                return categoryOptional.get();
            }
        }
        return null;
    }

    public Long mapCategoryToLong(Category value) {
        if (value != null) {
            if (value.getId() != null) {
                return value.getId();
            }
        }
        return null;
    }

    public Long mapAddressToLong(Address value) {
        if (value != null) {
            if (value.getId() != null) {
                return value.getId();
            }
        }
        return null;
    }



}