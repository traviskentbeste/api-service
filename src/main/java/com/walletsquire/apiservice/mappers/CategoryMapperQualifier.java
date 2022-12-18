package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.entities.Category;
import com.walletsquire.apiservice.services.CategoryService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryMapperQualifier {

    @Autowired
    private CategoryService categoryService;

    @Named("longToCategory")
    public Category longToCategory(Long value) {
//        System.out.println("longToCategory : -->" + value + "<--");
        if (value != null) {
            Optional<Category> optional = categoryService.getById(value);
            if (optional.isPresent()) {
//                System.out.println("is present");
                return optional.get();
            }
//            System.out.println("not present");
        }
        return null;
    }

}
