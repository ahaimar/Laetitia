package com.pack.Laetitia.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pack.Laetitia.packManager.enums.Authority;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@Entity
@Table(name = "roles")
@DiscriminatorValue("roles")  //This will help in identifying the type of entity when retrieving data from the database.
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(NON_DEFAULT)
public class RolesEntity extends Auditable {

    private String name;
    private Authority authorities;
}
