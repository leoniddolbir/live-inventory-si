package com.smkw.commerce.inventory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.smkw.commerce.inventory.annotations.Production;
import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAudit;
import com.smkw.commerce.inventory.integration.service.ItemMoveProcessor;

@PropertySource("classpath:META-INF/production/database/db.properties")
@Production
@EnableJpaRepositories(entityManagerFactoryRef = "auditEntityManagerFactory", basePackageClasses = InventoryAdjustmentRequestAudit.class)
@ComponentScan(basePackageClasses = { ItemMoveProcessor.class })
@Configuration
public class AuditConfig extends AuditConfigAbstract {
	/**
	 * Bootstrap database
	 */
}
