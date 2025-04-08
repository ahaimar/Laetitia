package com.pack.Laetitia.service.serv;


import com.pack.Laetitia.model.dto.User;
import com.pack.Laetitia.packManager.domin.Token;
import com.pack.Laetitia.packManager.domin.TokenData;
import com.pack.Laetitia.packManager.enums.TokenType;
import com.pack.Laetitia.packManager.function.TriConsumer;
import com.pack.Laetitia.security.JwtConfiguration;
import com.pack.Laetitia.service.interfaces.JwtService;
import com.pack.Laetitia.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.pack.Laetitia.packManager.Constants.*;
import static com.pack.Laetitia.packManager.enums.TokenType.ACCESS;
import static com.pack.Laetitia.packManager.enums.TokenType.REFRESH;
import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.springframework.boot.web.server.Cookie.SameSite.NONE;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfiguration implements JwtService {

    private final UserService userService;

    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {
        var token = Token.builder().access(builderToken.apply(user, ACCESS)).refresh(builderToken.apply(user, REFRESH)).build();
        return tokenFunction.apply(token);
    }

    @Override
    public Optional<String> extractToken(HttpServletRequest request, String cookieName) {

        return extractToken.apply(request, cookieName);
    }

    @Override
    public void addCookie(HttpServletResponse response, User user, TokenType type) {

        addCookie.accept(response, user, type);
    }

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenFunction) {
        return tokenFunction.apply(
                TokenData.builder()
                        .valid(Objects.equals(userService.getUserByUserId(subject.apply(token)).getUserId()
                                , claimsFunction.apply(token).getSubject()))
                        .authorities(authorities.apply(token))
                        .Claims(claimsFunction.apply(token))
                        .user(userService.getUserByUserId(subject.apply(token)))
                        .build()
        );
    }

    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        var optionalCookie = extractCookie.apply(request, cookieName);

        if (optionalCookie.isPresent()) {
            var cookie = optionalCookie.get();
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    private <T> T getClaimsValue(String token, Function<Claims, T> claims) {

        return claimsFunction.andThen(claims).apply(token);
    }

    private final Supplier<SecretKey> key = () -> {
        String secret = getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret key cannot be null or empty");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    };

    private final Function<String, Claims> claimsFunction = token ->
            Jwts.parserBuilder()
                    .setSigningKey(key.get())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


    private final Function<String, String> subject = token -> getClaimsValue(token, Claims::getSubject);

    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken = (request, cookieName) -> {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    };

    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie = (request, cookieName) -> {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                .findAny();
    };

    private final Supplier<JwtBuilder> builder = () -> Jwts.builder()
            .setHeader(Map.of(TYPE, JWT_TYPE))
            .setAudience(GET_ARRAYS_LLC)
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date.from(Instant.now()))
            .setNotBefore(new Date())
            .signWith(HS512, key.get());


    private final BiFunction<User, TokenType, String> builderToken = (user, type) -> {
        var jwtBuilder = builder.get()
                .setSubject(user.getUserId())
                .setExpiration(Date.from(Instant.now().plusSeconds(getExpiration())));

        if (TokenType.equals(type, ACCESS)) {
            jwtBuilder
                    .claim(AUTHORITIES, user.getAuthorities())
                    .claim(ROLE, user.getRole());
        }

        return jwtBuilder.compact();
    };

    private final TriConsumer<HttpServletResponse, User, TokenType> addCookie = (response, user, type) -> {

        switch (type) {
            case ACCESS -> {
                var accessToken = createToken(user, Token::getAccess);
                var cookie = new Cookie(type.getValue(), accessToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setMaxAge(2*60);
                cookie.setPath("/");
                cookie.setAttribute("SameSite", NONE.name());
                response.addCookie(cookie);
            }
            case REFRESH -> {
                var refreshToken = createToken(user, Token::getRefresh);
                var cookie = new Cookie(type.getValue(), refreshToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setMaxAge(2 * 60 * 60);
                cookie.setPath("/");
                cookie.setAttribute("SameSite", NONE.name());
                response.addCookie(cookie);
            }
        }
    };

    public Function<String, List<GrantedAuthority>> authorities = token -> {

        String authorities = claimsFunction.apply(token).get(AUTHORITIES, String.class);
        String role = claimsFunction.apply(token).get(ROLE, String.class);

        // Handle potential null values to avoid NullPointerException
        StringJoiner joiner = new StringJoiner(AUTHORITY_DELIMITER);
        if (authorities != null) joiner.add(authorities);
        if (role != null) joiner.add(ROLE_PREFIX + role);

        return commaSeparatedStringToAuthorityList(joiner.toString());
    };
}
