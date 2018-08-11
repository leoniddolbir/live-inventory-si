package com.smkw.commerce.inventory.test.config;

import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ibm.as400.access.AS400ConnectionPool;
import com.smkw.commerce.inventory.api.data.BaseEntity;
import com.smkw.commerce.util.IdGenerator;

@Slf4j
@Configuration
@PropertySource("classpath:META-INF/development/database/db.properties")
@ComponentScan(basePackageClasses = { BaseEntity.class })
@EnableJpaRepositories(basePackageClasses = { BaseEntity.class })
@EnableTransactionManagement
public class InfrastructureTestsConfig {

	/**
	 * Bootstrap database
	 */
	@Bean
	public DataSource dataSource(Environment env) {
		log.info("Fetching properties");
		Properties info = new Properties();
		info.put("driver", env.getProperty("inventory.jdbc.driverClassName"));
		log.info("initialPoolSize is " + env.getProperty("inventory.jdbc.initialPoolSize"));
		info.put("initialPoolSize", env.getProperty("inventory.jdbc.initialPoolSize"));
		info.put("maxPoolSize", env.getProperty("inventory.jdbc.maxPoolSize"));
		info.put("minPoolSize", env.getProperty("inventory.jdbc.minPoolSize"));
		info.put("user", env.getProperty("inventory.jdbc.username"));
		info.put("libraries", env.getProperty("inventory.jdbc.schema"));
		info.put("password", env.getProperty("inventory.jdbc.password"));
		DriverManagerDataSource aDatasource = new DriverManagerDataSource(env.getProperty("inventory.jdbc.url"), info);
		log.info("Driver class is: " + env.getProperty("inventory.jdbc.driverClassName"));
		aDatasource.setDriverClassName(env.getProperty("inventory.jdbc.driverClassName"));
		return aDatasource;
	}

	/**
	 * Sets up a {@link LocalContainerEntityManagerFactoryBean} to use
	 * Hibernate. Activates picking up entities from the project's base package.
	 * 
	 * @return
	 */
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(Environment env,
			@Qualifier("dataSource") DataSource dataSource) {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Enum.valueOf(Database.class, env.getProperty("inventory.jpa.database")));
		vendorAdapter.setDatabasePlatform(env.getProperty("inventory.hibernate.dialect"));
		vendorAdapter.setGenerateDdl(Boolean.getBoolean(env.getProperty("inventory.jpa.generateDdl")));
		vendorAdapter.setShowSql(Boolean.getBoolean(env.getProperty("inventory.jpa.showSql")));

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(BaseEntity.class.getPackage().getName());
		factory.setDataSource(dataSource);

		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return txManager;
	}

	@Bean
	public IdGenerator idGenerator() {
		return IdGenerator.getCurrent();
	}

	@Bean
	public AS400ConnectionPool legacySystem() {
		return new AS400ConnectionPool();
	}

}
