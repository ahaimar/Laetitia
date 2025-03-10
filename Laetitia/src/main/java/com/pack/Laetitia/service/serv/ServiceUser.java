package com.pack.Laetitia.service.serv;


import com.pack.Laetitia.model.entity.ConfirmationEntity;
import com.pack.Laetitia.model.entity.CredentialEntity;
import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.entity.UserEntity;
import com.pack.Laetitia.model.repositry.ConfirmationRepo;
import com.pack.Laetitia.model.repositry.CredentialRepo;
import com.pack.Laetitia.model.repositry.RoleRepo;
import com.pack.Laetitia.model.repositry.UserRepo;
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

    @Override
    public void verifyAccountKey(String key) {

        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);

        userRepo.save(userEntity);
        confirmationRepo.delete(confirmationEntity);

    }

    private UserEntity createNewUser(String firstName, String lastName, String mail) {

        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName, lastName, mail, role);
    }

    private UserEntity getUserEntityByEmail(String email) {

        var userByEmail = userRepo.findByEmailIgnoreCase(email);

        return userByEmail.orElseThrow(()-> new ApiException("User not found. "));
    }

    private ConfirmationEntity getUserConfirmation(String key) {

        return confirmationRepo.findByKey(key).orElse(null);
    }
}