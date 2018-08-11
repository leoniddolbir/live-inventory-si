package com.smkw.commerce.inventory.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:META-INF/development/integration/inventory-mail.xml")

public class MailerConfig {

}
