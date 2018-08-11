package com.smkw.commerce.inventory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.smkw.commerce.inventory.annotations.Production;
import com.smkw.commerce.inventory.api.data.BaseEntity;
import com.smkw.commerce.inventory.integration.service.ServiceStatusJob;
import com.smkw.commerce.inventory.service.InventoryService;

@Configuration
@PropertySource("classpath:META-INF/production/database/db.properties")
@Production
@ComponentScan(basePackageClasses = { InventoryService.class, ServiceStatusJob.class })
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackageClasses = { BaseEntity.class })
@EnableTransactionManagement
public class InfrastructureConfig extends InfrastructureConfigAbstract {

}
