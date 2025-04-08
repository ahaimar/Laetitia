package com.pack.Laetitia.security;

import com.pack.Laetitia.service.interfaces.UserService;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class FilterChainConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(auth ->{
                    auth.loginPage("/user/login").permitAll();
                    })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/", "user/**","admin/**").authenticated()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userDetailsService, BCryptPasswordEncoder encoder) {

        var myOwnAuthenticationProvider = new ApiAuthenticationProvider(userDetailsService, encoder);
        return new ProviderManager(myOwnAuthenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        var hanna = User.withUsername("hanna")
                .password(encoder.encode("0000"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(hanna);
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        
        return new InMemoryUserDetailsManager(
                User.withUsername("junior").password("{noop}0000").roles("USER")
                        .build(),

                User.withUsername("hanne").password("{noop}0000").roles("USER")
                        .build()
        );
    }

}
