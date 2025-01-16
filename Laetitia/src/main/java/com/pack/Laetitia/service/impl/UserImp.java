package com.pack.Laetitia.service.impl;

import com.pack.Laetitia.modle.entity.RolesEntity;

public interface UserImp {

    void createUser(String username, String LastName, String mail, String password);

    RolesEntity getRoleName(String name);
}
