package com.smkw.commerce.inventory.service;

public class LegacySystemException extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6732550040765942650L;

	public LegacySystemException(String aMessage, Throwable anException) {
		super(aMessage, anException);
	}
}
