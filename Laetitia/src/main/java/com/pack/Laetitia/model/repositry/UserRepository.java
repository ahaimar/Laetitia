package com.pack.Laetitia.model.repositry;

import com.pack.Laetitia.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByFirstName(String name);

    Optional<UserEntity> findByEmailIgnoreCase(String mail);

    Optional<UserEntity> findUserByUserId(String userId);
}
