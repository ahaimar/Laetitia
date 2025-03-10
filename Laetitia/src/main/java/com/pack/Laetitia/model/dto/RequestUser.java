package com.pack.Laetitia.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pack.Laetitia.model.entity.Auditable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUser extends Auditable {

            /*
            *       this class is responsible for creating the database and loading the database
            * */

    @NotEmpty(message = "First name cannot be empty or null")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty or null")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    private String phone;

    private String bio;
}
