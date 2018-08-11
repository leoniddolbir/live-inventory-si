package com.smkw.commerce.inventory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.smkw.commerce.inventory.annotations.Production;

@Configuration
@ImportResource("classpath:META-INF/production/integration/inventory-mail.xml")
@Production
public class MailerConfig {

}
