package com.greenwich.ecommerce.service.impl;


import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryValidator {


    public void isValidCategoryId(Long id) {
        if (id == null || id <= 0) {
            log.error("Invalid category ID: {}", id);
            throw new InvalidDataException("Category ID must be a positive number");
        }
    }

    public void isValidCategoryName(String categoryName) {
        if (Util.isNullOrBlank(categoryName)) {
            log.error("Category name is required");
            throw new InvalidDataException("Category name is required");
        }
    }

    public void isValidCategoryDescription(String description) {
        if (Util.isNullOrBlank(description)) {
            log.error("Category description is required");
            throw new InvalidDataException("Category description is required");
        }
//        if (description.length() > 500) {
//            log.error("Category description exceeds maximum length of 500 characters");
//            throw new InvalidDataException("Category description must not exceed 500 characters");
//        }
        /*
         * The logic validate description should be as follows:
         *  - Check do not allow null or blank description
         *  - Check the length of the description
         *  - Check if the description contains any invalid characters
         *  - Check if the description may be having a space character at the beginning or end
         * Uncomment the above code if you want to enforce a maximum length for the category description.
         * Currently, it is commented out to allow longer descriptions.
         */
    }

}
