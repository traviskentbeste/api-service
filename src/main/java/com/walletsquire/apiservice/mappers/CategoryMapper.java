package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.CategoryDTO;
import com.walletsquire.apiservice.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {
                CategoryMapperQualifier.class
        }
)
public interface CategoryMapper {

    @Mappings({
            @Mapping(target = "parent", source = "categoryDto.parent", qualifiedByName = "longToCategory")
    })
    Category toEntity(CategoryDTO categoryDto);

    @Mappings({
            @Mapping(target = "parent", source = "category.parent.id")
    })
    CategoryDTO toDto(Category category);

}