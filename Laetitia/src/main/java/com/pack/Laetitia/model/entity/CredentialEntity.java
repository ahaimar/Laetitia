package com.pack.Laetitia.model.entity;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@Table(name = "Credential")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(NON_DEFAULT)
@JsonIdentityReference(alwaysAsId = true)
public class CredentialEntity extends Auditable {

    private String password;

    @OneToOne(targetEntity = UserEntity.class, fetch = EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(nullable = false, name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private UserEntity userEntity;

}
