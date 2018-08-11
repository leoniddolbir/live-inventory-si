package com.smkw.commerce.inventory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.smkw.commerce.inventory.annotations.Development;
import com.smkw.commerce.inventory.api.data.BaseEntity;
import com.smkw.commerce.inventory.service.InventoryService;

@Configuration
@PropertySource("classpath:META-INF/development/database/db.properties")
@Development
@ComponentScan(basePackageClasses = { InventoryService.class })
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackageClasses = { BaseEntity.class })
@EnableTransactionManagement
public class InfrastructureConfigDev extends InfrastructureConfigAbstract {

}
