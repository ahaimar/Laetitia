package com.pack.Laetitia.modle.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Confirmations")
@JsonInclude(NON_DEFAULT)
@JsonIdentityReference(alwaysAsId = true)
public class ConfirmationEntity extends Auditable{

        // name the token !!
    private String key;

    @OneToOne(targetEntity = UserEntity.class, fetch = EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(nullable = false, name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private UserEntity userEntity;

    public ConfirmationEntity(UserEntity userEntity) {

        this.userEntity = userEntity;
        this.key = UUID.randomUUID().toString();
    }
}
