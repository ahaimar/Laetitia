package com.pack.Laetitia.service.interfaces;

import com.pack.Laetitia.model.dto.User;
import com.pack.Laetitia.model.entity.CredentialEntity;
import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.packManager.enums.LoginType;

public interface UserService {

    void createUser(String username, String LastName, String mail, String password);

    RolesEntity getRoleName(String name);

    void verifyAccountKey(String key);

    void upDateLoginAttempt(String email, LoginType loginType);

    User getUserByUserId(String userId);

    User getUserByEmail(String email);

    CredentialEntity getUserCredentialById(Long id);
}
