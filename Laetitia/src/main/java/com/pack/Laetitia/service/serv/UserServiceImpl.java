package com.pack.Laetitia.service.serv;


import com.pack.Laetitia.model.dto.User;
import com.pack.Laetitia.model.entity.ConfirmationEntity;
import com.pack.Laetitia.model.entity.CredentialEntity;
import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.entity.UserEntity;
import com.pack.Laetitia.model.repositry.ConfirmationRepository;
import com.pack.Laetitia.model.repositry.CredentialRepository;
import com.pack.Laetitia.model.repositry.RoleRepository;
import com.pack.Laetitia.model.repositry.UserRepository;
import com.pack.Laetitia.packManager.cache.CacheStore;
import com.pack.Laetitia.packManager.domin.RequestContext;
import com.pack.Laetitia.packManager.enums.Authority;
import com.pack.Laetitia.packManager.enums.LoginType;
import com.pack.Laetitia.packManager.event.UserEvent;
import com.pack.Laetitia.packManager.exceptio.ApiException;

import com.pack.Laetitia.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pack.Laetitia.packManager.enums.EventType.REGISTRATION;
import static com.pack.Laetitia.packManager.util.UserUtil.createUserEntity;
import static com.pack.Laetitia.packManager.util.UserUtil.fromUserEntity;
import static java.time.LocalDateTime.now;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final CredentialRepository credentialRep;
    private final RoleRepository roleRepository;
    //private final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Long> userCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String mail, String password){

        var userEntity = userRepository.save(createNewUser(firstName, lastName, mail));
        var credential = new CredentialEntity(password ,userEntity);
        credentialRep.save(credential);
        var confirmation = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmation);
        publisher.publishEvent(new UserEvent(userEntity, REGISTRATION,
                Map.of("Key", confirmation.getKey())));
    }

    @Override
    public RolesEntity getRoleName(String name){

        var role = roleRepository.findByNameIgnoreCase(name);
        return  role.orElseThrow(()->new ApiException("Role not found: " + name));
    }

    @Override
    public void verifyAccountKey(String key) {

        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);

        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);

    }

    @Override
    public void upDateLoginAttempt(String email, LoginType loginType) {

        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());

        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0L);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if (userCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0L);
                userEntity.setLastLogin(now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public User getUserByUserId(String userId) {

        var userEntity = userRepository.findUserByUserId(userId).orElseThrow(()-> new ApiException("User not found! "));

        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {

        UserEntity userEntity = getUserEntityByEmail(email);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public CredentialEntity getUserCredentialById(Long userId) {

        var credentialByUserId = credentialRep.getCredentialByUserEntityId(userId);

        return credentialByUserId.orElseThrow(()->new ApiException("Unable to find user credential"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String mail) {

        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName, lastName, mail, role);
    }

    private UserEntity getUserEntityByEmail(String email) {

        var userByEmail = userRepository.findByEmailIgnoreCase(email);

        return userByEmail.orElseThrow(()-> new ApiException("User not found. "));
    }

    private ConfirmationEntity getUserConfirmation(String key) {

        return confirmationRepository.findByKey(key).orElse(null);
    }
}