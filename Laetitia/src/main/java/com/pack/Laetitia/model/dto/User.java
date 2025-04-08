package com.pack.Laetitia.model.dto;

import lombok.*;

@Data
public class User {

    private Long id;
    private Long createdBy;
    private Long upDatedBy;

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;
    private String imageUrl;
    private String qrImageUrl;
    private String lastLogin;
    private String createAt;
    private String updateAt;
    private String role;
    private String authorities;

    // security properties
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean mfa;

}
