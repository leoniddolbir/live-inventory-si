package com.smkw.commerce.inventory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Slf4j
public class AppContextInitializer implements
		ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

	@Override
	public void initialize(
			AnnotationConfigWebApplicationContext applicationContext) {
		String aProfiles = getActiveProfile(applicationContext);
		log.debug("Setting active spring profile: " + aProfiles);
		applicationContext.getEnvironment().setActiveProfiles(aProfiles);
	}

	private String getActiveProfile(
			AnnotationConfigWebApplicationContext applicationContext) {
		String activeProfiles = applicationContext.getEnvironment()
				.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		if (activeProfiles != null) {
			return activeProfiles;
		}

		// default to development profile
		return "development";
	}

}
