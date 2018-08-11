package com.smkw.commerce.inventory;

import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
@Slf4j
@Configuration

public abstract class AuditConfigAbstract {
	@Bean
	public DataSource auditDataSource(Environment env) {
		log.info("Creating Audit Datasource");
		Properties info = new Properties();
		info.put("driver", env.getProperty("audit.jdbc.driverClassName"));
		info.put("initialPoolSize", env.getProperty("audit.jdbc.initialPoolSize"));
		info.put("maxPoolSize", env.getProperty("audit.jdbc.maxPoolSize"));
		info.put("minPoolSize", env.getProperty("audit.jdbc.minPoolSize"));
		info.put("user", env.getProperty("audit.jdbc.username"));
		info.put("libraries", env.getProperty("audit.jdbc.schema"));
		info.put("password", env.getProperty("audit.jdbc.password"));
		DriverManagerDataSource aDatasource = new DriverManagerDataSource(
				env.getProperty("audit.jdbc.url"), info);
		aDatasource.setDriverClassName(env.getProperty("audit.jdbc.driverClassName"));
		
		return aDatasource;
	}

	/**
	 * Sets up a {@link LocalContainerEntityManagerFactoryBean} to use
	 * Hibernate. Activates picking up entities from the project's base package.
	 * 
	 * @return
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean auditEntityManagerFactory(
			Environment env,@Qualifier("auditDataSource") DataSource auditDataSource) {
		log.info("Creating Audit Entity Manager Factory");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Enum.valueOf(Database.class,
				env.getProperty("audit.jpa.database")));
		vendorAdapter.setDatabasePlatform(env.getProperty("audit.hibernate.dialect"));
		vendorAdapter.setGenerateDdl(Boolean.getBoolean(env.getProperty("audit.jpa.generateDdl")));
		vendorAdapter.setShowSql(Boolean.getBoolean(env.getProperty("audit.jpa.showSql")));

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPersistenceUnitName("audit");
		factory.setPackagesToScan(getClass().getPackage().getName());
		factory.setDataSource(auditDataSource);
		log.info("Finished Audit Entity Manager Factory");
		return factory;
	}

	@Bean
	public PlatformTransactionManager auditTransactionManager(@Qualifier("auditEntityManagerFactory")
			LocalContainerEntityManagerFactoryBean auditEntityManagerFactory) {
		log.info("Creating Audit Transaction Manager");
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(auditEntityManagerFactory.getObject());
		return txManager;
	}


}
