package com.pack.Laetitia.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
@DiscriminatorValue("users")  //This will help you determine the entity type when retrieving data from the database.
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(NON_DEFAULT)
public class UserEntity extends Auditable {

    @Column(updatable = false, nullable = false, unique = true)
    private String userId;
    private String firstName;
    private String lastName;
    @Column(name = "mail" ,nullable = false, unique = true)
    private String email;
    private Long loginAttempts;
    private LocalDateTime lastLogin;
    private String phone;
    private String bio;
    private String imageUrl;

    // security properties
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean enabled;
    private boolean mfa;

    @JsonIgnore
    private String qrCodeSecret;
    @Column(name = "qr_Image_Url", columnDefinition = "text")
    private String qrImageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @NotFound(action = NotFoundAction.IGNORE)
    private RolesEntity role;
}
