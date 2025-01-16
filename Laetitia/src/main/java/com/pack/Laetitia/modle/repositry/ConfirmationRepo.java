package com.pack.Laetitia.modle.repositry;

import com.pack.Laetitia.modle.entity.ConfirmationEntity;
import com.pack.Laetitia.modle.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepo extends JpaRepository<ConfirmationEntity, Long> {

    Optional<ConfirmationEntity> findByKey(String key);
    Optional<ConfirmationEntity> findByUserEntity(UserEntity user);
}
