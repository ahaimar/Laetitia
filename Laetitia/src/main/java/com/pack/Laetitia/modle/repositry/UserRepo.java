package com.pack.Laetitia.modle.repositry;

import com.pack.Laetitia.modle.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailIgnoreCase(String mail);
    Optional<UserEntity> findUserEntityByUserId(String userId);
}
