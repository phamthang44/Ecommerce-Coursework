package com.greenwich.ecommerce.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD }) //chỉ áp dụng cho các trường hợp là method hoặc field
@Retention(RetentionPolicy.RUNTIME) //chạy trên môi trường runtime
public @interface PhoneNumber {
    String message() default "Invalid phone number"; //message mặc định khi không hợp lệ
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
