package com.smkw.commerce.inventory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.smkw.commerce.inventory.annotations.Development;

@Configuration
@ImportResource("classpath:META-INF/development/integration/inventory-integration.xml")
@Development
public class IntegrationConfigDev {
}
