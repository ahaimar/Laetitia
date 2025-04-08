package com.pack.Laetitia.packManager.domin;


import com.pack.Laetitia.model.dto.User;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@Setter
public class TokenData {

    private User user;
    private Claims Claims;
    private boolean valid;
    private List<GrantedAuthority> authorities;
}
