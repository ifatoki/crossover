package com.company.resourceapi.config;

import java.util.Optional;

import com.company.resourceapi.repositories.ProjectSpecification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ProjectConfig {
	
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.ofNullable("SYSTEM");
	}

	@Bean
	public ProjectSpecification projectSpecification() {
		return new ProjectSpecification();
	}
}