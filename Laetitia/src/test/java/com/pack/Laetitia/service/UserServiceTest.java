package com.pack.Laetitia.service;

import com.pack.Laetitia.model.entity.CredentialEntity;
import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.entity.UserEntity;
import com.pack.Laetitia.model.repositry.CredentialRepository;
import com.pack.Laetitia.model.repositry.UserRepository;
import com.pack.Laetitia.packManager.enums.Authority;
import com.pack.Laetitia.service.serv.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.pack.Laetitia.packManager.enums.Authority.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mocked dependency

    @Mock
    private CredentialRepository credentialRepository;

    @InjectMocks
    private UserServiceImpl userService; // Service under test

    @Test
    @DisplayName("Test find user by ID")
    public void getUserByIdTest() {
        // Arrange - Given
        var userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setId(1L);
        userEntity.setUserId("1");
        userEntity.setCreatedAt(LocalDateTime.of(1990, 11, 1, 1, 11, 11));
        userEntity.setUpdatedAt(LocalDateTime.of(1990, 11, 1, 1, 11, 11));
        userEntity.setLastLogin(LocalDateTime.of(1990, 11, 1, 1, 11, 11));

        var roleEntity = new RolesEntity("USER", USER);
        userEntity.setRole(roleEntity);

        var credentialEntity = new CredentialEntity();
        credentialEntity.setUpdatedAt(LocalDateTime.of(1990, 11, 1, 1, 11, 11));
        credentialEntity.setPassword("password");
        credentialEntity.setUserEntity(userEntity);

        when(userRepository.findUserByUserId("1")).thenReturn(Optional.of(userEntity));
        when(credentialRepository.getCredentialByUserEntityId(1L)).thenReturn(Optional.of(credentialEntity));

        // Act - When
        var user = userService.getUserByUserId("1");

        // Assert - Then
        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(user.getUserId()).isEqualTo("1");
        assertThat(user.getRole()).isEqualTo("USER"); // Check role assignment
    }
}
