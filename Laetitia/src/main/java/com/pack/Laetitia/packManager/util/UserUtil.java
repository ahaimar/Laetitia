package com.pack.Laetitia.packManager.util;

import com.pack.Laetitia.model.dto.User;
import com.pack.Laetitia.model.entity.CredentialEntity;
import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.pack.Laetitia.packManager.Constants.NINETY_DAYS;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class UserUtil {

    public static UserEntity createUserEntity(String firstName, String lastName, String mail, RolesEntity role) {

        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(mail)
                .lastLogin(LocalDateTime.now())
                //.lastLogin(LocalDateTime.from(now()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .loginAttempts(0L)
                .qrCodeSecret(EMPTY)
                .phone(EMPTY)
                .bio(EMPTY)
                .imageUrl("https://cdn-icons-png.flaicon.com/512/149/149071.png")
                .role(role)
                .build();
    }

    public static User fromUserEntity(UserEntity userEntity, RolesEntity role, CredentialEntity credentialEntity) {

        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        user.setCreateAt(userEntity.getCreatedAt().toString());
        user.setUpdateAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());

        return user;
    }

    public static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {

//        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(ChronoLocalDateTime.from(now()));
        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(LocalDateTime.now());
    }

}
