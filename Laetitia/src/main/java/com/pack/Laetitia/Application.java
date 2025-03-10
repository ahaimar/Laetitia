package com.pack.Laetitia;

import com.pack.Laetitia.model.entity.RolesEntity;
import com.pack.Laetitia.model.repositry.RoleRepo;
import com.pack.Laetitia.packManager.domin.RequestContext;
import com.pack.Laetitia.packManager.enums.Authority;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@Configuration
@EnableAsync(proxyTargetClass = true)  // Force class-based proxy for async
@EnableCaching(proxyTargetClass = true)  // Force class-based proxy for caching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepo roleRepo){

		return args -> {
//			RequestContext.setUserId(56434640L);
//			var userRole = new RolesEntity();
//			userRole.setName(Authority.USER.name());
//			userRole.setAuthorities(Authority.USER);
//			roleRepo.save(userRole);
//
//			var adminRole = new RolesEntity();
//			adminRole.setName(Authority.ADMIN.name());
//			adminRole.setAuthorities(Authority.ADMIN);
//			roleRepo.save(adminRole);
//			RequestContext.start();
		};
	}

}



















