package com.pack.Laetitia;

import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.repositry.RoleRepository;
import com.pack.Laetitia.packManager.domin.RequestContext;
import com.pack.Laetitia.packManager.enums.Authority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static java.util.UUID.randomUUID;

@SpringBootApplication
@EnableJpaAuditing
@Configuration
@EnableAsync(proxyTargetClass = true)  // Force class-based proxy for async
@EnableCaching(proxyTargetClass = true)  // Force class-based proxy for caching
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
//			try {
//				RequestContext.setUserId(56434640L);
//
//				if (roleRepository.findByNameIgnoreCase(Authority.USER.name()).isEmpty()) {
//					var userRole = new RolesEntity();
//					userRole.setName(Authority.USER.name());
//					userRole.setAuthorities(Authority.USER);
//					roleRepository.save(userRole);
//				}
//
//				if (roleRepository.findByNameIgnoreCase(Authority.ADMIN.name()).isEmpty()) {
//					var adminRole = new RolesEntity();
//					adminRole.setName(Authority.ADMIN.name());
//					adminRole.setAuthorities(Authority.ADMIN);
//					roleRepository.save(adminRole);
//				}
//
//				RequestContext.start();
//			} catch (Exception e) {
//				log.error("Error initializing roles: ", e);
//			}
		};
	}


}
