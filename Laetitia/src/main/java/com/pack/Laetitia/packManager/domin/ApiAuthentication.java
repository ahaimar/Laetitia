package com.pack.Laetitia.packManager.domin;

import com.pack.Laetitia.model.dto.User;
import com.pack.Laetitia.packManager.exceptio.ApiException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class ApiAuthentication extends AbstractAuthenticationToken {

    private static final String PASSWORD_PROTECTED = "[PASSWORD PROTECTED]";
    private static final String EMAIL_PROTECTED = "[EMAIL PROTECTED]";

    private User user;
    @Getter
    @Setter
    private String emial;
    @Getter
    @Setter
    private String password;
    private boolean authenticated;

    private ApiAuthentication(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.password = PASSWORD_PROTECTED;
        this.emial = EMAIL_PROTECTED;
        this.authenticated = true;
    }

    private ApiAuthentication(String email, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);

        this.password = password;
        this.emial = email;
        this.authenticated = false;
    }

    public static ApiAuthentication unauthenticated (String email, String password) {

        return new ApiAuthentication(email, password);
    }

    public static ApiAuthentication authenticated(User user,Collection<? extends GrantedAuthority> authorities) {

        return new ApiAuthentication(user, authorities);
    }

    @Override
    public Object getCredentials() {
        return PASSWORD_PROTECTED;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {

        throw new ApiException("You can set the authenticated");
        //super.setAuthenticated(authenticated);
    }

    @Override
    public boolean isAuthenticated() {

        return this.authenticated;
    }
}
