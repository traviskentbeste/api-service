package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Category;
import com.walletsquire.apiservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category create(Category category) {

        return categoryRepository.save(category);

    }

    public Optional<Category> getById(Long id) {

        return categoryRepository.findById(id);

    }

    public List<Category> getAll() {

        return categoryRepository.findAll();

    }

    public Category update(Category category, Long id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (! categoryOptional.isPresent()) {
            return null;
        }

        Category existingCategory = categoryOptional.get();

        if ( (category.getName() != null) && (! category.getName().isEmpty()) ) {
            existingCategory.setName(category.getName());
        }
        if ( (category.getParent() != null) ) {
            existingCategory.setParent(category.getParent());
        }
    
        return categoryRepository.save(existingCategory);

    }

    public void delete(Category category) {

        Optional<Category> categoryOptional = categoryRepository.findById(category.getId());

        if (categoryOptional.isPresent()) {
            categoryRepository.delete(categoryOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            categoryRepository.delete(categoryOptional.get());
        }

    }

}
