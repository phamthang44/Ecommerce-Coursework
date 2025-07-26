package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.dto.request.CategoryRequestDTO;
import com.greenwich.ecommerce.dto.response.CategoryResponseDTO;
import com.greenwich.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Converts a CategoryRequestDTO to a Category entity.
     *
     * @param category the CategoryRequestDTO to convert
     * @return the converted Category entity
     */
    @Mapping(source = "categoryName", target = "name")
    @Mapping(source = "categoryDescription", target = "description")
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    @Mapping(source = "name", target = "categoryName")
    @Mapping(source = "description", target = "description")
    CategoryResponseDTO toDto(Category category);


}
