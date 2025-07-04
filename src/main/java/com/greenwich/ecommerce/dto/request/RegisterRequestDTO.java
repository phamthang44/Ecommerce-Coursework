package com.greenwich.ecommerce.dto.request;

import com.greenwich.ecommerce.dto.validator.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO implements Serializable {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(
            description = "Email address of the user",
            example = "abc@gmail.com"
    )
    private String email;

    @NotBlank(message = "Username is required")
    @Schema(
            description = "The username of the user",
            example = "giao.lang"
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(
            description = "The password of the user",
            example = "abc12345678!@#"
    )
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message =
                    "The password must be at least 8 characters, including letters, numbers, and special characters."
    )
    private String password;

    @NotBlank(message = "Phone number is required")
    @PhoneNumber
    @Schema(
            description = "The phone number of the user",
            example = "01234567890"
    )
    private String phoneNumber;

    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[\\p{L} '-]{2,50}$", message = "Full name must contain only letters, spaces, hyphens or apostrophes (2–50 chars)")
    @Schema(
            description = "The full name of the user",
            example = "Nguyen Van A"
    )
    private String fullName;


}
