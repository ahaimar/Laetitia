package com.pack.Laetitia.packManager.util;

import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalTime.now;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class UserUtil {

    public static UserEntity createUserEntity(String firstName, String lastName, String mail, RolesEntity role) {

        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(mail)
                .lastLogin(LocalDateTime.from(now()))
                .accountNotExpired(true)
                .accountNotLocked(true)
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
}
