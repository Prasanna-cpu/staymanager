package com.spring.staymanager.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.staymanager.enums.Role;
import com.spring.staymanager.validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatch
public class RegisterRequest {

    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "phone number is required")
    @Length(min = 10, max = 10, message = "phone number is invalid")
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password is required")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "confirm password is required")
    private String confirmPassword;
    
    private Role role;

}
