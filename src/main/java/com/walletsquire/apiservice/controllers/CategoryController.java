package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.CategoryDTO;
import com.walletsquire.apiservice.entities.Category;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.CategoryMapper;
import com.walletsquire.apiservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{baseURI}/v{version}")
@Validated
public class CategoryController {
    public static final String endpoint = "/category";

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO categoryDto) {

        Category category = categoryMapper.toEntity(categoryDto);
        category = categoryService.create(category);
        categoryDto = categoryMapper.toDto(category);
        return new ResponseEntity<>(categoryDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Category> categoryOptional = categoryService.getById(id);

        if (!categoryOptional.isPresent()) {
            throw new EntityNotFoundException(Category.class, "id", id.toString());
        }

        CategoryDTO categoryDto  = categoryMapper.toDto(categoryOptional.get());

        return new ResponseEntity<>(categoryDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<CategoryDTO> getAll() {

        List<CategoryDTO> categoryDtoList = categoryService.getAll()
                .stream()
                .map(category -> {
                    return categoryMapper.toDto(category);
                }).collect(Collectors.toList());

        return categoryDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO categoryDto , @PathVariable @Min(1) Long id) {

        Optional<Category> categoryOptional = categoryService.getById(id);

        if (!categoryOptional.isPresent()) {
            throw new EntityNotFoundException(Category.class, "id", id.toString());
        }

        Category category = categoryMapper.toEntity(categoryDto );
        category = categoryService.update(category, id);
        categoryDto  = categoryMapper.toDto(category);

        return new ResponseEntity<>(categoryDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // category not found by the id
        Optional<Category> categoryOptional = categoryService.getById(id);

        if (!categoryOptional.isPresent()) {
            throw new EntityNotFoundException(Category.class, "id", id.toString());
        }

        // do the delete
        categoryService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}