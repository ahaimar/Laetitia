package com.pack.Laetitia.model.repositry;


import com.pack.Laetitia.model.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepo extends JpaRepository<CredentialEntity, Long> {

    Optional<CredentialEntity> getCredentialByUserEntityId(Long userId);
}
