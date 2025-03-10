package com.pack.Laetitia.controller;


import com.pack.Laetitia.model.dto.RequestUser;
import com.pack.Laetitia.packManager.domin.Response;
import com.pack.Laetitia.service.serv.ServiceUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.pack.Laetitia.packManager.util.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/user"})
public class UserResource {

    private final ServiceUser servUser;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Validated RequestUser user,
                                             HttpServletRequest request) {
        try{

            servUser.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());

            return  ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(),
                    "Account created , check your email to enable your account", CREATED));
        } catch (Exception e) {
            throw new RuntimeException("Exception for insert the user information \n"+e);
        }

    }

    @PostMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key,
                                             HttpServletRequest request) {
        try {

            servUser.verifyAccountKey(key);

            return  ResponseEntity.ok().body(getResponse(request, emptyMap(),
                    "Account verified ", OK));
        } catch (Exception e) {
            throw new RuntimeException("Exception for insert the user information \n"+e);
        }

    }


    private URI getUri() {

        return URI.create("http://localhost:9090/user/register");
    }


}
