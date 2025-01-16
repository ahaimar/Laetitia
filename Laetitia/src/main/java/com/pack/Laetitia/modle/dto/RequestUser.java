package com.pack.Laetitia.modle.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pack.Laetitia.modle.entity.RolesEntity;
import com.pack.Laetitia.modle.entity.Auditable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDateTime;

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

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    private String phone;

    private String bio;
}
