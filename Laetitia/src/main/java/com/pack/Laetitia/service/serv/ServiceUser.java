package com.pack.Laetitia.service.serv;


import com.pack.Laetitia.modle.entity.ConfirmationEntity;
import com.pack.Laetitia.modle.entity.CredentialEntity;
import com.pack.Laetitia.modle.entity.RolesEntity;
import com.pack.Laetitia.modle.entity.UserEntity;
import com.pack.Laetitia.modle.repositry.ConfirmationRepo;
import com.pack.Laetitia.modle.repositry.CredentialRepo;
import com.pack.Laetitia.modle.repositry.RoleRepo;
import com.pack.Laetitia.modle.repositry.UserRepo;
import com.pack.Laetitia.packManager.enums.Authority;
import com.pack.Laetitia.packManager.event.UserEvent;
import com.pack.Laetitia.packManager.exceptio.ApiException;
import com.pack.Laetitia.service.impl.UserImp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pack.Laetitia.packManager.enums.EventType.REGISTRATION;
import static com.pack.Laetitia.packManager.util.UserUtil.createUserEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class ServiceUser implements UserImp {

    private final UserRepo userRepo;
    private final ConfirmationRepo confirmationRepo;
    private final CredentialRepo credentialRep;
    private final RoleRepo roleRepo;
    //private final BCryptPasswordEncoder encoder;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String mail, String password){

        var userEntity = userRepo.save(createNewUser(firstName, lastName, mail));
        var credential = new CredentialEntity(password ,userEntity);
        credentialRep.save(credential);
        var confirmation = new ConfirmationEntity(userEntity);
        confirmationRepo.save(confirmation);
        publisher.publishEvent(new UserEvent(userEntity, REGISTRATION,
                Map.of("Key", confirmation.getKey())));
    }

    @Override
    public RolesEntity getRoleName(String name){

        var role = roleRepo.findByNameIgnoreCase(name);
        return  role.orElseThrow(()->new ApiException("Role not found: " + name));
    }

    private UserEntity createNewUser(String firstName, String lastName, String mail) {

        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName, lastName, mail, role);
    }
}