package com.smkw.commerce.inventory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@Import({ InfrastructureConfigDev.class, MailerConfigDev.class, AuditConfigDev.class, IntegrationConfigDev.class,
		InfrastructureConfig.class, MailerConfig.class, AuditConfig.class, IntegrationConfig.class })
public class InventoryConfig {

}
