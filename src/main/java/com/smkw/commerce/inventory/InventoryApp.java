package com.smkw.commerce.inventory;

import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class InventoryApp extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected void customizeRegistration(Dynamic registration) {
		super.customizeRegistration(registration);
		registration.setInitParameter("contextInitializerClasses", AppContextInitializer.class.getName());
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] {InventoryConfig.class};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.support.
	 * AbstractAnnotationConfigDispatcherServletInitializer
	 * #getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
	 * #getServletMappings()
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
	 * #getServletFilters()
	 */
	@Override
	protected javax.servlet.Filter[] getServletFilters() {
		return new javax.servlet.Filter[] { new OpenEntityManagerInViewFilter() };
	}
}
