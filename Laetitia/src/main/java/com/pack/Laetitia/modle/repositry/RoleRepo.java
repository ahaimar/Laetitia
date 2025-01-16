package com.pack.Laetitia.modle.repositry;


import com.pack.Laetitia.modle.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<RolesEntity, Long> {

    Optional<RolesEntity> findByNameIgnoreCase(String name);
}
