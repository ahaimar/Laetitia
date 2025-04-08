package com.pack.Laetitia.security;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.pack.Laetitia.model.dto.LoginRequest;
import com.pack.Laetitia.model.dto.User;
import com.pack.Laetitia.packManager.domin.Response;
import com.pack.Laetitia.service.interfaces.JwtService;
import com.pack.Laetitia.service.interfaces.UserService;
import com.pack.Laetitia.service.serv.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT;
import static com.pack.Laetitia.packManager.Constants.LOGIN_PATH;
import static com.pack.Laetitia.packManager.domin.ApiAuthentication.unauthenticated;
import static com.pack.Laetitia.packManager.enums.LoginType.LOGIN_ATTEMPT;
import static com.pack.Laetitia.packManager.enums.LoginType.LOGIN_SUCCESS;
import static com.pack.Laetitia.packManager.enums.TokenType.ACCESS;
import static com.pack.Laetitia.packManager.enums.TokenType.REFRESH;
import static com.pack.Laetitia.packManager.util.RequestUtils.getResponse;
import static com.pack.Laetitia.packManager.util.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserServiceImpl userService, JwtService jwtService) {
        super(new AntPathRequestMatcher(LOGIN_PATH, POST.name()), authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        try{
            var user = new ObjectMapper().configure( AUTO_CLOSE_JSON_CONTENT, true).readValue(request.getInputStream(), LoginRequest.class);
            userService.upDateLoginAttempt(user.getEmail(), LOGIN_ATTEMPT);
            var authentication = unauthenticated(user.getEmail(), user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            handleErrorResponse(request, response, exception);
            return null;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authentication);

        var user = (User) authentication.getPrincipal();
        userService.upDateLoginAttempt(user.getEmail(), LOGIN_SUCCESS);
        var httpResponse = user.isMfa() ? sendDrCode(request, user) : sendResponse(request, response, user);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());
        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {

        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user, REFRESH);

        return getResponse(request, Map.of("user", user), "Login Success", OK);
    }

    private Response sendDrCode(HttpServletRequest request, User user) {

        return getResponse(request, Map.of("user", user), "Please enter QR Code", OK);
    }

}
