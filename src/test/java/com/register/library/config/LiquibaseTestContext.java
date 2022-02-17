package com.register.library.config;

import com.register.library.LibraryApplication;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Profile("test-db")
@SpringBootApplication
public class LiquibaseTestContext extends LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiquibaseTestContext.class, args);
	}

	@Value("${liquibase.change-log:null}")
	private String changeLog;

	@Value("${liquibase.enabled:true}")
	private boolean enabled;

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void init() {
		liquibase();
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(changeLog);
		liquibase.setDataSource(dataSource);
		liquibase.setShouldRun(enabled);
		return liquibase;
	}
}
