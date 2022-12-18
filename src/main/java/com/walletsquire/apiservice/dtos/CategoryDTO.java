package com.walletsquire.apiservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    @Size(min = 3, max = 64, message = "must be between 3 and 64 characters")
    private String name;

    @Min(value = 1, message = "should be greater than 1")
    private Long parent;

    @JsonIgnore
    private Set<CategoryDTO> children = new HashSet<>();

}